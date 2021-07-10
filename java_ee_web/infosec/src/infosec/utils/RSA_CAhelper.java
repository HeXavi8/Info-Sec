package infosec.utils;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.*;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA 非对称加密算法
 */
public class RSA_CAhelper
{
    /**
     * 指定加密算法为RSA
     */
    private static final String ALGORITHM = "RSA";
    /**
     * 密钥长度，用来初始化
     */
    private static final int KEY_SIZE = 1024;

    /**
     * 指定CA公钥存放文件
     */
    private static  String CA_PUBLIC_KEY;
    static {
        try {
            CA_PUBLIC_KEY = SystemConfig.setAndGetProjectPath() + "/CApublic.pem";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 指定CA私钥存放文件
     */
    private static  String CA_PRIVATE_KEY;
    static {
        try {
            CA_PRIVATE_KEY = SystemConfig.setAndGetProjectPath() + "/CAprivate.pem";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成密钥对
     */
    public static void generateKeyPair() throws Exception
    {

        /**
         * 为RSA算法创建一个KeyPairGenerator对象
         */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);

        /**
         * 利用上面的随机数据源初始化这个KeyPairGenerator对象
         */
        keyPairGenerator.initialize(KEY_SIZE);

        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        /** 得到公钥 */
        PublicKey publicKey = keyPair.getPublic();

        /** 得到私钥 */
        PrivateKey privateKey = keyPair.getPrivate();

        ObjectOutputStream oos1 = null;
        ObjectOutputStream oos2 = null;
        try
        {
            System.out.println("生成密钥对，并写入相应文件中");

            File file = new File(CA_PUBLIC_KEY);
            if (file.exists())
            {
                System.out.println("公钥和私钥已经生成，不需要重复生成，path:"+CA_PUBLIC_KEY);
                return;
            }

            /** 用对象流将生成的密钥写入文件 */
            System.out.println("PUBLIC_KEY_FILE 写入："+CA_PUBLIC_KEY);
            oos1 = new ObjectOutputStream(new FileOutputStream(CA_PUBLIC_KEY));

            System.out.println("PRIVATE_KEY_FILE 写入："+CA_PRIVATE_KEY);
            oos2 = new ObjectOutputStream(new FileOutputStream(CA_PRIVATE_KEY));

            oos1.writeObject(publicKey);
            oos2.writeObject(privateKey);
        } catch (Exception e)
        {
            throw e;
        } finally
        {
            /** 清空缓存，关闭文件输出流 */
            if(oos1!=null)oos1.close();
            if(oos2!=null)oos2.close();
        }
    }

    /**
     * 加密方法
     *
     * @param plain 明文数据
     * @return
     */
    public static byte[] encrypt(String plain) throws Exception
    {
        // generateKeyPair();

        //从文件加载公钥
        Key privateky = loadPrivateKey();

        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateky);
        byte[] b = plain.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return b1;
        //BASE64Encoder encoder = new BASE64Encoder();
        //return encoder.encode(b1);
    }

    /**
     * 从文件加载公钥
     */
    public static PublicKey loadPublicKey() throws Exception
    {
        PublicKey publicKey = null;
        ObjectInputStream ois = null;
        try
        {
            System.out.println("PUBLIC_KEY_FILE 读取："+CA_PUBLIC_KEY);
            /** 读出文件中的公钥 */
            ois = new ObjectInputStream(new FileInputStream(CA_PUBLIC_KEY));
            publicKey = (PublicKey) ois.readObject();
        } catch (Exception e)
        {
            throw e;
        } finally
        {
            ois.close();
        }
        return publicKey;
    }

    /**
     * 解密算法
     *
     * @param crypto 密文
     */
    public static String decrypt(String crypto) throws Exception
    {
        Key publickey = loadPublicKey();

        /** 得到Cipher对象对已用私钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publickey);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(crypto);

        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 从文件加载私钥
     *
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey() throws Exception
    {
        PrivateKey privateKey;
        ObjectInputStream ois = null;
        try
        {
            System.out.println("PRIVATE_KEY_FILE 读取："+CA_PRIVATE_KEY);

            /** 读出文件中的私钥 */
            ois = new ObjectInputStream(new FileInputStream(CA_PRIVATE_KEY));
            privateKey = (PrivateKey) ois.readObject();
        } catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        } finally
        {
            ois.close();
        }
        return privateKey;
    }

}