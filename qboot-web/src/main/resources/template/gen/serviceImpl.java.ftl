package org.qboot.${functionName}.${moduleName}.service;

import org.qboot.common.facade.CrudService;
import org.qboot.${functionName}.${moduleName}.dao.${className}Dao;
import org.qboot.${functionName}.${moduleName}.dto.${className}Dto;
import org.springframework.stereotype.Service;

/**
 * ${menuName}ServiceImpl
 * @author systemGenerated ${.now}
 */
@Service
public class ${className}ServiceImpl extends CrudService<${className}Dao, ${className}Dto> implements ${className}Service {

}
