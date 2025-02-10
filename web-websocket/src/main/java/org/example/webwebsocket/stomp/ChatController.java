package org.example.webwebsocket.stomp;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/test") // 메세지 전송
    public void sendMessage(RequestChatContentsDto message) throws Exception {
        Thread.sleep(1000); // simulated delay

        chatService.manipulateChatContents(message.getContents());

        template.convertAndSend("/sub", message); // 구독한 채팅방으로 메세지 전송
    }
}