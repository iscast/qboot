package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysUserDao;
import org.qboot.sys.dto.SysUserDto;

import java.util.List;

/**
 * 系统用户service
 */
public interface SysUserService extends ICrudService<SysUserDao, SysUserDto> {

	SysUserDto findByLoginName(String loginName);

    SysUserDto findByMobile(String mobile);

    SysUserDto findByDto(SysUserDto dto);

    List<String> findUserIds(SysUserDto dto);

	boolean checkLoginName(String userId, String loginName);

	int save(SysUserDto t);
	
	int updateInfo(SysUserDto t);
	
	int deleteById(String id);

	boolean validatePwd(String password, String userId);
	
	int changePwd(SysUserDto t);
	
	int setStatus(SysUserDto t);
	
	boolean selectFirstLoginUser(String userId);

	SysUserDto findSecretInfo(SysUserDto qry);
}
