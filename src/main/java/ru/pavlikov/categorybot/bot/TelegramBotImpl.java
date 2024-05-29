package ru.pavlikov.categorybot.bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.stickers.UploadStickerFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.commands.CommandManager;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class TelegramBotImpl extends TelegramLongPollingBot {

    private Consumer<? super Update> updateAction;
    private final String username;

    public TelegramBotImpl(String token, String username) {
        super(new DefaultBotOptions(), token);
        this.username = username;
    }
    public void setUpdateAction(Consumer<? super Update> updateHandler) {
        updateAction = updateHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
            if (updateAction != null && update != null) {
                    updateAction.accept(update);
            }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBaseUrl() {
        return super.getBaseUrl();
    }


}
