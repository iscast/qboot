package org.qboot.sys.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qboot.sys.dto.SysMenuDto;
import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.service.impl.SysMenuService;
import org.qboot.sys.service.impl.SysRoleService;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.AuthTreeEntity;
import org.qboot.common.utils.TreeHelper;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Title: MenuController</p>
 * <p>Description: 系统菜单权限</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/menu")
public class MenuController extends BaseController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysMenuService sysMenuService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	private TreeHelper<SysMenuDto> treeHelper = new TreeHelper<SysMenuDto>();

	@GetMapping("/qryAuth")
	public ResponeModel qryAuth() {
		if(SecurityUtils.getUserId() == null) {
			return ResponeModel.error("sys.response.msg.failToLoadAuth");
		}
		List<SysMenuDto> list = new ArrayList<SysMenuDto>();
		if(SecurityUtils.isSuperAdmin()) {
			SysMenuDto sysMenu = new SysMenuDto();
			sysMenu.setSortField("sort,parent_ids");
			sysMenu.setDirection(SysConstants.ASC);
			list = sysMenuService.findList(sysMenu);
		}else {
			list = sysMenuService.qryAuth(SecurityUtils.getUserId());
		}
		
		return ResponeModel.ok(treeHelper.treeGridList(list));
	}
	
	@GetMapping("/qryAllMenus")
	public ResponeModel qryAllMenus(@RequestParam(required=false) String roleId) {
		try {
			
			List<SysMenuDto> list = new ArrayList<SysMenuDto>();
			if(!SecurityUtils.isSuperAdmin()) {
				List<SysRoleDto> roles = sysRoleService.findByUserId(SecurityUtils.getUserId());
				Set<String> menuIdList = new HashSet<String>();
				if(!CollectionUtils.isEmpty(roles)) {
					for(SysRoleDto role : roles) {
						List<SysMenuDto> sysMenus = sysMenuService.findByRoleId(role.getId());
						if(CollectionUtils.isEmpty(sysMenus)) {
							continue;
						}
						sysMenus = sysMenus.stream().filter(p -> !menuIdList.contains(p.getId())).collect(Collectors.toList());
						if(CollectionUtils.isEmpty(sysMenus)) {
							continue;
						}
						list.addAll(sysMenus);
						List<String> menuIds = sysMenus.stream().map(p->p.getId()).collect(Collectors.toList());
						menuIdList.addAll(menuIds);
					}
				}
			}else {
				SysMenuDto sysMenu = new SysMenuDto();
				sysMenu.setSortField("sort,parent_ids");
				sysMenu.setDirection(SysConstants.ASC);
				list = sysMenuService.findList(sysMenu);
			}
			
			List<SysMenuDto> roleAuths = new ArrayList<SysMenuDto>();
			if(StringUtils.isNotBlank(roleId)) {
				roleAuths = sysMenuService.findByRoleId(roleId);
			}
			List<AuthTreeEntity> treeList = authTreeList(list, roleAuths);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("trees", treeList);
			return ResponeModel.ok(retMap);
		} catch (Exception e) {
			logger.error("权限加载失败, {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponeModel.error();
	}
	
	@GetMapping("/qryMenus")
	public ResponeModel qryParentMenus(@RequestParam(required=false) String parentId) {
		List<SysMenuDto> list = sysMenuService.findMenuByParentId(parentId);
		return ResponeModel.ok(list);
	}
	
	@GetMapping("/qryParentMenus")
	public ResponeModel qryParentMenus(@Validated SysMenuDto sysMenu, BindingResult bindingResult) {
		sysMenu.setIsShow(sysMenu.getIsShow());
		sysMenu.setSortField("id,sort");
		sysMenu.setDirection(SysConstants.DESC);
		List<SysMenuDto> list = sysMenuService.findParentMenuList(sysMenu);
		return ResponeModel.ok(list);
	}
	
	@PreAuthorize("hasAuthority('sys:menu:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysMenuDto sysMenu = sysMenuService.findById(id);
		return ResponeModel.ok(sysMenu);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:menu:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysMenuDto sysMenu, BindingResult bindingResult) {
		SysMenuDto menu = sysMenuService.findByPermission(sysMenu.getPermission());
		if(menu != null) {
			return ResponeModel.error("authDuplicate");
		}
		
		SysMenuDto parent = null;
		if (sysMenu.getParentId() != null && !sysMenu.getParentId().equals("0") && !sysMenu.getParentId().equals("")) {
			parent = sysMenuService.findById(sysMenu.getParentId());
		} 
		treeHelper.setParent(sysMenu, parent);
		sysMenu.setIsShow("1");
		sysMenu.setCreateBy(SecurityUtils.getLoginName());
		sysMenu.setCreateDate(new Date());
		int cnt = sysMenuService.save(sysMenu);
		if(cnt > 0) {
			return ResponeModel.ok();
		}else {
			return ResponeModel.error();
		}
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:menu:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysMenuDto sysMenu, BindingResult bindingResult) {
		SysMenuDto menu = sysMenuService.findByPermission(sysMenu.getPermission());
		if(menu != null && !String.valueOf(menu.getId()).equals(sysMenu.getId())) {
			return ResponeModel.error("authDuplicate");
		}
		
		SysMenuDto parent = null;
		if (sysMenu.getParentId() != null && !sysMenu.getParentId().equals("0") && !sysMenu.getParentId().equals("")) {
			parent = sysMenuService.findById(sysMenu.getParentId());
		} 
		treeHelper.setParent(sysMenu, parent);
		sysMenu.setUpdateBy(SecurityUtils.getLoginName());
		sysMenu.setUpdateDate(new Date());
		int cnt = sysMenuService.updateSelecter(sysMenu);
		if(cnt > 0) {
			return ResponeModel.ok();
		}else {
			return ResponeModel.error();
		}
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:menu:delete')")
	@GetMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysMenuService.deleteById(id);
		if(cnt > 0) {
			return ResponeModel.ok();
		}else {
			return ResponeModel.error();
		}
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:menu:save')")
	@PostMapping("/batchSave")
	public ResponeModel batchSave(@RequestBody List<SysMenuDto> list ) {
		sysMenuService.batchSave(list);
		return ResponeModel.ok();
	}
	
	@PostMapping("/qryByRoleId")
	public ResponeModel qryMenuByRoleId(@RequestParam(required=false) String roleId) {
		return ResponeModel.ok(sysMenuService.findByRoleId(roleId));
	}
	
	@PreAuthorize("hasAuthority('sys:menu:update')")
	@GetMapping("/changeShowFlag")
	public ResponeModel changeShowFlag(@RequestParam(required=false) String id, @RequestParam(required=false) String isShow) {
		try {
			SysMenuDto sysMenu = sysMenuService.findById(id);
			if(sysMenu != null) {
				if(sysMenu.getPermission().equals("sys:menu") || sysMenu.getPermission().equals("sys:menu:save")  || sysMenu.getPermission().equals("sys:menu:update") 
						|| sysMenu.getPermission().equals("sys:menu:delete") || sysMenu.getPermission().equals("sys:menu:qry")) {
					return ResponeModel.error("cannotDisableMenu");
				}
				sysMenu.setIsShow(isShow);
				int cnt = sysMenuService.changeShowFlag(id, isShow);
				return ResponeModel.ok(cnt);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("修改菜单状态失败！{}", ExceptionUtils.getStackTrace(e));
		}
		return ResponeModel.error();
	}

    private List<AuthTreeEntity> authTreeList(List<SysMenuDto> list, List<SysMenuDto> roleAuths) {
        Map<String, String> authMap = getRoleAuthMap(roleAuths);
        List<AuthTreeEntity> result = this.nextAuthTreeList(list, TreeHelper.DEFAULT_PARENTID, authMap);
        return result;
    }

    private List<AuthTreeEntity> nextAuthTreeList(List<SysMenuDto> list, String parentId, Map<String, String> authMap){
        List<AuthTreeEntity> result = new ArrayList<>();
        for (SysMenuDto t : list) {//
            if (parentId.equals(t.getParentId())) {
                // 从跟节点开始
                AuthTreeEntity ate = new AuthTreeEntity();
                ate.setName(t.getName());
                ate.setValue(t.getId());
                ate.setChecked(authMap.get(t.getId())==null?false:true);
                if(!t.getType().equals("999")) {
                    ate.setList(this.nextAuthTreeList(list, t.getId(), authMap));
                }
                result.add(ate);
            }
        }
        return result;
    }

    private Map<String, String> getRoleAuthMap(List<SysMenuDto> roleAuths){
        Map<String, String> authMap = new HashMap<String, String>();
        if(!CollectionUtils.isEmpty(roleAuths)) {
            for(SysMenuDto menu : roleAuths) {
                authMap.put(menu.getId(), menu.getId());
            }
        }
        return authMap;
    }

}
