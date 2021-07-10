//数字证书数字签名（signature）、数字证书内容（服务端发送的公钥）（content）、CA公钥（CApublicKey）、指定的哈希算法（hash）
alert("ok")
console.log("222获取到信息",signature,CApublicKey)

//使用CA公钥解密
var decryptSignature = new JSEncrypt();
decryptSignature.setPrivateKey('-----BEGIN RSA CApublicKey KEY-----'+CApublicKey+'-----END RSA CApublicKey KEY-----');
//数字签名->（CA公钥CApublicKey解密）->摘要1
abstract1 = decryptSignature.decrypt(signature);
console.log('解密后数据:%o', abstract1);

//服务器公钥，即电子原文content
var content = ''


// var bcrypt = require('bcryptjs')
// // 【同步加密和验证】
// // 随机字符串
// var salt = bcrypt.genSaltSync(10)
// // 对明文加密
// var pwd1 = bcrypt.hashSync('123456', salt)
// // 验证比对,返回布尔值表示验证结果 true表示一致，false表示不一致
// var isOk = bcrypt.compareSync('12345678', pwd1)
// alert(isOk)
// console.log(isOk)