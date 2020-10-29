package org.qboot.common.dao;

import org.qboot.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * crud persistence
 * @Author: iscast
 * @Date: 2020/8/22 18:16
 */
public interface CrudDao <T extends BaseEntity<?>> extends BaseDao {
    int insert(T t);

    int update(T t);

    int updateById(T t);

    int delete(T t);

    int deleteById(Serializable id);

    T findById(Serializable id);

    List<T> findList(T t);
}
