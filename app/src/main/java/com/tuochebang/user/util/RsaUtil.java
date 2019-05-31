package com.tuochebang.user.util;

import android.util.Log;
import com.bumptech.glide.load.Key;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class RsaUtil {
    private static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static PublicKey getPublicKeyFromX509(String algorithm, String bysKey) throws NoSuchAlgorithmException, Exception {
        return KeyFactory.getInstance(algorithm).generatePublic(new X509EncodedKeySpec(Base64Util.decode(bysKey).getBytes()));
    }

    public static String encrypt(String content, String key) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, key);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, pubkey);
            return new String(Base64Util.encode(cipher.doFinal(content.getBytes(Key.STRING_CHARSET_NAME))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sign(String content, String privateKey) {
        String charset = Key.STRING_CHARSET_NAME;
        try {
            PrivateKey priKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Base64Util.decode(privateKey).getBytes()));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            return Base64Util.encode(signature.sign()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMD5(String content) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(content.getBytes());
            byte[] tmp = md.digest();
            char[] str = new char[32];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                int i2 = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = i2 + 1;
                str[i2] = hexDigits[byte0 & 15];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            PublicKey pubKey = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64Util.decode(publicKey).getBytes()));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            Log.i("Result", "content :   " + content);
            Log.i("Result", "sign:   " + sign);
            boolean bverify = signature.verify(Base64Util.decode(sign).getBytes());
            Log.i("Result", "bverify = " + bverify);
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
