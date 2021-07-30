package test;

import main.PasswordManager;

import java.io.IOException;

// Use this file to encrypt your password and put the encrypted password in configuration.properties
public class EncryptExample {
    public static void main(String[] args) throws IOException {
        String toEncryptString = "encryptionPassword";
        String encryptedString = PasswordManager.encrypt(toEncryptString);

        System.out.println(toEncryptString);
        System.out.println(encryptedString);

    }
}
