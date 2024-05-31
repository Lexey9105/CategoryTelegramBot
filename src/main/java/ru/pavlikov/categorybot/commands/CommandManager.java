package ru.pavlikov.categorybot.commands;


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface CommandManager {

   void updateParserAndPerform(Update update) throws TelegramApiException, IOException;

}
