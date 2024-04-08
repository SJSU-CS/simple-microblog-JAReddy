package edu.sjsu.cmpe272.simpleblog.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageSuccess {
    Long messageId;
    LocalDateTime date;
    String author;

    String message;
    String attachment;

    String signature;

    public MessageSuccess() {

    }

    public MessageSuccess(Long messageId, LocalDateTime date, String author, String message, String attachment, String signature) {
        this.messageId = messageId;
        this.date = date;
        this.author = author;
        this.message = message;
        this.attachment = attachment;
        this.signature = signature;
    }

    @Override
    public String toString() {
        String paperClip = "";
        if(attachment != null) {
            paperClip = "📎";
        }
        String val = messageId + ": " + date + " " + author + " says " + "\"" + message+ "\"" +paperClip;
        return val;
    }
}
