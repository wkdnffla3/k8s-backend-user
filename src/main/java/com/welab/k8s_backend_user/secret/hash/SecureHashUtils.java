package com.welab.k8s_backend_user.secret.hash;

public class SecureHashUtils {
    public static String hash(String message){
        return message;
    }

    public static boolean matches(String message, String hashedMessage){
        String hashed = hash(message);

        return hashed.equals(hashedMessage);
    }
}
