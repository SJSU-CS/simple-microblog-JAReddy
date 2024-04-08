package edu.sjsu.cmpe272.simpleblog.server.controller;

import edu.sjsu.cmpe272.simpleblog.common.request.MessageRequest;
import edu.sjsu.cmpe272.simpleblog.common.request.PaginatedRequest;
import edu.sjsu.cmpe272.simpleblog.common.response.MessageSuccess;
import edu.sjsu.cmpe272.simpleblog.common.response.MessageSuccessList;
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

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MessageRepository repository;

    @PostMapping("/create")
    public MessageSuccess createMessage(@RequestBody MessageRequest request) {
        Message msg = new Message(request);
        msg = repository.save(msg);

        MessageSuccess response = new MessageSuccess();
        response.setMessageId(msg.getMessageId());
        return response;
    }

    @PostMapping("/list")
    public MessageSuccessList listMessage(@RequestBody PaginatedRequest request) {
        Pageable pageable = PageRequest.of(request.getNext(), request.getLimit());
        Page<Message> msgList = repository.findAll(pageable);
        return convertToMessageSuccessList(msgList.getContent());
    }

    private MessageSuccessList convertToMessageSuccessList(List<Message> msgList) {
        List<MessageSuccess> resList = new ArrayList<>();
        for(Message msg: msgList) {
            resList.add(msg.toMessageSuccess());
        }
        MessageSuccessList res = new MessageSuccessList();
        res.setMsgSuccessList(resList);
        return res;
    }

}
