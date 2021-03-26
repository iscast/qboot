package org.qboot.sys.dao;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysDeptDto;

import java.io.Serializable;
import java.util.List;

/**
 * 部门
 * @author iscast
 * @date 2020-09-25
 */
public interface SysDeptDao extends CrudDao<SysDeptDto> {
    SysDeptDto findById(Long id);

    int deleteRoleDeptByDeptId(Serializable deptId);

    // 部门名称唯一
    int checkNameUnique(SysDeptDto deptDto);

    // 根据id查询子部门id
    String getDeptIds(String id);

    List<SysDeptDto> relationQueryDept(SysDeptDto deptDto);
}