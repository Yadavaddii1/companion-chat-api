package com.example.companion_chat_api.ServiceImpl;

import com.example.companion_chat_api.Model.ChatConv;
import com.example.companion_chat_api.Model.ChatMessage;
import com.example.companion_chat_api.Repository.ChatMessageRepository;
import com.example.companion_chat_api.Service.ChattingInterfaceService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ChattingInterfaceServiceImpl implements ChattingInterfaceService {

    private final ChatMessageRepository chatMessageRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private static final Long AI_USER_ID = 149304L; 

    @Autowired
    public ChattingInterfaceServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        // 1. Save user message
        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(new Date());
        }
        ChatMessage savedUserMessage = chatMessageRepository.save(chatMessage);

        // 2. Call Gemini AI
        String aiReply = callGeminiAPI(chatMessage.getMessage());

        // 3. Save AI response as a new ChatMessage
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setSenderId(AI_USER_ID); // AI as sender
        aiMessage.setReceiverId(chatMessage.getSenderId()); // user as receiver
        aiMessage.setMessage(aiReply);
        aiMessage.setTimestamp(new Date());

        chatMessageRepository.save(aiMessage);

        return savedUserMessage;
    }

    @SuppressWarnings("rawtypes")
    private String callGeminiAPI(String userMessage) {
    RestTemplate restTemplate = new RestTemplate();

    Map<String, Object> content = new HashMap<>();
    content.put("contents", new Object[] {
        Map.of("parts", new Object[] {
            Map.of("text", userMessage)
        })
    });

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-goog-api-key", apiKey);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(content, headers);

    try {
        Map response = restTemplate.postForObject(apiUrl, entity, Map.class);

        if (response != null && response.containsKey("candidates")) {
            var candidates = (java.util.List<Map<String, Object>>) response.get("candidates");
            if (!candidates.isEmpty()) {
                Map<String, Object> firstCandidate = candidates.get(0);

                Map<String, Object> contentMap = (Map<String, Object>) firstCandidate.get("content");
                if (contentMap != null) {
                    var parts = (java.util.List<Map<String, Object>>) contentMap.get("parts");
                    if (parts != null && !parts.isEmpty()) {
                        Object text = parts.get(0).get("text");
                        if (text != null) {
                            return text.toString().trim();
                        }
                    }
                }
            }
        }

        return "⚠️ Sorry, AI returned no text.";
    } catch (Exception e) {
        e.printStackTrace();
        return "⚠️ Error calling AI service: " + e.getMessage();
    }

  }

  @Override
  public List<ChatConv> getConversation(Long userId, Long withId) {
      // Fetch all messages where either is sender/receiver
      return chatMessageRepository.findConversation(userId, withId);
  }
}


