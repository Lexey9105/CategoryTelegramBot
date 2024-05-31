package ru.pavlikov.categorybot.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pavlikov.categorybot.exception.CategoryRequestException;
import ru.pavlikov.categorybot.exception.NoCategoryException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Создает екземпляр {@link Command} с данными из апдейта.
 * При помощи {@link Pattern}  парсит :
 * команду пользователя и id чата {@link CommandService#addUserCommand(String, Long)},
 * {@link File} переданный с update,команду из вложения к файлу,id чата {@link CommandService#addUserCommand(Update, Long)},
 * Аргументы для работы {@link ru.pavlikov.categorybot.commands.handlers.AddElementCommand} и {@link ru.pavlikov.categorybot.commands.handlers.RemoveElementCommand}
 * @author pavlikov
 */
@Component
@RequiredArgsConstructor
public class CommandService {

    private final FileUtils fileUtils;

    private final Pattern pattern = Pattern.compile(
            "/(addElement|removeElement|removeAll|viewTree|download|upload|help|start)\\s*(<[^>]+>)?\\s*(<[^>]+>)?");


    public Command addUserCommand(String input, Long chatId){

        Matcher matcher = pattern.matcher(input);
        if(matcher.find()) {
            String output = matcher.group(1);
                Command command = new Command();
                command.setCommand(output);
                command.setChatId(chatId);
                command.setMassageText(input);

            return command;}
        else {throw new CategoryRequestException();}
    }
    public Command addUserCommand(Update update, Long chatId){
        Matcher matcher = pattern.matcher(update.getMessage().getCaption());
        if(matcher.find()) {
            String output = matcher.group(1);
            Command command=new Command();
            command.setCommand(output);
            command.setChatId(chatId);
            command.setFile(fileUtils.uploadDocument(update));
            return command;}
        else throw new CategoryRequestException();
    }

    public Command addParentAndChildren(Command command, String categoriesArguments){
        Matcher matcher = pattern.matcher(categoriesArguments);
        if(matcher.find()) {
        Optional<String> parentCategory=Optional.ofNullable(matcher.group(2));
        Optional<String> childrenCategory=Optional.ofNullable(matcher.group(3));

        if(parentCategory.isPresent()&&childrenCategory.isPresent()){
            command.setParentCategory(parentCategory.get());
            command.setChildrenCategory(childrenCategory.get());
        } else if(parentCategory.isPresent()) {
            command.setParentCategory(parentCategory.get());
            command.setChildrenCategory("null");
        }
        }
        else throw new NoCategoryException();
        return command;
    }

    public Command addUserFile(Command command) throws IOException {
        File file = File.createTempFile("Categories",".xlsx");
        command.setFile(file);
        return command;


    }



}
