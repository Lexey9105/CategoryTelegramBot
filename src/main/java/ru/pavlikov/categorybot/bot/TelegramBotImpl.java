package ru.pavlikov.categorybot.bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pavlikov.categorybot.commands.CommandManagerImpl;

import java.util.function.Consumer;
/**
 * @author pavlikov
 */
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

    /**
     * Принимает апдейты с телеграма,
     * при помощи функционального интерфейса {@link Consumer<Update> updateAction.accept(update)}
     * передает ссылку на объект Update в блок кода {@link CommandManagerImpl}.
     */
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
