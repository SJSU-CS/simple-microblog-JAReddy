package edu.sjsu.cmpe272.simpleblog.client;


import edu.sjsu.cmpe272.simpleblog.common.entity.Message;
import edu.sjsu.cmpe272.simpleblog.common.request.UserRequest;
import edu.sjsu.cmpe272.simpleblog.common.response.UserSuccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyPair;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.regex.Pattern;


@SpringBootApplication
@Command
@Slf4j
public class ClientApplication implements CommandLineRunner, ExitCodeGenerator {

    @Autowired
    CommandLine.IFactory iFactory;

    @Autowired
    private ConfigurableApplicationContext context;

    @Value("${serverUrl}")
    private String serverUrl;

    @Autowired
    private Util util;

    @Command(name = "post", description = "Post a message")
    public int post(@Parameters String message, @Parameters(defaultValue = "null") File attachment) {
        if (attachment != null) {
            try {
                final String uri = serverUrl + "/messages/create/";
                UserKey userKey =  util.getUserKey();
                if (userKey == null) {
                    return -1;
                }

                byte[] fileBytes = Files.readAllBytes(attachment.toPath());
                String encodedAttachment = Base64.getEncoder().encodeToString(fileBytes);

                Message request = new Message();
                request.setDate(LocalDateTime.now());
                request.setAuthor(userKey.getUserId());
                request.setMessage(message);
                request.setAttachment(encodedAttachment);
                request.setSignature(util.signMessageRequest(request, userKey));

                RestTemplate restTemplate = new RestTemplate();
                Message response = restTemplate.postForObject(uri, request, Message.class);

                if(response == null || response.getMessageId() == null) {
                    log.error("Error while posting the message to the server");
                    return -1;
                } else {
                    log.info("Message with Id {} is saved to database", response.getMessageId());
                }
            } catch (Exception e) {
                log.error("Error while posting message {}", e.getMessage());
                return -1;
            }
        }
        return 1;
    }

    @Command(name = "create", description = "Create a user")
    int create(@Parameters String id) {
        try {
            final String uri = serverUrl + "/user/create";
            // Generate Key Pair
            KeyPair keyPair = util.generateKeyPair();
            String regex = "^[a-z0-9]+$";
            if (!Pattern.matches(regex, id)) {
                log.error("User Id should only contain lower case alphabets and number");
                return -1;
            }

            // Save User ID and Private Key to mb.ini file
            util.saveToMbIni(id, keyPair.getPrivate());
            RestTemplate restTemplate = new RestTemplate();

            String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            UserRequest request = new UserRequest(id, publicKeyBase64);
            UserSuccess response = restTemplate.postForObject(uri, request, UserSuccess.class);
            if(response == null || response.getMessage() == null) {
                log.error("Error while saving the user details in the server");
                exitCode = -1;
                return exitCode;
            } else {
                log.info("User with Id {} and public key is saved to database", id);
            }

            log.info("User with Id {} is created", id);
            return exitCode;
        } catch (Exception e) {
            log.error("Error while creating user : \n {}", e.getMessage());
            exitCode = -1;
            return exitCode;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    int exitCode;

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(this, iFactory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
