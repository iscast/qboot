package org.iscast.common.dao;

import org.iscast.common.entity.BaseEntity;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 18:16
 */
public interface CrudDao <T extends BaseEntity<?>> extends BaseDao {
    int insert(T t);

    int update(T t);

    int updateById(T t);

    int delete(T t);

    int deleteById(java.io.Serializable id);

    T findById(java.io.Serializable id);

    java.util.List<T> findList(T t);
}
