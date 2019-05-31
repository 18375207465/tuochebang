package com.tuochebang.user.util;

import cn.jiguang.api.utils.ByteBufferUtils;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static AESUtil instance = new AESUtil();
    private final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";
    private final int HASH_ITERATIONS = ByteBufferUtils.ERROR_CODE;
    private IvParameterSpec IV;
    private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";
    private final int KEY_LENGTH = 256;
    private char[] humanPassphrase = new char[]{'c', '3', 'I', '!', '#', 'l', '8', '7', '2', 'F', 'M', '4', 'M', 's', 'C', 'b', 'x'};
    private byte[] iv = new byte[]{(byte) 15, (byte) 11, (byte) 9, (byte) 0, (byte) 12, (byte) 2, (byte) 13, (byte) 15, (byte) 1, (byte) 9, (byte) 8, (byte) 10, Byte.MAX_VALUE, (byte) 107, (byte) 14, (byte) 42};
    private SecretKeyFactory keyfactory = null;
    private PBEKeySpec myKeyspec = new PBEKeySpec(this.humanPassphrase, this.salt, ByteBufferUtils.ERROR_CODE, 256);
    private byte[] salt = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15};
    private SecretKey sk = null;
    private SecretKeySpec skforAES = null;

    public static synchronized AESUtil getInstance() {
        AESUtil aESUtil;
        synchronized (AESUtil.class) {
            if (instance == null) {
                instance = new AESUtil();
            }
            aESUtil = instance;
        }
        return aESUtil;
    }

    private AESUtil() {
        try {
            this.keyfactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            this.sk = this.keyfactory.generateSecret(this.myKeyspec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("no key factory support for PBEWITHSHAANDTWOFISH-CBC");
        } catch (InvalidKeySpecException e2) {
            System.out.println("invalid key spec for PBEWITHSHAANDTWOFISH-CBC");
        }
        this.skforAES = new SecretKeySpec(this.sk.getEncoded(), "AES");
        this.IV = new IvParameterSpec(this.iv);
    }

    public String encrypt(byte[] plaintext) {
        return Base64Encoder.encode(encrypt("AES/CBC/PKCS5Padding", this.skforAES, this.IV, plaintext));
    }

    public String decrypt(String ciphertext_base64) {
        return new String(decrypt("AES/CBC/PKCS5Padding", this.skforAES, this.IV, Base64Decoder.decodeToBytes(ciphertext_base64)));
    }

    private byte[] addPadding(byte[] plain) {
        int i;
        int shortage = 16 - (plain.length % 16);
        if (shortage == 0) {
            shortage = 16;
        }
        byte[] plainpad = new byte[(plain.length + shortage)];
        for (i = 0; i < plain.length; i++) {
            plainpad[i] = plain[i];
        }
        for (i = plain.length; i < plain.length + shortage; i++) {
            plainpad[i] = (byte) shortage;
        }
        return plainpad;
    }

    private byte[] dropPadding(byte[] plainpad) {
        byte[] plain = new byte[(plainpad.length - plainpad[plainpad.length - 1])];
        for (int i = 0; i < plain.length; i++) {
            plain[i] = plainpad[i];
            plainpad[i] = (byte) 0;
        }
        return plain;
    }

    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(1, sk, IV);
            return c.doFinal(msg);
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            System.out.println("no cipher getinstance support for " + cmp);
            return null;
        } catch (NoSuchPaddingException e) {
            System.out.println("no cipher getinstance support for padding " + cmp);
            return null;
        } catch (InvalidKeyException e2) {
            System.out.println("invalid key exception");
            return null;
        } catch (InvalidAlgorithmParameterException e3) {
            System.out.println("invalid algorithm parameter exception");
            return null;
        } catch (IllegalBlockSizeException e4) {
            System.out.println("illegal block size exception");
            return null;
        } catch (BadPaddingException e5) {
            System.out.println("bad padding exception");
            return null;
        }
    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(2, sk, IV);
            return c.doFinal(ciphertext);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("no cipher getinstance support for " + cmp);
            return null;
        } catch (NoSuchPaddingException e2) {
            System.out.println("no cipher getinstance support for padding " + cmp);
            return null;
        } catch (InvalidKeyException e3) {
            System.out.println("invalid key exception");
            return null;
        } catch (InvalidAlgorithmParameterException e4) {
            System.out.println("invalid algorithm parameter exception");
            return null;
        } catch (IllegalBlockSizeException e5) {
            System.out.println("illegal block size exception");
            return null;
        } catch (BadPaddingException e6) {
            System.out.println("bad padding exception");
            e6.printStackTrace();
            return null;
        }
    }
}
