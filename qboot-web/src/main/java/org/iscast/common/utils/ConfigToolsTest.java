package org.iscast.common.utils;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * 测试工具 工具类
 * 数据库的加密和解密的方法
 * 生成公钥私钥，私钥用来加密，公钥用来解密
 * @author history
 * @date 2019年3月29日
 */
public class ConfigToolsTest {

    public static void main(String[] args){
        try {
            encrypt();
            decrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//加密

public static void encrypt() throws Exception{
String privateKey ="MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAxVUbnWbfmuJa3GbcaF44Sy/6Psi3l+RBFno+Q1vjVro1Ce+aFguBgmcS2SO80c8XgffkaNtCFU7gtb3ED1ZASQIDAQABAkAZRLSsiyfG5YVNA77lfmsm9ZXNxFX+FTRgdHbUmUebKzNYE4GK1PBLBzy6tImaRmIkYzT6VSlu1j78ZsmfAIuhAiEA6BRRJTV62WMmvKLYlxZb5iaZs12b7hXF995YBgsiNd8CIQDZq/QQ3ucyQDYYKop3hcLcGgup63TnCJL7Ts1Gv1E+1wIhAOSFPPG6m2D7NOWCJGuBZLRXRflML4JchjbwBRTOWb1ZAiAkoZh4QlrcDQxNctNMm/kX+1YdKV/KBZpeqntHPakZjwIgX8WTJ2jIzTKTQwPbvO1OYnCjpCD6PCfBzZ7jn4ciNeU=";
String password= "123456";
System.out.println(ConfigTools.encrypt(privateKey,password));
}
//解密

public static void decrypt() throws Exception{
String publickey ="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMVVG51m35riWtxm3GheOEsv+j7It5fkQRZ6PkNb41a6NQnvmhYLgYJnEtkjvNHPF4H35GjbQhVO4LW9xA9WQEkCAwEAAQ==";
String password ="bdaNF4UO59n57CZ0TfXlRabcwx9RJhcNjwj79e/YbVQju7Nfo3/OfdeLfcaMbHzZ7803XX8h5m/r1slKXFHZeQ==";
String a = ConfigTools.decrypt(publickey, password);
System.out.println(a);
// return ConfigTools.decrypt(publickey, password);
}

}