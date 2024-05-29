package ru.pavlikov.categorybot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.exception.NoCategoryException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.service.impl.CategoryService;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

@Component
@RequiredArgsConstructor
public class RemoveElementCommand implements CommandExecutor {

    private final SendMessageUtils sendMessageUtils;
    private final CategoryService categoryService;
    @Override
    public void execute(Command command)  {
        if (command.getParentCategory().equals("null")){
            throw new NoCategoryException();
        }

        try {
            categoryService.removeByNameAndChatId(command,command.getChatId());
            sendMessageUtils.sendMessage(command.getChatId().toString(), "Категория "+command.getParentCategory()+" успешно удалена");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
