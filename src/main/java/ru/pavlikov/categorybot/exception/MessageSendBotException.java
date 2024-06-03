package ru.pavlikov.categorybot.exception;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSendBotException extends RuntimeException {

    public MessageSendBotException(TelegramApiException e) {
        super("Ошибка при отправке сообщения");
    }
}
