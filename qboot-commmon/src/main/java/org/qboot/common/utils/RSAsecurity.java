package org.qboot.common.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * RSA 工具类
 * 单例模式
 * 提供：秘钥对生成、秘钥加密、公钥解密
 * 注意：加密内容长度不能超过117字节
 */
public class RSAsecurity {
	
	private static RSAsecurity security = new RSAsecurity();
	
	private RSAsecurity(){
		super();
	}
	public static RSAsecurity getInstance(){
		return security;
	}
	private static KeyPairGenerator keyPairGenerator;
	{
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(1024);  // 注意：1024位 / 8位/字节 = 128字节 - 11字节 = 117 字节，加密内容长度不能 117字节
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成秘钥对
	 * @return 公钥 & 私钥
	 */
	public Tuple2<String, String>  generateKeyPair(){
		// 初始化秘钥对
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// 公钥
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		// 私钥
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
		
		return Tuples.of(new String(Base64.encodeBase64(rsaPublicKey.getEncoded())), new String(Base64.encodeBase64(rsaPrivateKey.getEncoded())));
	}
	
	/**
	 * 公钥加密
	 * @param rsaPublicKey 公钥
	 * @param src 源字符串
	 * @return 密文
	 */
	public String encrypt(String rsaPublicKey,String src){
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(rsaPublicKey));
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			// 初始化加密
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 加密字符串
			byte[] result = cipher.doFinal(src.getBytes());
			return new String(Base64.encodeBase64(result));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 私钥解密
	 * @param rsaPublicKey 私钥
	 * @param ciphertext 密文
	 * @return 明文
	 */
	public String decrypt(String rsaPrivateKey,String ciphertext){
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(rsaPrivateKey));
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			// 初始化解密
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 解密字符串
			byte[] result = cipher.doFinal(Base64.decodeBase64(ciphertext));
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static void main(String[] args) {
		Tuple2<String, String> keyPair = RSAsecurity.getInstance().generateKeyPair();
		System.out.println("公钥：" + keyPair.getT1());
		System.out.println("私钥：" + keyPair.getT2());
		
		String src = "!@#$%^&*()AaBbCcsdfsdfsdfs34134134145sda!@FWEFWED";
		
		String ciphertext = RSAsecurity.getInstance().encrypt(keyPair.getT1(), src);
		System.out.println(src+" 密文："+ciphertext);
		String result = RSAsecurity.getInstance().decrypt(keyPair.getT2(), ciphertext);
		System.out.println(src+" 明文："+result);
	}
}