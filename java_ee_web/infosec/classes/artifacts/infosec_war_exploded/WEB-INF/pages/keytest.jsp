<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>使用jsencrypt执行OpenSSL的RSA加密，解密</title>
</head>
<!--引入jsencrypt.js-->
<script src="https://cdn.bootcss.com/jsencrypt/3.0.0-beta.1/jsencrypt.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jsrsasign/8.0.20/jsrsasign-all-min.js"></script>
<script type="text/javascript">

    // 公钥
    let pk="-----BEGIN PUBLIC KEY-----" +
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKSuh9VV//0S5+h3coHcCTVHuJ\n" +
        "sTHoPR5aFEWF3+GyDfWfQ49spzdZoV+YtJQ9DAW9T9ivRbI/xtmvsI6uc8Dmifrk\n" +
        "ZWfWAVy7xDWIr/5X5K/KoxPJZixT9oK0t18nTVFwzGSHlguo+hYauwcwS4r8Hoap\n" +
        "OEJ8A9F/viBnbxVCQQIDAQAB"+
        "-----END PUBLIC KEY-----"
    // 私钥
    let priK = "-----BEGIN PRIVATE KEY-----\n" +
        "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMpK6H1VX//RLn6H\n" +
        "dygdwJNUe4mxMeg9HloURYXf4bIN9Z9Dj2ynN1mhX5i0lD0MBb1P2K9Fsj/G2a+w\n" +
        "jq5zwOaJ+uRlZ9YBXLvENYiv/lfkr8qjE8lmLFP2grS3XydNUXDMZIeWC6j6Fhq7\n" +
        "BzBLivwehqk4QnwD0X++IGdvFUJBAgMBAAECgYArxbu66w9jxnq+DEf/WTjDaIN2\n" +
        "zu3Sp+76ZOqKRmCK67Rbav4M773tWhraazCsaP6Q8Ucc3gLtA/U0rAqjuw5hMVDR\n" +
        "FNAOflXK4Fd/BmOVCyihJt5idM1uyS7jW6IFaofq7WRx8z6Y1COPnKFC+0r961xN\n" +
        "UnvghsArU8mz+qrswQJBAPmPMuxtNZt2ex8nhZ92OXUksgvV7fcyr0yH392fD5je\n" +
        "YpBXM5K1MyGnC87vBmqiZTel/TujgHuWpdvYPqg/jlkCQQDPg2zFqaymBCATcB6m\n" +
        "jbmwvHarpiz483oMpELDBAEqZbTn9SZh4sw7B1DrCBYCz7EEaEAgxrcsumhMqeYV\n" +
        "sGYpAkAq+t2jWF+uKaakw/LAduUIZsx/O1XjiAgm3xPQIG/YPRyujUYlky1+Mz3W\n" +
        "YtYxEUzrW5wUZGFhZ52jEkggQ+npAkAQyBwf0Bz6QBMn/0U7SvLdbCBcuU+hwm74\n" +
        "XsBXbRd7CkabC6RS/jht/aV5nGnLSGiUZqno/KkLoPeWW5cT4fQhAkBfPKsj+R5x\n" +
        "eIcSqVFNB3sN6q32la0C3I3ixgisiGa60/wt5HwKFfC+QTSznrd+2xdP6kDmrQB3\n" +
        "RDXBeK6msNKk\n" +
        "-----END PRIVATE KEY-----"

    var getpk='${CApublicKey}'
    let backPk="-----BEGIN PUBLIC KEY-----" +getpk+
        "-----END PUBLIC KEY-----"
    console.log("标准公钥"+pk)
    console.log("后端CA公钥"+backPk)

    // 原文
    var src = '${content}'
    let getSignature='${signature}'

    // 方式1: 先建立 key 对象, 构建 signature 实例, 传入 key 初始化 -> 签名
    var getpr='${CAprivateKey}'
    var backpr="-----BEGIN PRIVATE KEY-----" +getpr+
        "-----END PRIVATE KEY-----"
    console.log("后端CA私钥"+backpr)

    var key = KEYUTIL.getKey(backpr);
    console.log(key);
    // 创建 Signature 对象
    let signature=new KJUR.crypto.Signature({alg:"SHA1withRSA"});
    // 传入key实例, 初始化signature实例
    signature.init(key);
    // 传入待签明文
    signature.updateString(src);
    // 签名, 得到16进制字符结果
    let a = signature.sign();
    let sign = hextob64(a);

    console.log("前端签名"+sign);
    console.log(getSignature);

    // 验签
    // !要重新new 一个Signature, 否则, 取摘要和签名时取得摘要不一样, 导致验签误报失败(原因不明)!
    let signatureVf = new KJUR.crypto.Signature({alg:"SHA1withRSA",prvkeypem:backPk});
    signatureVf.updateString(src);
    // !接受的参数是16进制字符串!
    let b = signatureVf.verify(b64tohex(getSignature));
    console.log("jsrsasign verify: "+b);
</script>
<body>
<h1>登陆成功！</h1>
<br>
<a href=/index.jsp" />">退出</a>
</body>
</html>