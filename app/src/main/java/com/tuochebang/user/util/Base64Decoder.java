package com.tuochebang.user.util;

import com.bumptech.glide.load.Key;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Base64Decoder extends FilterInputStream {
    private static final char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final int[] ints = new int[128];
    private int carryOver;
    private int charCount;

    static {
        for (int i = 0; i < 64; i++) {
            ints[chars[i]] = i;
        }
    }

    private int decoded;

    public Base64Decoder(InputStream in) {
        super(in);
    }

    public int read() throws IOException {
        int x;
        do {
            x = this.in.read();
            if (x == -1) {
                return -1;
            }
        } while (Character.isWhitespace((char) x));
        this.charCount++;
        if (x == 61) {
            return -1;
        }
        x = ints[x];
        int mode = (this.charCount - 1) % 4;
        if (mode == 0) {
            this.carryOver = x & 63;
            return read();
        } else if (mode == 1) {
            decoded = ((this.carryOver << 2) + (x >> 4)) & 255;
            this.carryOver = x & 15;
            return decoded;
        } else if (mode == 2) {
            decoded = ((this.carryOver << 4) + (x >> 2)) & 255;
            this.carryOver = x & 3;
            return decoded;
        } else if (mode == 3) {
            return ((this.carryOver << 6) + x) & 255;
        } else {
            return -1;
        }
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        if (buf.length < (len + off) - 1) {
            throw new IOException("The input buffer is too small: " + len + " bytes requested starting at offset " + off + " while the buffer  is only " + buf.length + " bytes long.");
        }
        int i = 0;
        while (i < len) {
            int x = read();
            if (x == -1 && i == 0) {
                return -1;
            }
            if (x == -1) {
                return i;
            }
            buf[off + i] = (byte) x;
            i++;
        }
        return i;
    }

    public static String decode(String encoded) {
        return new String(decodeToBytes(encoded));
    }

    public static byte[] decodeToBytes(String encoded) {
        byte[] bytes = null;
        try {
            bytes = encoded.getBytes(Key.STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
        }
        Base64Decoder in = new Base64Decoder(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) (((double) bytes.length) * 0.67d));
        try {
            byte[] buf = new byte[4096];
            while (true) {
                int bytesRead = in.read(buf);
                if (bytesRead != -1) {
                    out.write(buf, 0, bytesRead);
                } else {
                    out.close();
                    return out.toByteArray();
                }
            }
        } catch (IOException e2) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Throwable th;
        if (args.length != 1) {
            System.err.println("Usage: java Base64Decoder fileToDecode");
            return;
        }
        Base64Decoder decoder = null;
        try {
            Base64Decoder decoder2 = new Base64Decoder(new BufferedInputStream(new FileInputStream(args[0])));
            try {
                byte[] buf = new byte[4096];
                while (true) {
                    int bytesRead = decoder2.read(buf);
                    if (bytesRead == -1) {
                        break;
                    }
                    System.out.write(buf, 0, bytesRead);
                }
                if (decoder2 != null) {
                    decoder2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                decoder = decoder2;
                if (decoder != null) {
                    decoder.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (decoder != null) {
                decoder.close();
            }
        }
    }
}
