package com.example.companion_chat_api.ControllerImpl;


import com.example.companion_chat_api.Controller.ChattingInterfaceController;
import com.example.companion_chat_api.Model.ChatConv;
import com.example.companion_chat_api.Model.ChatMessage;
import com.example.companion_chat_api.Service.ChattingInterfaceService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/Chat")
public class ChattingInterfaceControllerImpl implements ChattingInterfaceController {

    private final ChattingInterfaceService chatService;

    @Autowired
    public ChattingInterfaceControllerImpl(ChattingInterfaceService chatService) {
        this.chatService = chatService;
    }

    @Override
    @PostMapping("send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage chatMessage) {
        ChatMessage savedMessage = chatService.saveMessage(chatMessage);
        return ResponseEntity.ok(savedMessage);
    }

    @Override
    @GetMapping("conversation")
    public ResponseEntity<List<ChatConv>> getConversation(@RequestParam Long userId,
                                                          @RequestParam Long withId) {
        List<ChatConv> conversation = chatService.getConversation(userId, withId);
        return ResponseEntity.ok(conversation);
    }
}
