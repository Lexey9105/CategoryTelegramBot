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
public class ViewTreeCommand {

    private final SendMessageUtils sendMessageUtils;
    private final CategoryService categoryService;

    /**
     * Принимает {@link Command} и исполняет команду /download
     *
     * @param command - команда с аргументами
     */

    public void execute(Command command) throws TelegramApiException {
        sendMessageUtils.sendMessage(command.getChatId().toString(), categoryService.treeToString(command));
    }
}
