package infosec.utils;

import infosec.model.DigitalCertification;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSAUtils {

    public static String data="hello world";
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    private static RSAUtils instance;

    //构造器私有化
    private RSAUtils() throws Exception {
        KeyPair keyPair=genKeyPair(1024);

        publicKey=keyPair.getPublic();
        privateKey=keyPair.getPrivate();
    }

    //取得单例实例，懒汉式启动，线程安全
    public static synchronized RSAUtils getInstance() throws Exception {
        if(instance==null){
            instance=new RSAUtils();
        }
        return instance;
    }
    @Test
    public void testRSA() throws Exception {
        // TODO Auto-generated method stub

        KeyPair keyPair=genKeyPair(1024);

        //获取公钥，并以base64格式打印出来
        PublicKey publicKey=keyPair.getPublic();
        System.out.println("公钥："+new String(Base64.getEncoder().encode(publicKey.getEncoded())));

        //获取私钥，并以base64格式打印出来
        PrivateKey privateKey=keyPair.getPrivate();
        System.out.println("私钥："+new String(Base64.getEncoder().encode(privateKey.getEncoded())));

        //公钥加密
        byte[] encryptedBytes=encrypt(data.getBytes());
        System.out.println("加密后："+new String(encryptedBytes));

        //私钥解密
        byte[] decryptedBytes=decrypt(encryptedBytes);
        System.out.println("解密后："+new String(decryptedBytes));
    }

    //生成密钥对
    public static KeyPair genKeyPair(int keyLength) throws Exception{
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    //公钥加密
    public  byte[] encrypt(byte[] content) throws Exception{
        Cipher cipher=Cipher.getInstance("RSA");//java默认"RSA"="RSA/ECB/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    //私钥解密
    public  byte[] decrypt(byte[] content) throws Exception{
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    //取得公钥
    public PublicKey getPublicKey(){
        return publicKey;
    }


}
