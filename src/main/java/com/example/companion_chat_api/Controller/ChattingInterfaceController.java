package com.example.companion_chat_api.Controller;
import org.springframework.http.ResponseEntity;

import com.example.companion_chat_api.Model.ChatConv;
import com.example.companion_chat_api.Model.ChatMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@FeignClient(name = "ChattingInterfaceController", url = "${superapp.url}", path = "/Chat", primary = false)
@ResponseBody
public interface ChattingInterfaceController {

    ResponseEntity<ChatMessage> sendMessage(ChatMessage chatMessage);

    ResponseEntity<List<ChatConv>> getConversation(Long userId, Long withId);

}
