package edu.sjsu.cmpe272.simpleblog.server.controller;

import edu.sjsu.cmpe272.simpleblog.common.request.UserRequest;
import edu.sjsu.cmpe272.simpleblog.common.response.UserSuccess;
import edu.sjsu.cmpe272.simpleblog.server.entity.User;
import edu.sjsu.cmpe272.simpleblog.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository repository;

    @PostMapping("/create")
    public UserSuccess createUser(@RequestBody UserRequest user) {
        User usr = new User(user);
        Optional<User> existingUser = repository.findById(user.getUser());
        if (existingUser.isPresent()) {
            return new UserSuccess("Duplicate Id");
        } else{
            repository.save(usr);
        }
        UserSuccess res = new UserSuccess("welcome");
        return res;
    }

    @GetMapping("/{userName}/public-key")
    public String getPublicKey(@PathVariable String userName) {
        Optional<User> usr = repository.findById(userName);
        if (usr.isPresent()) {
            return usr.get().getPublicKey();
        } else {
            return "Username not found";
        }
    }
}
