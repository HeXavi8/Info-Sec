package infosec.model;

import infosec.utils.*;
import org.junit.Test;
import java.security.PublicKey;
import java.util.Base64;

public class DigitalCertification {
    //数字证书的密文
    private static String content;
    //数字签名
    private static String signature;
    //CA的公钥
    private static String CApublicKey;
    //数字证书实例
    private static DigitalCertification instance;

    //构造器私有化
    private DigitalCertification() throws Exception {
        //赋值服务器公钥
        content=new String(Base64.getEncoder().encode(RSApem.getPublicKeyFromPem(SystemConfig.PUBLIC_KEY).getEncoded()));
        //生成摘要并加密成为签名
        byte[] serpbBytes=RSApem.getPublicKeyFromPem(SystemConfig.PUBLIC_KEY).getEncoded();
        String serpbString=new String(Base64.getEncoder().encode(serpbBytes));
        String abstractWithHuanhang=PasswordHelper.signatureWithSHA1_RSA(serpbString.getBytes());
        signature=abstractWithHuanhang.replaceAll("\r|\n","");
        //赋值CA公钥
        CApublicKey=new String(Base64.getEncoder().encode(RSApem.getPublicKeyFromPem(SystemConfig.CA_PUBLIC_KEY).getEncoded()));
    }


    //取得单例实例，懒汉式启动，线程安全
    public static synchronized DigitalCertification getInstance() throws Exception {
        if(instance==null){
            instance=new DigitalCertification();
        }
        return instance;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCApublicKey() {
        return CApublicKey;
    }

    public void setCApublicKey(String CApublicKey) {
        this.CApublicKey = CApublicKey;
    }


}
