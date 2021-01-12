package org.qboot.common.dao;

import org.qboot.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * crud persistence
 * @author iscast
 * @date 2020-09-25
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
