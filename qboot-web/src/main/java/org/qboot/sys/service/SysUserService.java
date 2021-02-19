package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysUserDao;
import org.qboot.sys.dto.SysUserDto;

/**
 * 系统用户service
 */
public interface SysUserService extends ICrudService<SysUserDao, SysUserDto> {

	SysUserDto findByLoginName(String loginName);
	
	boolean checkLoginName(Long userId, String loginName);

	int save(SysUserDto t);
	
	int update(SysUserDto t);

    int updateById(SysUserDto t);
	
	int deleteById(Long id);

	boolean validatePwd(String password, Long userId);
	
	int initPwd(SysUserDto t, int initFlag, String ip);
	
	int setStatus(SysUserDto t);
	
	boolean selectFirstLoginUser(Long userId);

	SysUserDto findSecretInfo(SysUserDto qry);
}
