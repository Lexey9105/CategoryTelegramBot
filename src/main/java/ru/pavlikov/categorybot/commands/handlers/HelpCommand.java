package ru.pavlikov.categorybot.commands.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.utils.SendMessageUtils;
/**
 * @author pavlikov
 */
@Component
@RequiredArgsConstructor
public class HelpCommand  {

    private final SendMessageUtils sendMessageUtils;
    public final static String HELP_MESSAGE =
            """
                    Добавить корневой элемент:
                    /addElement <название элемента>

                    Добавить дочерний элемент к существующему элементу:
                    /addElement <родительский элемент> <дочерний элемент>

                    Просмотр всего дерева категорий:
                    /viewTree

                    Удалить элемент и все его дочерние элементы:
                    /removeElement <название элемента>
                    
                    Удаление всех категорий
                    /removeAll

                    Список всех доступных команд и краткое их описание:
                    /help
                    
                    Отправить Excel документ с деревом категорий для сохранения:
                    /upload
                    
                    Получить Excel документ с деревом категорий:
                    /download
                                     
                    """;
    /**
     * Принимает {@link Command} и исполняет команду /download
     * @param command - команда с аргументами
     */

    public void execute(Command command) throws TelegramApiException {
        try {
            sendMessageUtils.sendMessage(command.getChatId().toString(), HELP_MESSAGE);
        }catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
    }
}
