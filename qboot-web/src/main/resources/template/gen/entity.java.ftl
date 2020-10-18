package ${moduleName}.entity;

import org.qboot.common.entity.BaseEntity;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
<#if hasBigDecimal >
import java.math.BigDecimal;
</#if>
<#if hasDate >
import java.util.Date;
</#if>
/**
 * ${menuName}
 * @author systemGenerated ${.now}
 */
public class ${className}Dto extends BaseEntity<${pkType}> {

   private static final long serialVersionUID = 1L;
<#list columnInfos as columnInfo>
	<#if columnInfo.javaFieldName != "id" && columnInfo.javaFieldName != "createBy" && columnInfo.javaFieldName != "createDate" && columnInfo.javaFieldName != "updateBy" && columnInfo.javaFieldName != "updateDate" && columnInfo.javaFieldName != "remarks">
    /**
     * ${columnInfo.columnComment}
     */
    <#if columnInfo.nullable! != "1">
    @NotNull
    <#if columnInfo.javaType == "String">
    @Length(min = 1, max = ${columnInfo.maxLength?c!255})
    </#if>
    <#else>
    <#if columnInfo.javaType == "String">
    @Length(max = ${columnInfo.maxLength?c!255})
    </#if>
    </#if>
    private ${columnInfo.javaType} ${columnInfo.javaFieldName};
	</#if>
</#list>
<#list columnInfos as columnInfo>
	<#if columnInfo.javaFieldName != "id" && columnInfo.javaFieldName != "createBy" && columnInfo.javaFieldName != "createDate" && columnInfo.javaFieldName != "updateBy" && columnInfo.javaFieldName != "updateDate" && columnInfo.javaFieldName != "remarks">
    public ${columnInfo.javaType} get${columnInfo.javaFieldName ? cap_first}(){
        return ${columnInfo.javaFieldName};
    }
    public void set${columnInfo.javaFieldName ? cap_first}(${columnInfo.javaType} ${columnInfo.javaFieldName}){
        this.${columnInfo.javaFieldName} = ${columnInfo.javaFieldName};
    }
	</#if>
</#list>
}