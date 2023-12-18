package com.module.license.processors;

import com.module.license.util.DecryptionUtility;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;


@Slf4j
@Component
public class DecryptLicenseProcessor implements Processor {

//    private String encryptionKey;

    @Override
    public void process(Exchange exchange) throws Exception {

        log.info("Decryption Processor");

        // Extract necessary data from the exchange
        byte[] encryptedData = exchange.getIn().getBody(byte[].class);


        // Retrieve the encryption key from the exchange properties
        String encryptionKey = exchange.getProperty("encryptionKey", String.class);
//        this.encryptionKey = exchange.getProperty("encryptionKey", String.class);
//        Map<String, Object> encryptionKey = exchange.getProperties();
        System.out.println("encryption key in exchange: " + encryptionKey);
//      String encryption = (String) encryptionKey.getOrDefault("encryptionKey", null );

        log.info("Encryption Key: {}", encryptionKey);

        byte[] decryptedData = DecryptionUtility.decryptLicense(encryptedData, encryptionKey);

        // Example: Print a message after decryption
        System.out.println("License file decrypted successfully.");
        log.info("License file decrypted successfully.");

        // Set the decrypted data as the new body of the exchange
        exchange.getIn().setBody(decryptedData);
    }
}
