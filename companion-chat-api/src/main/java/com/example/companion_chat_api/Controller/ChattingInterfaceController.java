package com.example.companion_chat_api.Controller;
import org.springframework.http.ResponseEntity;

import com.example.companion_chat_api.Model.ChatConv;
import com.example.companion_chat_api.Model.ChatMessage;

import java.util.List;

@FeignClient(name = "ChattingInterfaceController", url = "${superapp.url}", path = "/Chat", primary = false)
@ResponseBody
public interface ChattingInterfaceController {

    ResponseEntity<ChatMessage> sendMessage(ChatMessage chatMessage);

    ResponseEntity<List<ChatConv>> getConversation(Long userId, Long withId);

}
