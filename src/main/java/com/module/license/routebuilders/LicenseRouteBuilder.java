package com.module.license.routebuilders;

import com.module.license.processors.DecryptLicenseProcessor;
import com.module.license.processors.EncryptionKeyProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class LicenseRouteBuilder extends RouteBuilder{


        Logger logger = LoggerFactory.getLogger(LicenseRouteBuilder.class);

        private final EncryptionKeyProcessor encryptionKeyProcessor;

        private final DecryptLicenseProcessor decryptLicenseProcessor;

        public LicenseRouteBuilder(EncryptionKeyProcessor encryptionKeyProcessor, DecryptLicenseProcessor decryptLicenseProcessor) {
            this.encryptionKeyProcessor = encryptionKeyProcessor;
            this.decryptLicenseProcessor = decryptLicenseProcessor;
        }

        @Override
        public void configure() throws Exception {

            onException(Exception.class)
                    .handled(true)
                    .log("Error processing file: ${header.CamelFileName}")
                    .to("{{routes.license.error.location}}");

            from("{{file.location}}")
                    .choice()
                    .when(header("CamelFileNameOnly").startsWith("KEY-Bank"))
                    .process(exchange -> {
                        // Process the key synchronously
                        encryptionKeyProcessor.process(exchange);
                    })
                    .endChoice()
                    .when(header("CamelFileNameOnly").startsWith("Bank-GPG"))
//                    .process(exchange -> {
//                        // Access the encryption key after it has been processed
//                        String encryptionKey = exchange.getProperty("encryptionKey", String.class);
//                        exchange.getIn().setHeader("encryptionKey", encryptionKey);
//                        System.out.println("encryption key in route: " + encryptionKey);
//                    })
                    .process(decryptLicenseProcessor)
                    .otherwise()
                    .log("Unknown file type: ${header.CamelFileNameOnly}")
                    .end();

//            from("{{file.location}}")
//                    .choice()
//                    .when(header("CamelFileNameOnly").startsWith("KEY-Bank"))
//                    .process(exchange -> {
//                        // Process the key synchronously
//                        encryptionKeyProcessor.process(exchange);
//                    })
//                    .endChoice()
//                    .when(header("CamelFileNameOnly").startsWith("Bank-GPG"))
//                    .to("direct:decryptRoute")
//                    .otherwise()
//                    .log("Unknown file type: ${header.CamelFileNameOnly}")
//                    .end();
//
//            from("direct:decryptRoute")
//                    .log("Before decryption: ${header.CamelFileNameOnly}")
//                    .process(exchange -> {
//                        // Access the encryption key after it has been processed
//                        String encryptionKey = exchange.getProperty("encryptionKey", String.class);
//                        exchange.getIn().setHeader("encryptionKey", encryptionKey);
//                    })
//                    .delay(5000)
//                    .log("After delay: ${header.CamelFileNameOnly}")
//                    .process(decryptLicenseProcessor)
//                    .log("After decryption: ${header.CamelFileNameOnly}")
//                    .end();
        }
}
