package ${moduleName}.service;

import org.qboot.common.facade.CrudService;
import ${moduleName}.dao.${className}Dao;
import ${moduleName}.entity.${className}Dto;
import org.springframework.stereotype.Service;

/**
 * ${menuName}ServiceImpl
 * @author systemGenerated ${.now}
 */
@Service
public class ${className}ServiceImpl extends CrudService<${className}Dao, ${className}Dto> implements ${className}Service {

}
