package com.se2020.course.registration.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
    public static String hashPassword(String rawPassword){
        String ret = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(rawPassword.getBytes());
            byte[] digested = md5.digest();
            ret = DatatypeConverter.printHexBinary(digested);

        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return ret;
    }
}
