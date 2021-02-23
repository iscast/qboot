package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.IdGen;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.common.utils.RedisTools;
import org.qboot.common.utils.ValidateUtils;
import org.qboot.sys.dto.SysParamClassDto;
import org.qboot.sys.dto.SysParamTypeDto;
import org.qboot.sys.service.SysParamClassService;
import org.qboot.sys.service.SysParamTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * 系统类型
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@RequestMapping("${admin.path}/sys/paramclass")
public class SysParamClassController extends BaseController {

	@Autowired
	private SysParamClassService sysParamClassService;
    @Autowired
    private SysParamTypeService sysParamTypeService;
    @Autowired
    private RedisTools redisTools;

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysParamClassDto sysParam) {
		PageInfo<SysParamClassDto> page = sysParamClassService.findByPage(sysParam);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:param:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam String id) {
		SysParamClassDto sysParam = sysParamClassService.findById(id);
		if(null == sysParam) {
            return ResponeModel.error(SYS_PARAM_CLASS_QUERY_FAIL);
        }
		return ResponeModel.ok(sysParam);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysParamClassDto sysParam, BindingResult bindingResult) {
        ValidateUtils.checkBind(bindingResult);
        sysParam.setId(IdGen.uuid());
		sysParam.setCreateBy(SecurityUtils.getLoginName());
		sysParam.setVisible(1); // 默认可用
		sysParam.setPhysicsFlag(SysConstants.SYS_DELFLAG_NORMAL);
		if(sysParamClassService.save(sysParam) > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_CLASS_SAVE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysParamClassDto sysParam, BindingResult bindingResult) {
        ValidateUtils.checkBind(bindingResult);
		sysParam.setUpdateBy(SecurityUtils.getLoginName());
		if(sysParamClassService.update(sysParam) > 0) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + sysParam.getParamTypeClass());
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_CLASS_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam String id, @RequestParam String paramTypeClass) {
        MyAssertTools.notNull(id, SYS_PARAM_CLASS_ID_NULL);
		if(sysParamClassService.deleteById(id) > 0) {
            try {
                redisTools.del(CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + paramTypeClass);
            } catch (Exception e) {
                logger.error("clear {} redis cache fail", CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + paramTypeClass);
            }
            List<SysParamTypeDto> paramTypes = sysParamTypeService.findParamTypes(paramTypeClass);
            for(SysParamTypeDto dto : paramTypes) {
                sysParamTypeService.deleteById(dto.getId());
            }

			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_CLASS_DELETE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/visible")
	public ResponeModel visible(@RequestParam String id, @RequestParam Integer visible) {
        MyAssertTools.notNull(id, SYS_PARAM_CLASS_ID_NULL);
		SysParamClassDto sysParam = new SysParamClassDto();
		sysParam.setId(id);
		sysParam.setVisible(visible);
        sysParam.setUpdateDate(new Date());
        sysParam.setUpdateBy(SecurityUtils.getLoginName());
		if(sysParamClassService.update(sysParam) > 0) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + sysParam.getParamTypeClass());
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_CLASS_UPDATE_FAIL);
	}

}
