package ru.pavlikov.categorybot.exception;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class FileSendBotException extends RuntimeException {

    public FileSendBotException(TelegramApiException e) {
        super("Ошибка при отправке файла");
    }
}
