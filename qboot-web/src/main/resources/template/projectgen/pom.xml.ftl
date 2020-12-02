<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<artifactId>${projectName}</artifactId>
 	<packaging>war</packaging>
 	<version>1.1.1</version>
	
	<dependencies>
		<dependency>
			<groupId>org.qboot</groupId>
			<artifactId>qboot-web</artifactId>
			<version>1.1.1</version>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

<#--		<dependency>-->
<#--			<groupId>org.springframework.boot</groupId>-->
<#--			<artifactId>spring-boot-configuration-processor</artifactId>-->
<#--			<optional>true</optional>-->
<#--		</dependency>-->
	</dependencies>
    <build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>  
		</plugins>
	</build>

</project>
