package org.qboot.common.config;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.qboot.common.constants.SysConstants;
import org.qboot.sys.exception.SysGenException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class FreeMarkerResolver {

	public static final String DEFAULT_DIRECTORY = "template";
	private static Configuration cfg = buildConfiguration(DEFAULT_DIRECTORY);

	public static Configuration buildConfiguration(String directory) {
		// 1.创建配置实例Cofiguration
		Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		cfg.setDefaultEncoding("utf-8");
		ClassPathResource cps = new ClassPathResource(directory);
		try {
			cfg.setDirectoryForTemplateLoading(cps.getFile());
			return cfg;
		} catch (IOException e) {
            throw new SysGenException(SysConstants.GLOBAL_DEFAULT_ERROR, e);
		}
	}

	/**
	 * 
	 * @param name
	 *            相对路径下的模板
	 * @param data
	 *            数据
	 * @param out
	 *            输出流
	 */
	public static void renderTemplate(String name, Object data, Writer out) {
		try {
			Template template = cfg.getTemplate(name);
			template.process(data, out);
		} catch (Exception e) {
            throw new SysGenException(SysConstants.GLOBAL_DEFAULT_ERROR, e);
		}
	}
	/**
	 * 获取渲染后的文本
	 * @param name
	 *            相对路径下的模板
	 * @param data
	 *            数据
	 * @param out
	 *            输出流
	 */
	public static String renderTemplate(String name, Object data) {
		String result = null;
		try {
			Template template = cfg.getTemplate(name);
			StringWriter out = new StringWriter();
			template.process(data, out);
			result = out.toString();
			out.close();
			return result;
		} catch (Exception e) {
            throw new SysGenException(SysConstants.GLOBAL_DEFAULT_ERROR, e);
		}
	}
	/**
	 * 模板内容
	 * @param name
	 *            相对路径下的模板
	 */
	public static String readTemplate(String name) {
		try {
			Template template = cfg.getTemplate(name);
			return template.toString();
		} catch (Exception e) {
            throw new SysGenException(SysConstants.GLOBAL_DEFAULT_ERROR, e);
		}
	}
	
	/**
	 * 字符串模板
	 * @param templateString
	 * @param data
	 * @return
	 */
	public static String renderStringTemplate(String templateString, Object data) {
		Template template;
		String result = null;
		try {
			template = new Template(null, new StringReader(templateString), cfg);
			StringWriter sw = new StringWriter();
			template.process(data, sw);
			result = sw.toString();
			sw.close();
		} catch (Exception e) {
            throw new SysGenException(SysConstants.GLOBAL_DEFAULT_ERROR, e);
		}
		return result;
	}
}
