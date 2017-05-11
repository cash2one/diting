package com.diting.service;

import com.diting.model.Chat;

import java.util.Map;

/**
 * ChatService.
 */
public interface ChatService {

    Chat getChat(Chat chat);

    Chat getRemoteChat(Chat chat);

    Chat getAngelChat(Chat chat);

    Chat getWxChat(Chat chat);
}
