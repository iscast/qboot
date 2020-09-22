package org.qboot.common.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.qboot.common.dao.CrudDao;
import org.qboot.common.entity.BaseEntity;
import org.qboot.common.facade.ICrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 21:52
 */
@Transactional(rollbackFor = {Exception.class})
public class CrudService<D extends CrudDao<T>, T extends BaseEntity<? extends Serializable>> extends BaseService implements ICrudService<D, T> {
    @Autowired
    protected D d;

    public CrudService() {
    }

    public int save(T t) {
        t.setCreateDate(new Date());
        t.setUpdateDate(t.getCreateDate());
        int cnt = this.d.insert(t);
        return cnt;
    }

    public int update(T t) {
        Assert.notNull(t, "更新对象为空");
        t.setUpdateDate(new Date());
        int cnt = this.d.update(t);
        return cnt;
    }

    public int updateById(T t) {
        Assert.notNull(t, "更新对象为空");
        Assert.notNull(t.getId(), "更新对象id为空");
        Assert.hasLength(t.getId().toString(), "更新对象id为空");
        t.setUpdateDate(new Date());
        int cnt = this.d.updateById(t);
        return cnt;
    }

    @Transactional(readOnly = true)
    public T findById(Serializable id) {
        Assert.notNull(id, "id为空");
        Assert.hasLength(id.toString(), "id为空");
        return this.d.findById(id);
    }

    public int delete(T t) {
        Assert.notNull(t, "删除对象为空");
        if (null != t) {
            t.setUpdateDate(new Date());
            return this.d.delete(t);
        } else {
            return 0;
        }
    }

    public int deleteById(Serializable id) {
        Assert.notNull(id, "id为空");
        int cnt = this.d.deleteById(id);
        return cnt;
    }

    @Transactional(readOnly = true)
    public List<T> findList(T t) {
        return this.d.findList(t);
    }

    @Transactional(readOnly = true)
    public PageInfo<T> findByPage(T t, boolean count) {
        PageHelper.startPage(t.getPage(), t.getLimit(), count);
        List<T> list = this.findList(t);
        PageInfo<T> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Transactional(readOnly = true)
    public PageInfo<T> findByPage(T t) {
        return this.findByPage(t, Boolean.TRUE);
    }
}

