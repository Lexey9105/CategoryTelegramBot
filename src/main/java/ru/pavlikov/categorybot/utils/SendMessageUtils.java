package ru.pavlikov.categorybot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.bot.TelegramBotImpl;
import ru.pavlikov.categorybot.exception.FileSendBotException;
import ru.pavlikov.categorybot.exception.MessageSendBotException;

import java.io.File;

/**
 * Воспомогательный класс для отправки сообщений и фалов в чат
 * @author pavlikov
 */
@RequiredArgsConstructor
@Component
public class SendMessageUtils {

    private final TelegramBotImpl telegramBot;

    public void sendMessage(String chatId, String message)
            throws TelegramApiException {
        try {
            telegramBot.execute(new SendMessage(chatId, message));
        } catch (TelegramApiException e) {
            throw new MessageSendBotException(e);
        }

    }

    public void sendFile(String chatId, File file) {
        InputFile inputFile = new InputFile(file);
        try {
            telegramBot.execute(new SendDocument(chatId, inputFile));
        } catch (TelegramApiException e) {
            throw new FileSendBotException(e);
        }

    }
}
