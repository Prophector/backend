package ch.sebastianhaeni.prophector.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static String hex(byte[] array) {
        var sb = new StringBuilder();
        for (byte b : array) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString();
    }

    public static String md5Hex(String message) {
        try {
            return hex(MessageDigest.getInstance("MD5").digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignore) {
            // nop
        }
        return null;
    }
}
