package org.example.webwebsocket.stomp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestChatContentsDto {
    private MessageType type;
    private String contents;

    public enum MessageType {
        SEND,
        EXIT
    }
}