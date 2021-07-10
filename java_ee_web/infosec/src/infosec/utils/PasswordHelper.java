package infosec.utils;

import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;
import sun.security.rsa.RSASignature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class PasswordHelper {

	public static final String signatureWithSHA1_RSA(byte[] password) throws Exception {
		String base64Sign = "";
		// 签名
		Signature sign = Signature.getInstance("SHA1withRSA");
		sign.initSign(RSApem.getPrivateKeyFromPem(SystemConfig.CA_PRIVATE_KEY));
		byte[] bysData = password;
		sign.update(bysData);
		byte[] signByte = sign.sign();
		BASE64Encoder encoder = new BASE64Encoder();
		base64Sign = encoder.encode(signByte);
		return base64Sign;
	}

	public static final String getSaltedMD5Password(String password, Object salt){
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		return md5.encodePassword(password, salt);
	}
	

	public static final String getSaltedSHAPassword(String password, Object salt){
		ShaPasswordEncoder sha = new ShaPasswordEncoder();
		return sha.encodePassword(password, salt);
	}
	
	public static final String getBCryptPassword(String password){
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		return bCrypt.encode(password);
	}

	
}
