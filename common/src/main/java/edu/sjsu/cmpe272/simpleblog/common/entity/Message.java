package edu.sjsu.cmpe272.simpleblog.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("message-id")
    Long messageId;

    LocalDateTime date;
    String author;

    String message;
    String attachment;

    String signature;

    public Message(Message msg) {
        date = msg.date;
        author = msg.author;
        message = msg.message;
        attachment = msg.attachment;;
        signature = msg.signature;
    }

    public Message() {

    }
}
