package com.example.companion_chat_api.Repository;

import com.example.companion_chat_api.Model.ChatConv;
import com.example.companion_chat_api.Model.ChatMessage;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {


    @Query("SELECT c FROM ChatConv c WHERE " +
           "(c.senderId = :userId AND c.receiverId = :withId) OR " +
           "(c.senderId = :withId AND c.receiverId = :userId) " +
           "ORDER BY c.timestamp ASC")
    List<ChatConv> findConversation(@Param("userId") Long userId,
                                    @Param("withId") Long withId);
                                    
}
