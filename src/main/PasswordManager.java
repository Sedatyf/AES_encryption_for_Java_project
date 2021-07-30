package main;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Properties;

public class PasswordManager {


    /**
     * Get password String from configuration.properties
     * @return return the encrypted string
     * @throws IOException File not found
     */
    public static String getPassword() throws IOException {
        InputStream inputConfiguration = new FileInputStream("src/resources/configuration.properties");
        Properties properties = new Properties();
        properties.load(inputConfiguration);

        return properties.getProperty("password");
    }


    /**
     * Get userSecretKey String from secretFile.properties
     * @return return the secret key
     * @throws IOException File not found
     */
    public static String getSecretKey() throws IOException {
        InputStream inputSecretKey = new FileInputStream("src/resources/secretFile.properties");
        Properties properties = new Properties();
        properties.load(inputSecretKey);

        return properties.getProperty("userSecretKey");
    }


    /**
     * Get salt String from secretFile.properties
     * @return return the salt
     * @throws IOException File not found
     */
    public static String getSalt() throws IOException {
        InputStream inputSecretKey = new FileInputStream("src/resources/secretFile.properties");
        Properties properties = new Properties();
        properties.load(inputSecretKey);

        return properties.getProperty("salt");
    }

    
    /**
     * Encrypt your password with SHA256 + secret key + salt
     * @param strToEncrypt your password to encrypt
     * @return your encrypted password
     */
    public static String encrypt(String strToEncrypt) {
        try {
            String SECRET_KEY = getSecretKey();
            String SALT = getSalt();

            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }


    /**
     * Decrypt an encrypted password with same salt and secret key as encrypt method
     * @return your decrypted password
     */
    public static String decrypt() {
        try {
            String PASSWORD = getPassword();
            String SECRET_KEY = getSecretKey();
            String SALT = getSalt();

            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

            return new String(cipher.doFinal(Base64.getDecoder().decode(PASSWORD)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


}
