package com.bbsk.anything.security.serivce;

import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha512CustomPasswordEncoder implements PasswordEncoder {
    private final String ALGORITHM = "SHA-512";
    @Override
    public String encode(final CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        return encode(rawPassword.toString().getBytes());
    }
    private String encode(final byte[] rawPassword) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(rawPassword);
            final StringBuilder encodedPassword = new StringBuilder();
            for (final byte digest : messageDigest.digest()) {
                String hexStr = Integer.toHexString(digest & 0xff);
                while (hexStr.length() < 2) {
                    hexStr = "0" + hexStr;
                }
                encodedPassword.append(hexStr.substring(hexStr.length() - 2));
            }
            return encodedPassword.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("Invalid algorithm '" + ALGORITHM + "'.", ex);
        }
    }
    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return MessageDigest.isEqual(bytesUtf8(encode(rawPassword)), bytesUtf8(encodedPassword));
    }
    private byte[] bytesUtf8(final CharSequence s) {
        return s != null ? Utf8.encode(s) : null;
    }
}
