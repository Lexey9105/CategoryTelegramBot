package ru.pavlikov.categorybot.commands.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.service.impl.CategoryService;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

/**
 * @author pavlikov
 */
@Component
@RequiredArgsConstructor
public class RemoveElementCommand {

    private final SendMessageUtils sendMessageUtils;
    private final CategoryService categoryService;


    /**
     * Принимает {@link Command} и исполняет команду /download
     *
     * @param command - команда с аргументами
     */

    public void execute(Command command) {
        try {
            categoryService.removeByNameAndChatId(command);
            sendMessageUtils.sendMessage(command.getChatId().toString(), "Категория " + command.getParentCategory() + " успешно удалена");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Принимает {@link Command} и исполняет команду /download
     *
     * @param command - команда с аргументами
     */
    public void delete(Command command) throws TelegramApiException {
        categoryService.deleteAll(command);
        sendMessageUtils.sendMessage(command.getChatId().toString(), "Удалены все добавленные категории");
    }
}
