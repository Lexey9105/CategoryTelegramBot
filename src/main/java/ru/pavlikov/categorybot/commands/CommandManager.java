package ru.pavlikov.categorybot.commands;


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;

public interface CommandManager {

   void updateParserAndPerform(Update update) throws TelegramApiException;

}
