package ru.pavlikov.categorybot.commands.executors;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;

public interface CommandExecutor {

    void execute(Command command) throws TelegramApiException;
}
