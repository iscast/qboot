package org.iscast.common.facade;

import com.github.pagehelper.PageInfo;
import org.iscast.common.dao.CrudDao;
import org.iscast.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:14
 */
public interface ICrudService<D extends CrudDao<T>, T extends BaseEntity<? extends Serializable>> {
    int save(T t);

    int update(T t);

    int updateById(T t);

    T findById(Serializable id);

    int delete(T t);

    int deleteById(Serializable id);

    List<T> findList(T t);

    PageInfo<T> findByPage(T t, boolean count);

    PageInfo<T> findByPage(T t);
}

