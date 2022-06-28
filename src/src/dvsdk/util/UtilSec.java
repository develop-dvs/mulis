package dvsdk.util;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author develop-dvs
 */
public class UtilSec {
    /**
     * String to Base64
     *//*
    @Deprecated
    public static String base64Encode(String input) {
    sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    return encoder.encode(input.getBytes());
    }*/
    
    /**
     * String (UTF-8) to Base64
     */
    public static String base64Encode(String input, String encoding) {
        try {
            String ret = new String(input.getBytes(Charset.defaultCharset()), encoding);
            return base64Encode(ret); 
        }
        catch (Exception ex) {
            return input;
        }
    }
    /**
     * String (UTF-8) to Base64
     */
    public static String base64Encode(String input) {
        try {
            Base64 encoder = new Base64();
            byte[] result = encoder.encode(input.getBytes("UTF-8"));
            return new String(result, "UTF-8");
        } catch (Exception ex) {
            Logger.put(ex);
            return "";
        }
    }
    
    /**
     * Сгенерировать MD5
     * @param str
     * @return 
     */
    public static String getMD5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes(), 0, str.length());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (Exception ex) {
            Logger.put(ex);
            return "error";
        }
    }
}
