package edu.sjsu.cmpe272.simpleblog.server.controller;

import edu.sjsu.cmpe272.simpleblog.common.request.PaginatedRequest;
import edu.sjsu.cmpe272.simpleblog.server.entity.Message;
import edu.sjsu.cmpe272.simpleblog.server.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MessageRepository repository;

    @PostMapping("/create")
    public Message createMessage(@RequestBody Message request) {
        Message msg = new Message(request);
        msg = repository.save(msg);

        Message resMsg = new Message();
        resMsg.setMessageId(msg.getMessageId());
        return resMsg;
    }

    @PostMapping("/list")
    public Page<Message> listMessage(@RequestBody PaginatedRequest request) {
        Pageable pageable = PageRequest.of(request.getNext(), request.getLimit());
        Page<Message> msgList = repository.findAll(pageable);
        return msgList;
    }
}
