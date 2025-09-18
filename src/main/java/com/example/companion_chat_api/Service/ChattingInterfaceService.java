package com.example.companion_chat_api.Service;

import java.util.List;

import com.example.companion_chat_api.Model.ChatConv;
import com.example.companion_chat_api.Model.ChatMessage;

public interface ChattingInterfaceService {
    
    ChatMessage saveMessage(ChatMessage chatMessage);

    List<ChatConv> getConversation(Long userId, Long withId);
}
