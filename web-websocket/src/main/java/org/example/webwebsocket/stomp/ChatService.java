package org.example.webwebsocket.stomp;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatService {
    public String manipulateChatContents(String contents) {
        return contents;
    }
}
