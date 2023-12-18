package com.module.license.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Component
public class EncryptionKeyProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        log.info("Reading Encrypted Key");


        // Read the contents of the KEY-Bank file
        String keyFilePath = exchange.getIn().getHeader(Exchange.FILE_PATH, String.class);

        String encryptionKey = new String(Files.readAllBytes(Paths.get(keyFilePath)));
        System.out.println("encryptionKey data: " + encryptionKey);

        // Store the encryption key in exchange properties for later use
        exchange.setProperty("encryptionKey", encryptionKey);
        System.out.println("encryptionKey data in exchange: " + encryptionKey);


        // Example: Print a message after reading the key
        System.out.println("Encryption key read successfully.");
        log.info("Encryption key read successfully.");


    }
}
