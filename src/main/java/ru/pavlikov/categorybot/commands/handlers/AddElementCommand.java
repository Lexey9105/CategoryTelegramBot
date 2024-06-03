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
public class AddElementCommand  {

    private final SendMessageUtils sendMessageUtils;
    private final CategoryService categoryService;

    /**
     * Принимает {@link Command} и исполняет команду /addElement
     * @param command - команда с аргументами
     */

    public void execute(Command command)  {
        if (command.getChildrenCategory().equals("null")) {
            try {
                categoryService.saveRootCategory(command);
                sendMessageUtils.sendMessage(command.getChatId().toString(), "Корневая категория "+command.getParentCategory()+" успешно добавлена");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                categoryService.saveChildrenCategory(command);
                sendMessageUtils.sendMessage(command.getChatId().toString(), "Дочерняя категория "+command.getChildrenCategory()+" успешно добавлена к "+command.getParentCategory());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
