package org.qboot.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 21:52
 */
@Transactional(rollbackFor = {Exception.class})
public class BaseService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    public BaseService() {
    }
}
