package ru.pavlikov.categorybot.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBotImpl extends TelegramLongPollingBot {
    private final String username;

    public TelegramBotImpl(String token, String username) {
        super(new DefaultBotOptions(), token);
        this.username = username;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
