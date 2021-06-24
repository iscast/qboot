package org.qboot.sys.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.CodecUtils;
import org.qboot.common.utils.IdGen;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.common.utils.PwdUtils;
import org.qboot.sys.dao.SysUserDao;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.dto.SysUserRoleDto;
import org.qboot.sys.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysUserErrTable.*;

/**
 * 系统用户service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysUserServiceImpl extends CrudService<SysUserDao, SysUserDto> implements SysUserService {

    @Override
	public SysUserDto findByLoginName(String loginName) {
        MyAssertTools.hasLength(loginName, SYS_USER_LOGINNAME_EMPTY);
        SysUserDto qry = new SysUserDto();
        qry.setLoginName(loginName);
        return this.d.findByDto(qry);
	}

    @Override
    public SysUserDto findByMobile(String mobile) {
        MyAssertTools.hasLength(mobile, SYS_USER_MOBILE_EMPTY);
        SysUserDto qry = new SysUserDto();
        qry.setMobile(mobile);
        return this.d.findByDto(qry);
    }

    @Override
    public SysUserDto findByDto(SysUserDto dto) {
        return this.d.findByDto(dto);
    }

    @Override
    public List<String> findUserIds(SysUserDto dto) {
        return this.d.findUserIds(dto);
    }

    @Override
	public boolean checkLoginName(String userId, String loginName) {
        MyAssertTools.hasLength(loginName, SYS_USER_LOGINNAME_EMPTY);
        SysUserDto qry = new SysUserDto();
        qry.setLoginName(loginName);
        SysUserDto exesitUser = this.d.findByDto(qry);

        if(exesitUser == null) {
            return false;
        }else if(userId != null && exesitUser.getId().equals(userId)) {
            return false;
        }
        return true;
	}

	@Override
	public int save(SysUserDto t) {
		String salt = RandomStringUtils.randomAlphanumeric(20);
        String userId = IdGen.uuid();
        t.setId(userId);
		t.setSalt(salt);
		t.setPassword(PwdUtils.encrypt(t.getPassword(), salt));
		if(this.d.insert(t) > 0) {
			return this.saveUserRole(t.getRoleIds(), userId);
		}
        return 0;
	}
	
	@Override
	public int updateInfo(SysUserDto t) {
		SysUserDto sysUser = this.findById(t.getId());
        MyAssertTools.notNull(sysUser, SYS_USER_NOTEXISTS);
		return super.update(t);
	}


    @Override
    public int update(SysUserDto t) {
		int cnt = this.d.update(t);
		if(cnt > 0) {
			this.d.deleteUserRoleByUserId(t.getId());
			this.saveUserRole(t.getRoleIds(), t.getId());
		}
		return cnt;
	}

    @Override
	public int deleteById(String id) {
		this.d.deleteUserRoleByUserId(id);
		return super.deleteById(id);
	}

    @Override
	public boolean validatePwd(String password, String userId) {
        MyAssertTools.hasLength(password, SYS_USER_PWD_EMPTY);
        MyAssertTools.notNull(userId, SYS_USER_UERID_EMPTY);
        SysUserDto qry = new SysUserDto();
        qry.setId(userId);
        SysUserDto secretInfo = d.findSecretInfo(qry);
        MyAssertTools.notNull(secretInfo, SYS_USER_NOTEXISTS);
		return secretInfo.getPassword().equals(PwdUtils.encrypt(password, secretInfo.getSalt()));
	}

    @Override
	public int changePwd(SysUserDto secretInfo) {
        String salt = RandomStringUtils.randomAlphanumeric(20);
        secretInfo.setSalt(salt);
        secretInfo.setPassword(PwdUtils.encrypt(secretInfo.getPassword(), salt));
		return d.changePwd(secretInfo);
	}

    @Override
	public int setStatus(SysUserDto t) {
        MyAssertTools.notNull(t, SYS_USER_NOTEXISTS);
		return d.setStatus(t);
	}

    @Override
	public boolean selectFirstLoginUser(String userId){
		SysUserDto sysUser = this.findById(userId);
        return null != sysUser && SysConstants.SYS_USER_STATUS_INIT.equals(sysUser.getStatus());
	}

    @Override
	public SysUserDto findSecretInfo(SysUserDto qry){
        MyAssertTools.notNull(qry, SYS_USER_NOTEXISTS);
        return d.findSecretInfo(qry);
    }

    private int saveUserRole(List<String> roleIds, String userId) {
        if (null == roleIds || roleIds.isEmpty()) {
            return 0;
        }
        List<SysUserRoleDto> userRoleList =  new ArrayList<>();
        for (String roleId : roleIds) {
            userRoleList.add(new SysUserRoleDto(String.valueOf(userId), roleId));
        }
        return this.d.insertUserRole(userRoleList);
    }
}
