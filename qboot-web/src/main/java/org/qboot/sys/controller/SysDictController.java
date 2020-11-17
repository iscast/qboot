package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dto.SysDictDto;
import org.qboot.sys.service.impl.SysDictService;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * <p>Title: SysDictController</p>
 * <p>Description: 系统字典</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/dict")
public class SysDictController extends BaseController {

	@Autowired
	private SysDictService sysDictService;

	@PreAuthorize("hasAuthority('sys:dict:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysDictDto sysDict, BindingResult bindingResult) {
		PageInfo<SysDictDto> page = sysDictService.findByPage(sysDict);
		if(null == page) {
            return ResponeModel.error(SYS_DICT_LIST_EMPTY);
        }
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:dict:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
        MyAssertTools.notNull(id, SYS_DICT_ID_NULL);
		SysDictDto sysDict = sysDictService.findById(id);
		if(null == sysDict) {
            return ResponeModel.error(SYS_DICT_NO_EXIST);
        }
		return ResponeModel.ok(sysDict);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dict:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysDictDto sysDict, BindingResult bindingResult) {
		if(null == sysDict.getSort()) {
            sysDict.setSort(0);
        }
        sysDict.setStatus(SysConstants.SYS_ENABLE);
        sysDict.setCreateBy(SecurityUtils.getLoginName());
		if(sysDictService.save(sysDict) > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_DICT_SAVE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysDictDto sysDict, BindingResult bindingResult) {
		sysDict.setUpdateBy(SecurityUtils.getLoginName());
		if(sysDictService.update(sysDict) > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_DICT_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PostMapping("/setStatus")
	public ResponeModel setStatus(@RequestParam String id, @RequestParam String status) {
        MyAssertTools.notNull(id, SYS_DICT_ID_NULL);
        SysDictDto sysDict = new SysDictDto();
		sysDict.setId(id);
		sysDict.setStatus(status);
		sysDict.setUpdateBy(SecurityUtils.getLoginName());
		sysDict.setUpdateDate(new Date());
		if(sysDictService.setStatus(sysDict) > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_DICT_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dict:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam String id) {
        MyAssertTools.notNull(id, SYS_DICT_ID_NULL);
        SysDictDto sysDict = new SysDictDto();
		sysDict.setId(id);
		sysDict.setStatus(SysConstants.SYS_DISABLE);
		sysDict.setUpdateBy(SecurityUtils.getLoginName());
		sysDict.setUpdateDate(new Date());
		if(sysDictService.setStatus(sysDict) > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_DICT_DELETE_FAIL);
	}

	@RequestMapping("/getDictType")
	public ResponeModel getDictType(@RequestParam String dictType) {
        MyAssertTools.notNull(dictType, SYS_DICT_TYPE_NULL);
		List<SysDictDto> dicts = sysDictService.findTypes(dictType);
		if(CollectionUtils.isEmpty(dicts)) {
            return ResponeModel.error(SYS_DICT_QUERY_FAIL);
        }
		return ResponeModel.ok(dicts);
	}
	
}
