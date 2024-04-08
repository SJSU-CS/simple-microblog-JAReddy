package edu.sjsu.cmpe272.simpleblog.server.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    String user;

    @JsonProperty("public-key")
    String publicKey;

    public User(User usr) {
        user = usr.user;
        publicKey = usr.publicKey;
    }

    public User() {

    }
}
