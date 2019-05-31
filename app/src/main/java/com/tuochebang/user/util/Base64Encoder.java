package com.tuochebang.user.util;

import com.bumptech.glide.load.Key;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Base64Encoder extends FilterOutputStream {
    private static final char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private int carryOver;
    private int charCount;

    public Base64Encoder(OutputStream out) {
        super(out);
    }

    public void write(int b) throws IOException {
        if (b < 0) {
            b += 256;
        }
        int lookup;
        if (this.charCount % 3 == 0) {
            lookup = b >> 2;
            this.carryOver = b & 3;
            this.out.write(chars[lookup]);
        } else if (this.charCount % 3 == 1) {
            lookup = ((this.carryOver << 4) + (b >> 4)) & 63;
            this.carryOver = b & 15;
            this.out.write(chars[lookup]);
        } else if (this.charCount % 3 == 2) {
            this.out.write(chars[((this.carryOver << 2) + (b >> 6)) & 63]);
            this.out.write(chars[b & 63]);
            this.carryOver = 0;
        }
        this.charCount++;
        if (this.charCount % 57 == 0) {
            this.out.write(10);
        }
    }

    public void write(byte[] buf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(buf[off + i]);
        }
    }

    public void close() throws IOException {
        if (this.charCount % 3 == 1) {
            this.out.write(chars[(this.carryOver << 4) & 63]);
            this.out.write(61);
            this.out.write(61);
        } else if (this.charCount % 3 == 2) {
            this.out.write(chars[(this.carryOver << 2) & 63]);
            this.out.write(61);
        }
        super.close();
    }

    public static String encode(String unencoded) {
        byte[] bytes = null;
        try {
            bytes = unencoded.getBytes(Key.STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
        }
        return encode(bytes);
    }

    public static String encode(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) (((double) bytes.length) * 1.37d));
        Base64Encoder encodedOut = new Base64Encoder(out);
        try {
            encodedOut.write(bytes);
            encodedOut.close();
            return out.toString(Key.STRING_CHARSET_NAME);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Throwable th;
        if (args.length != 1) {
            System.err.println("Usage: java com.oreilly.servlet.Base64Encoder fileToEncode");
            return;
        }
        Base64Encoder encoder = null;
        BufferedInputStream in = null;
        try {
            BufferedInputStream in2;
            Base64Encoder encoder2 = new Base64Encoder(System.out);
            try {
                in2 = new BufferedInputStream(new FileInputStream(args[0]));
            } catch (Throwable th2) {
                th = th2;
                encoder = encoder2;
                if (in != null) {
                    in.close();
                }
                if (encoder != null) {
                    encoder.close();
                }
                throw th;
            }
            try {
                byte[] buf = new byte[4096];
                while (true) {
                    int bytesRead = in2.read(buf);
                    if (bytesRead == -1) {
                        break;
                    }
                    encoder2.write(buf, 0, bytesRead);
                }
                if (in2 != null) {
                    in2.close();
                }
                if (encoder2 != null) {
                    encoder2.close();
                }
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                encoder = encoder2;
                if (in != null) {
                    in.close();
                }
                if (encoder != null) {
                    encoder.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            if (in != null) {
                in.close();
            }
            if (encoder != null) {
                encoder.close();
            }
        }
    }
}
