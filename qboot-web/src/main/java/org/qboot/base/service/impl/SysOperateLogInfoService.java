package org.qboot.base.service.impl;

import org.qboot.base.dao.SysOperateLogInfoDao;
import org.qboot.base.dto.SysOperateLogInfoDto;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.RedisTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>Title: SysOperateLogInfoService</p>
 * <p>Description: opt log manage service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysOperateLogInfoService extends CrudService<SysOperateLogInfoDao, SysOperateLogInfoDto>{
	
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private RedisTools redisTools;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertOperationLogInfo(SysOperateLogInfoDto operationLogInfoDto) {
		try {
			this.d.insert(operationLogInfoDto);
		} catch (DuplicateKeyException e) {
			logger.info("'{}' has already existed!", operationLogInfoDto.getReqUri());
			findLogIdByUri(operationLogInfoDto.getReqUri());
		}
	}
	

	public Integer updateOperationLogInfo(SysOperateLogInfoDto operationLogInfoDto) {
		if (operationLogInfoDto == null) {
			return 0;
		}
		SysOperateLogInfoDto oldOperationLogInfoDto = this.d.findById(operationLogInfoDto.getLogId());
		if (oldOperationLogInfoDto == null) {
			return 0;
		}
		return this.d.update(operationLogInfoDto);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Long findLogIdByUri(String reqUri) {
        String key = CacheConstants.CACHE_PREFIX_SYS_OPTLOG_URL + reqUri;
		SysOperateLogInfoDto sol = redisTools.get(key);
		if(sol != null) {
			return sol.getLogId();
		}
		SysOperateLogInfoDto solid = new SysOperateLogInfoDto();
		solid.setReqUri(reqUri);
		List<SysOperateLogInfoDto> infoList = this.d.findByUri(solid);
		if(!CollectionUtils.isEmpty(infoList)) {
			solid = infoList.get(0);
		}else {
			solid.setUriName(reqUri);
			solid.setCreateDate(new Date());
			solid.setUriType(2);
			solid.setPhysicsFlag(1);
			solid.setCreateBy(SysConstants.SYSTEM_CRATER_NAME);
			this.d.insert(solid);
			solid = this.d.findByUri(solid).get(0);
		}
		redisTools.set(key, solid, 24*60*60);
		return solid.getLogId();
	}
	
	public boolean uriIsUsed(Long logId, String reqUri) {
		SysOperateLogInfoDto solid = new SysOperateLogInfoDto();
		solid.setReqUri(reqUri);
		List<SysOperateLogInfoDto> infoList = this.d.findByUri(solid);
		
		SysOperateLogInfoDto infoDto = null;
		if(!CollectionUtils.isEmpty(infoList)) {
			infoDto = infoList.get(0);
		}

		if(infoDto != null && logId.equals(infoDto.getLogId())) {
			return true;
		}
		if(infoDto == null && (logId == null || logId == 0L)) {
			return true;
		}
		return false;
	}
}
