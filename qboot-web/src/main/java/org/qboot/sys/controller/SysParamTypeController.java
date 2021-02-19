package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.common.utils.RedisTools;
import org.qboot.common.utils.ValidateUtils;
import org.qboot.sys.dto.SysParamTypeDto;
import org.qboot.sys.service.SysParamTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

@RestController
@RequestMapping("${admin.path}/sys/paramtype")
public class SysParamTypeController extends BaseController {

	@Autowired
	private SysParamTypeService sysParamService;
    @Autowired
    RedisTools redisTools;

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysParamTypeDto sysParam) {
		if(StringUtils.isBlank(sysParam.getParamTypeClass())) {
            return ResponeModel.error(SYS_PARAM_TYPE_QUERY_FAIL);
		}
		PageInfo<SysParamTypeDto> page = sysParamService.findByPage(sysParam);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryList")
	public ResponeModel qryList(SysParamTypeDto sysParam) {
		if(StringUtils.isBlank(sysParam.getParamTypeClass())) {
			return ResponeModel.ok(SYS_PARAM_TYPE_QUERY_FAIL);
		}
		List<SysParamTypeDto> list = sysParamService.findList(sysParam);
		return ResponeModel.ok(list);
	}
	
	@PreAuthorize("hasAuthority('sys:param:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Long id) {
		SysParamTypeDto sysParam = sysParamService.findById(id);
		if(null == sysParam) {
            return ResponeModel.ok(SYS_PARAM_TYPE_QUERY_FAIL);
        }
		return ResponeModel.ok(sysParam);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysParamTypeDto sysParam, BindingResult bindingResult, HttpServletRequest request) {
        ValidateUtils.checkBind(bindingResult);
		sysParam.setCreateBy(SecurityUtils.getLoginName());
		sysParam.setPhysicsFlag(SysConstants.SYS_DELFLAG_NORMAL);
		if(sysParamService.save(sysParam) > 0) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + sysParam.getParamTypeClass());
			return ok();
		}
		return ResponeModel.error(SYS_PARAM_TYPE_SAVE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysParamTypeDto sysParam, BindingResult bindingResult, HttpServletRequest request) {
        ValidateUtils.checkBind(bindingResult);
		sysParam.setUpdateBy(SecurityUtils.getLoginName());
		if(sysParamService.update(sysParam) > 0) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + sysParam.getParamTypeClass());
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_TYPE_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Long id, @RequestParam String paramTypeClass) {
        MyAssertTools.notNull(id, SYS_PARAM_TYPE_ID_NULL);
		if(sysParamService.deleteById(id) > 0 && StringUtils.isNotBlank(paramTypeClass)) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + paramTypeClass);
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_TYPE_DELETE_FAIL);
	}

	@RequestMapping("/getParamType")
	public ResponeModel getParamType(@RequestParam String paramType) {
		List<SysParamTypeDto> types = sysParamService.findParamTypes(paramType);
		if(CollectionUtils.isEmpty(types)) {
            return ResponeModel.error(SYS_PARAM_TYPE_QUERY_FAIL);
        }
		return ResponeModel.ok(types);
	}
}
