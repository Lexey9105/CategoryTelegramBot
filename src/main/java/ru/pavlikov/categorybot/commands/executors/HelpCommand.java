package ru.pavlikov.categorybot.commands.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

@Component
@RequiredArgsConstructor
public class HelpCommand implements CommandExecutor{

    private final SendMessageUtils sendMessageUtils;
    public final static String HELP_MESSAGE =
            """
                    добавить корневой элемент:

                    /addElement <название элемента>

                    добавить дочерний элемент к существующему элементу:
                    /addElement <родительский элемент> <дочерний элемент>

                    просмотр всего дерева категорий:
                    /viewTree

                    удалить элемент и все его дочерние элементы:
                    /removeElement <название элемента>

                    список всех доступных команд и краткое их описание:
                    /help""";

    @Override
    public void execute(Command command) throws TelegramApiException {
        try {
            sendMessageUtils.sendMessage(command.getChatId().toString(), HELP_MESSAGE);
        }catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
    }
}
