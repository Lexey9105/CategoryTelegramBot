package ru.pavlikov.categorybot.commands;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.bot.TelegramBotImpl;
import ru.pavlikov.categorybot.commands.executors.*;
import ru.pavlikov.categorybot.exception.CategoryRequestException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class CommandManagerImpl implements CommandManager{

    private final TelegramBotImpl categoryBot;
    private final HelpCommand helpCommand;
    private final AddElementCommand addElementCommand;
    private final ViewTreeCommand viewTreeCommand;
    private final RemoveElementCommand removeElement;
    private final SendMessageUtils sendMessageUtils;
    private final DocumentUploader documentUploader;





    private final Pattern pattern = Pattern.compile(
            "/(addElement|removeElement|viewTree|download|help|start)\\s*(<[^>]+>)?\\s*(<[^>]+>)?");


    public void updateGet(Update update){
        try {
        updateParserAndPerform(update);
     }catch(Exception e){
            try {
                sendMessageUtils.sendMessage(update.getMessage().getChatId().toString(), e.getMessage());
            }catch (TelegramApiException exc) {
                throw new RuntimeException(exc);
            }

        }

    }

    @Override
    public void updateParserAndPerform(Update update)  {
        System.out.println("1");
        Long chatId=update.getMessage().getChatId();
        String input;
               //
        Optional<String> as=Optional.ofNullable(update.getMessage().getCaption());
        if(as.isPresent()){
            input=update.getMessage().getCaption();
        }else {input=update.getMessage().getText();}


        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String command = matcher.group(1);

            Optional<String> parentCategory=Optional.ofNullable(matcher.group(2));
            Optional<String> childrenCategory=Optional.ofNullable(matcher.group(3));
            Command newCommand;
            if(parentCategory.isPresent()&&childrenCategory.isPresent()){
                newCommand=createCommand(chatId,command,parentCategory.get(),childrenCategory.get());
            } else
                newCommand = parentCategory.map(s -> createCommand(chatId, command, s, "null"))
                        .orElseGet(() -> createCommand(chatId, command, "null", "null"));

            try {
            switch (command) {
                case "addElement":
                    addElementCommand.execute(newCommand);
                    break;
                case "removeElement":
                    removeElement.execute(newCommand);
                    break;
                case "viewTree":
                    viewTreeCommand.execute(newCommand);
                    break;
                case "help":
                    helpCommand.execute(newCommand);
                    break;
                case "start":
                    sendMessageUtils.sendMessage(newCommand.getChatId().toString(), "Вас приветствует бот для создания древа категорий. Для получения списка команд отправьте '/help'");
                    break;

            }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new CategoryRequestException();
        }
    }

    private Command createCommand(Long chatId,String command,String parentCategory,String childrenCategory){


        Command newCommand=new Command();

            newCommand.setChatId(chatId);
            newCommand.setCommand(command);
            newCommand.setParentCategory(parentCategory);
            newCommand.setChildrenCategory(childrenCategory);
            return newCommand;

    }

    @PostConstruct
    private void subscribeToBot() {
        categoryBot.setUpdateAction(this::updateGet);
    }

}
