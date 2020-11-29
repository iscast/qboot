package org.qboot.common.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.qboot.common.dao.CrudDao;
import org.qboot.common.entity.BaseEntity;
import org.qboot.common.facade.ICrudService;
import org.qboot.common.utils.MyAssertTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static org.qboot.common.exception.errorcode.SystemErrTable.*;

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
        MyAssertTools.notNull(t, SYS_SAVE_POJO_NULL_ERROR);
        t.setCreateDate(new Date());
        t.setUpdateDate(t.getCreateDate());
        int cnt = this.d.insert(t);
        return cnt;
    }

    public int update(T t) {
        MyAssertTools.notNull(t, SYS_UPDATE_POJO_NULL_ERROR);
        t.setUpdateDate(new Date());
        int cnt = this.d.update(t);
        return cnt;
    }

    public int updateById(T t) {
        MyAssertTools.notNull(t, SYS_UPDATE_POJO_NULL_ERROR);
        MyAssertTools.notNull(t.getId(), SYS_PARAM_POJO_ID_NULL_ERROR);
        t.setUpdateDate(new Date());
        int cnt = this.d.updateById(t);
        return cnt;
    }

    @Transactional(readOnly = true)
    public T findById(Serializable id) {
        MyAssertTools.notNull(id, SYS_PARAM_ID_NULL_ERROR);
        return this.d.findById(id);
    }

    public int delete(T t) {
        MyAssertTools.notNull(t, SYS_DELETE_POJO_NULL_ERROR);
        if (null != t) {
            t.setUpdateDate(new Date());
            return this.d.delete(t);
        } else {
            return 0;
        }
    }

    public int deleteById(Serializable id) {
        MyAssertTools.notNull(id, SYS_DELETE_POJO_NULL_ERROR);
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

