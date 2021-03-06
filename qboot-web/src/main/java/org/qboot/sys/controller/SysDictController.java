package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
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
import org.qboot.sys.dto.SysDictDto;
import org.qboot.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
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
 * 系统字典
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@RequestMapping("${admin.path}/sys/dict")
public class SysDictController extends BaseController {

	@Autowired
	private SysDictService sysDictService;
    @Autowired
    private RedisTools redisTools;

	@PreAuthorize("hasAuthority('sys:dict:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysDictDto sysDict) {
		PageInfo<SysDictDto> page = sysDictService.findByPage(sysDict);
		if(null == page) {
            return ResponeModel.error(SYS_DICT_LIST_EMPTY);
        }
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:dict:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam String id) {
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
        ValidateUtils.checkBind(bindingResult);
        if(null == sysDict.getSort()) {
            sysDict.setSort(0);
        }

        if(StringUtils.isNotBlank(sysDict.getCode())) {
            sysDict.setCode(StringEscapeUtils.unescapeHtml4(sysDict.getCode()));
        }
        if(StringUtils.isNotBlank(sysDict.getName())) {
            sysDict.setName(StringEscapeUtils.unescapeHtml4(sysDict.getName()));
        }
        if(StringUtils.isNotBlank(sysDict.getRemarks())) {
            sysDict.setRemarks(StringEscapeUtils.unescapeHtml4(sysDict.getRemarks()));
        }
        sysDict.setId(IdGen.uuid());
        sysDict.setStatus(SysConstants.SYS_ENABLE);
        sysDict.setCreateBy(SecurityUtils.getLoginName());
		if(sysDictService.save(sysDict) > 0) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE + sysDict.getType());
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE_SINGLE + sysDict.getType());
			return ok();
		}
		return ResponeModel.error(SYS_DICT_SAVE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysDictDto sysDict, BindingResult bindingResult) {
        ValidateUtils.checkBind(bindingResult);

        if(StringUtils.isNotBlank(sysDict.getCode())) {
            sysDict.setCode(StringEscapeUtils.unescapeHtml4(sysDict.getCode()));
        }
        if(StringUtils.isNotBlank(sysDict.getName())) {
            sysDict.setName(StringEscapeUtils.unescapeHtml4(sysDict.getName()));
        }
        if(StringUtils.isNotBlank(sysDict.getRemarks())) {
            sysDict.setRemarks(StringEscapeUtils.unescapeHtml4(sysDict.getRemarks()));
        }

		sysDict.setUpdateBy(SecurityUtils.getLoginName());
		if(sysDictService.update(sysDict) > 0) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE + sysDict.getType());
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE_SINGLE + sysDict.getType());
			return ok();
		}
		return ResponeModel.error(SYS_DICT_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PostMapping("/editStatus")
	public ResponeModel editStatus(@RequestParam String id, @RequestParam String status) {
        MyAssertTools.notNull(id, SYS_DICT_ID_NULL);
        SysDictDto sysDict = new SysDictDto();
		sysDict.setId(id);
		sysDict.setStatus(status);
		sysDict.setUpdateBy(SecurityUtils.getLoginName());
		sysDict.setUpdateDate(new Date());
		if(sysDictService.editStatus(sysDict) > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_DICT_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dict:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam String id) {
        MyAssertTools.hasLength(id, SYS_DICT_ID_NULL);
		if(sysDictService.deleteById(id) > 0) {
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
