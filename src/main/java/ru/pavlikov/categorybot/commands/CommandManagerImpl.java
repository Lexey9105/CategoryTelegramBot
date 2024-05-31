package ru.pavlikov.categorybot.commands;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.bot.TelegramBotImpl;
import ru.pavlikov.categorybot.commands.handlers.*;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * @author pavlikov
 */
@Service
@RequiredArgsConstructor
public class CommandManagerImpl implements CommandManager{

    private final TelegramBotImpl categoryBot;
    private final CommandService commandService;
    private final HelpCommand helpCommand;
    private final AddElementCommand addElementCommand;
    private final ViewTreeCommand viewTreeCommand;
    private final RemoveElementCommand removeElement;
    private final UploadCommand uploadCommand;
    private final DownloadCommand downloadCommand;
    private final SendMessageUtils sendMessageUtils;

    /**
     * Принимает ссылки на апдейты и передает в метод  updateParserAndPerform.
     * Возникающие исключения передает в чат с которого прищел апдейт.
     * При помощи {@link CommandService} парсит при необходимости данные из update,
     * в последтвии передавая их при помощи {@link Command} обработчикам
     * @param update апдейт {@link Update} с командой
     * @author pavlikov
     */
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
    public void updateParserAndPerform(Update update) throws TelegramApiException, IOException {
        Long chatId=update.getMessage().getChatId();
        Command command;

        Optional<String> checkCaption=Optional.ofNullable(update.getMessage().getCaption());
        if(checkCaption.isPresent()){
            command=commandService.addUserCommand(update,chatId);
        }else {command=commandService.addUserCommand(update.getMessage().getText(),chatId);}


            switch (command.getCommand()) {
                case "addElement":
                    commandService.addParentAndChildren(command,update.getMessage().getText());
                    addElementCommand.execute(command);
                    break;
                case "removeElement":
                    commandService.addParentAndChildren(command,update.getMessage().getText());
                    removeElement.execute(command);
                    break;
                case "removeAll":
                    removeElement.delete(command);
                    break;
                case "viewTree":
                    viewTreeCommand.execute(command);
                    break;
                case "upload":
                    uploadCommand.execute(command);
                    break;
                case "download":
                    downloadCommand.execute(commandService.addUserFile(command));
                    break;
                case "help":
                    helpCommand.execute(command);
                    break;
                case "start":
                    sendMessageUtils.sendMessage(command.getChatId().toString(), "Вас приветствует бот для создания древа категорий. Для получения списка команд отправьте '/help'");
                    break;
            }
    }



    @PostConstruct
    private void subscribeToBot() {
        categoryBot.setUpdateAction(this::updateGet);
    }

}
