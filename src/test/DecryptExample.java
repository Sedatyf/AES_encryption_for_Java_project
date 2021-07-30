package test;

import main.PasswordManager;

import java.io.IOException;

// This file is meant to demonstrate how to decrypt an encrypted password so you can use this password
// to connect API in code without having plain text password
public class DecryptExample {
    public static void main(String[] args) throws IOException {
        String encryptedString = PasswordManager.getPassword();
        String decryptedString = PasswordManager.decrypt();

        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }
}
