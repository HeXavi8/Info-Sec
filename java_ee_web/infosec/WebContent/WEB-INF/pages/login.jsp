<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jsrsasign/8.0.20/jsrsasign-all-min.js"></script>

    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!--CSP是网页安全政策(Content Security Policy)的缩写。主要用来防止XSS攻击。是一种由开发者定义的安全性政策申明，
    通过CSP所约束的责任指定可信的内容来源，通过 Content-Security-Policy 网页的开发者可以控制整个页面中 外部资源 的加载和执行。
    比如可以控制哪些 域名下的静态资源可以被页面加载，哪些不能被加载。
    这样就可以很大程度的防范了 来自 跨站(域名不同) 的脚本攻击。-->
    <meta http-equiv="Content-Security-Policy" content="
default-src http: https:  *.xxx.com 'self' 'unsafe-inline' ;
style-src 'self' 'unsafe-inline' *.yyy.com;
script-src 'self' 'unsafe-inline' 'unsafe-eval' ;
">

    <title>InfoSec: 登录</title>
</head>
<body>
<h1 id="title1">登陆</h1>
<br>
<!--获取后端发来的内容-->
<script type="text/javascript">
    var getpk='${CApublicKey}'
    let backPk="-----BEGIN PUBLIC KEY-----" +getpk+
        "-----END PUBLIC KEY-----"
    console.log("后端CA公钥"+backPk)
    // 原文
    var src = '${content}'
    let getSignature='${signature}'
    // 验签
    // !要重新new 一个Signature, 否则, 取摘要和签名时取得摘要不一样, 导致验签误报失败(原因不明)!
    let signatureVf = new KJUR.crypto.Signature({alg:"SHA1withRSA",prvkeypem:backPk});
    signatureVf.updateString(src);
    // !接受的参数是16进制字符串!
    let b = signatureVf.verify(b64tohex(getSignature));
    console.log("jsrsasign verify: "+b);

    if(b==false){
        alert("数字签名验证失败！！！返回上一页")
        //返回上一页
        window.history.go(-1);
    }else{
        //继续执行
        alert("数字签名验证成功！！！继续执行")
    }
</script>
<!--XSS防范-->
<script type="text/javascript">
    // XSS防御HTML编码,使用正则表达式实现html编码
    function htmlEncodeByRegExp(str) {
        var s = '';
        if (str.length === 0) {
            console.log("XSS防御HTML编码输入为空")
            return s;
        }
        console.log("XSS防御HTML编码输入,使用正则表达式对html编码")
        return (s + str)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/ /g, "&nbsp;")
            .replace(/\'/g, "&#39")
            .replace(/\"/g, "&quot;")
            .replace(/\//g, '&#x2F;');
    };

    //XSS防御javascript编码
    function encodeForJavascript(str) {
        let encoded = '';
        for(let i = 0; i < str.length; i++) {
            let cc = hex = str[i];
            if (!/[A-Za-z0-9]/.test(str[i]) && str.charCodeAt(i) < 256) {
                hex = '\\x' + cc.charCodeAt().toString(16);
            }
            encoded += hex;
        }
        console.log("XSS防御javascript编码")
        return encoded;
    };
</script>


<input id="username" type="text" required="required" placeholder="用户" name="username" />
<input id="password" type="password" required="required" placeholder="密码" name="password" />
<button id="button1" type="submit" onclick="test()">登录</button>

<script>

    function test() {
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        console.log("加密前",username, password)

        //服务器公钥
        var publicKey = "-----BEGIN PUBLIC KEY-----" +'${content}'+ "-----END PUBLIC KEY-----";

        console.log("服务器公钥",publicKey)

        // 加密enc_username
        let pub_username = KEYUTIL.getKey(publicKey);
        let enc_username = KJUR.crypto.Cipher.encrypt(username, pub_username);
        //console.log(`公钥加密username结果：${enc_username}`);

        // 加密enc_password
        let pub_password = KEYUTIL.getKey(publicKey);
        let enc_password = KJUR.crypto.Cipher.encrypt(password, pub_password);
        //console.log(`公钥加密password结果：${enc_password}`);

        console.log("username:");username = htmlEncodeByRegExp(username);
        console.log("password:");password = htmlEncodeByRegExp(password);

        console.log("username:");username = encodeForJavascript(username);
        console.log("password:");password = encodeForJavascript(password);

        //做成json格式post给后端
        var obj = {
            'username':username,
            'password':password
        };

        var obj1 = {
            'username':enc_username,
            'password':pub_password
        };

        console.log("加密后的obj：",obj1)

        var httpRequest = new XMLHttpRequest(); //第一步：创建需要的对象
        httpRequest.open('POST', 'http://localhost:8080/infosec/login', true); //第二步：打开连接/***发送json格式文件必须设置请求头 ；如下 - */
        httpRequest.setRequestHeader("Content-type",
            "application/json"); //设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）
        httpRequest.send(JSON.stringify(obj)); //发送请求 将json写入send中
        /**
         * 获取数据后的处理程序*/
        httpRequest.onreadystatechange = function () { //请求后的回调接口，可将请求成功后要执行的程序写在其中
            if (httpRequest.readyState == 4 && httpRequest.status == 200) { //验证请求是否发送成功
                var json = httpRequest.responseText; //获取到服务端返回的数据
                //console.log(json);//返回json就是http源码

                //解析一下http源码并用innerHTML显示到页面上
                var target=document.getElementById("show");
                target.innerHTML=json;

                //隐藏登录界面，set为none
                var title1 =document.getElementById("title1");
                title1.style.display="none";
                var username =document.getElementById("username");
                username.style.display="none";
                var password =document.getElementById("password");
                password.style.display="none";
                var button1 =document.getElementById("button1");
                button1.style.display="none";

            }
        };
    }


</script>

</script>
<div id="show"></div>
</body>
</html>