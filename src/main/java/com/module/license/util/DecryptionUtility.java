package com.module.license.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DecryptionUtility {

        public static byte[] decryptLicense(byte[] encryptedData, String key) {
            try {
                // The decryption logic goes here
                // Use the provided encryptedData and key to decrypt the license file

                // Example implementation:
                // return SomeDecryptionUtility.decrypt(encryptedData, key);

                // Ensure to handle exceptions and return null in case of decryption failure

                // Decode Base64
                byte[] decodedData = Base64.getDecoder().decode(encryptedData);

                // Extract IV (Initialization Vector) from the first 16 bytes
                byte[] ivBytes = new byte[16];
                System.arraycopy(decodedData, 0, ivBytes, 0, 16);
                IvParameterSpec iv = new IvParameterSpec(ivBytes);

                // Extract encrypted content (excluding IV)
                byte[] encryptedContent = new byte[decodedData.length - 16];
                System.arraycopy(decodedData, 16, encryptedContent, 0, encryptedContent.length);
                System.out.println("decodedData length: " + decodedData.length);


                // Create SecretKeySpec from the key
                //SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
                if (key != null) {
                    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");

                    // Initialize Cipher for decryption
                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

                    // Decrypt the content.
                    return cipher.doFinal(encryptedContent);

                } else {
                   throw new NullPointerException("Key is empty!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Handle decryption failure
                throw new RuntimeException("Failed to decrypt license file", e);
            }

        }
    }
