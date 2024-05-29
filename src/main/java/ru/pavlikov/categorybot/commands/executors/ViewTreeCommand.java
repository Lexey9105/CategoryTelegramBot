package ru.pavlikov.categorybot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.service.impl.CategoryService;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

@Component
@RequiredArgsConstructor
public class ViewTreeCommand implements CommandExecutor{

    private final SendMessageUtils sendMessageUtils;
    private final CategoryService categoryService;
    @Override
    public void execute(Command command) throws TelegramApiException {
        try {
            sendMessageUtils.sendMessage(command.getChatId().toString(), categoryService.treeToString(command.getChatId()));
        }catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
    }
}
