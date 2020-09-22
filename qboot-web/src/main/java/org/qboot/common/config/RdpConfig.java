package org.qboot.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Title: RdpConfig</p>
 * <p>Description: rdp自动配置类</p>
 * 
 * @author history
 * @date 2018-10-19
 */
@ConfigurationProperties(prefix = "rdp")
public class RdpConfig {
	
	private String urlPrefix;
	
	private String storePath;
	
	public String getUrlPrefix() {
		return urlPrefix;
	}
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	public String getStorePath() {
		return storePath;
	}
	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

}
