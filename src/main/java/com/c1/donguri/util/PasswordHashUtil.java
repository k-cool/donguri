package com.c1.donguri.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt: Spring Security provided password hashing library
 * - Securely hash passwords with automatic salt generation
 * - Industry standard for password storage security
 */
public class PasswordHashUtil {
    
    // BCrypt encoder: 기본 강도 10
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    /**
     * 평문 비밀번호 → 해시로 변환
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return encoder.encode(plainPassword);
    }
    
    /**
     * 비밀번호 일치 여부 확인
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return encoder.matches(plainPassword, hashedPassword);
    }
    
    /**
     * 비밀번호 재해시 필요 여부 확인
     */
    public static boolean needsRehash(String hashedPassword) {
        return encoder.upgradeEncoding(hashedPassword);
    }
}
