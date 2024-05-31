package ru.pavlikov.categorybot.commands.handlers;


import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;

public interface CommandHandler {

    void execute(Command command) throws TelegramApiException;
}
