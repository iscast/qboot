<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="org.qboot.${functionName}.${moduleName}.dao.${className}Dao" >
  <resultMap id="BaseResultMap" type="org.qboot.${functionName}.${moduleName}.dto.${className}Dto" >
<#list columnInfos as columnInfo>
    <${(columnInfo.columnKey == "PRI") ? string("id","result")} column="${columnInfo.dbColumnName}" property="${columnInfo.javaFieldName}" jdbcType="${columnInfo.jdbcType}" />
</#list>
  </resultMap>
  
  <sql id="Base_Column_List" >
  <#assign notFirst = false>
  <#list columnInfos as columnInfo><#if columnInfo.jdbcType !='LONGVARCHAR'><#if notFirst>,</#if>${columnInfo.dbColumnName}<#assign notFirst = true></#if></#list>
  </sql>
  <#if hasBlob>
  <sql id="Blob_Column_List" >
  <#assign notFirst = false>
  <#list columnInfos as columnInfo><#if columnInfo.jdbcType =='LONGVARCHAR'><#if notFirst>,</#if>${columnInfo.dbColumnName}<#assign notFirst = true></#if></#list>
  </sql>
  </#if>
  <select id="findById" resultMap="BaseResultMap" parameterType="${pkType}" >
    select 
    <include refid="Base_Column_List" />
    <#if hasBlob>
    ,<include refid="Blob_Column_List" />
    </#if>
    from ${tableName}
    <#list columnInfos as columnInfo>
      <#if columnInfo.columnKey =="PRI">
         where ${columnInfo.javaFieldName} = ${"#"}{${columnInfo.dbColumnName}}
      </#if>
   </#list>
  </select>
  <select id="findList" resultMap="BaseResultMap" parameterType="org.qboot.${functionName}.${moduleName}.dto.${className}Dto" >
    select 
    <include refid="Base_Column_List" />
    from ${tableName}
<#if hasSearch>
    <where>
<#list columnInfos as columnInfo>
	<#if columnInfo.search! ="1">
    <if test="${(columnInfo.javaType == "String")?string("${columnInfo.javaFieldName} !=null and ${columnInfo.javaFieldName}!=''","${columnInfo.javaFieldName} !=null")}">
    		<#if columnInfo.queryType! == "=">
    and ${columnInfo.dbColumnName} = ${"#"}{${columnInfo.javaFieldName}}
        <#elseif columnInfo.queryType! == "!=">
    and ${columnInfo.dbColumnName} != ${"#"}{${columnInfo.javaFieldName}}
    		<#elseif columnInfo.queryType! == ">=">
    and ${columnInfo.dbColumnName} >= ${"#"}{${columnInfo.javaFieldName}}
    		<#elseif columnInfo.queryType! == ">">
    and ${columnInfo.dbColumnName} > ${"#"}{${columnInfo.javaFieldName}}
    	    <#elseif columnInfo.queryType! == "<=">
    and <![CDATA[  ${columnInfo.dbColumnName} <= ${"#"}{${columnInfo.javaFieldName}} ]]>
    		<#elseif columnInfo.queryType! == "<">
    and <![CDATA[  ${columnInfo.dbColumnName} < ${"#"}{${columnInfo.javaFieldName}} ]]>
    		<#elseif columnInfo.queryType! == "between">
    and ${columnInfo.dbColumnName} between ${"#"}{${columnInfo.javaFieldName}} and ${"#"}{${columnInfo.javaFieldName}}
    		<#elseif columnInfo.queryType! == "like">
    and ${columnInfo.dbColumnName} like concat("%",${"#"}{${columnInfo.javaFieldName}},"%")
    		<#elseif columnInfo.queryType! == "left like">
    and ${columnInfo.dbColumnName} like concat("%",${"#"}{${columnInfo.javaFieldName}})
    		<#elseif columnInfo.queryType! == "right like">
    and ${columnInfo.dbColumnName} like concat(${"#"}{${columnInfo.javaFieldName}},"%")
    		<#else>
    	and ${columnInfo.dbColumnName} = ${"#"}{${columnInfo.javaFieldName}}
    		</#if>
    </if>
	</#if>
</#list>
    </where>
</#if>
	<choose>
    <when test="sortField != null and sortField != '' and direction != null and direction !=''">
    order by ${r'#{sortField} #{direction}'}
    </when>
    <otherwise>
   <!-- 默认排序设置 -->
    </otherwise>
    </choose>
  </select>
  <delete id="deleteById" >
    delete from ${tableName}
    <#list columnInfos as columnInfo>
      <#if columnInfo.columnKey =="PRI">
         where ${columnInfo.javaFieldName} = ${"#"}{${columnInfo.dbColumnName}}
      </#if>
   </#list>
  </delete>
  <#assign notFirst = false>
  <insert id="insert" parameterType="org.qboot.${functionName}.${moduleName}.dto.${className}Dto"  useGeneratedKeys="true" keyProperty="id">
    insert into ${tableName} (<#list columnInfos as columnInfo><#if columnInfo.insert! ="1"><#if notFirst>,</#if>${columnInfo.dbColumnName}<#assign notFirst = true></#if></#list>)
    <#assign notFirst = false>
    values (<#list columnInfos as columnInfo><#if columnInfo.insert! ="1"><#if notFirst>,</#if>${"#"}{${columnInfo.javaFieldName}}<#assign notFirst = true></#if></#list>)
  </insert>
  <update id="updateById" parameterType="org.qboot.${functionName}.${moduleName}.dto.${className}Dto" >
    update ${tableName}
    <set>
      <#assign notFirst = false>
      <#list columnInfos as columnInfo>
        <#if columnInfo.update! ="1">
          <if test="${(columnInfo.javaType == "String")?string("${columnInfo.javaFieldName} !=null and ${columnInfo.javaFieldName}!=''","${columnInfo.javaFieldName} !=null")}">
            <#if notFirst>,</#if>${columnInfo.dbColumnName} = ${"#"}{${columnInfo.javaFieldName}}<#assign notFirst = true>
          </if>
        </#if>
      </#list>
    </set>
    <#list columnInfos as columnInfo>
      <#if columnInfo.columnKey =="PRI">
        where ${columnInfo.javaFieldName} = ${"#"}{${columnInfo.dbColumnName}}
      </#if>
    </#list>

  </update>
  <update id="update" parameterType="org.qboot.${functionName}.${moduleName}.dto.${className}Dto" >
    update ${tableName}
    <set> 
    	<#assign notFirst = false>
      <#list columnInfos as columnInfo>
      <#if columnInfo.update! ="1">
         <if test="${(columnInfo.javaType == "String")?string("${columnInfo.javaFieldName} !=null and ${columnInfo.javaFieldName}!=''","${columnInfo.javaFieldName} !=null")}">
         	<#if notFirst>,</#if>${columnInfo.dbColumnName} = ${"#"}{${columnInfo.javaFieldName}}<#assign notFirst = true>
         </if>
      </#if>
      </#list>
    </set>
    <#list columnInfos as columnInfo>
      <#if columnInfo.columnKey =="PRI">
         where ${columnInfo.javaFieldName} = ${"#"}{${columnInfo.dbColumnName}}
      </#if>
   </#list>
    
  </update>
</mapper>