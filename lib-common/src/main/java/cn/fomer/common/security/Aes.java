package cn.fomer.common.security;




import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
* 2019-05-15 AES.CBC.PKCS5
* 
* AES 是一种可逆加密算法，对用户的敏感信息加密处理
* 对原始数据进行AES加密后，在进行Base64编码转化；
* 正确
*/
public class Aes 
{
	/*已确认
	* 加密用的Key 可以用26个字母和数字组成
	* 此处使用AES-128-CBC加密模式，key需要为16位。
	*/
    final static String FORMATE="AES/CBC/PKCS5Padding";
    
    // 加密
    public static String encrypt(String text, String key, String iv) throws Exception 
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(text.getBytes("utf-8"));
        String base64= new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
        return base64;
    }

    // 解密
    public static String decrypt(String text, String sKey, String iv) throws Exception 
    {
        try 
        {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(text);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String src = new String(original,"utf-8");
            return src;
        } catch (Exception ex) {
            return null;
        }
}

    public static void main(String[] args) throws Exception {
        // 需要加密的字串
        String cSrc = "中国人1";
        System.out.println("加密前的字串是："+cSrc);
        // 加密
        String enString = Aes.encrypt(cSrc,ParamVO.key,ParamVO.iv);
        System.out.println("加密后的字串是："+ enString);
        
        System.out.println("IO/GVa3OFJSFISY0f+cF4EV3OH69V8DjKLRi+3fk/3M=".equals(enString));
        
        // 解密
        String DeString = Aes.decrypt(enString,ParamVO.key,ParamVO.iv);
        System.out.println("解密后的字串是：" + DeString);
    }
}