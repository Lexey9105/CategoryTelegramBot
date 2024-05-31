package ru.pavlikov.categorybot.commands.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.service.impl.CategoryService;
import ru.pavlikov.categorybot.service.impl.excel.ExcelParser;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

/**
 * @author pavlikov
 */
@Component
@RequiredArgsConstructor
public class UploadCommand implements CommandHandler {

    private final ExcelParser excelParser;
    private final CategoryService categoryService;
    private final SendMessageUtils sendMessageUtils;

    /**
     * Принимает {@link Command} и исполняет команду /download
     * @param command - команда с аргументами
     */
    @Override
    public void execute(Command command) throws TelegramApiException {
        categoryService.saveAll(command,excelParser.parse(command.getFile(),command.getChatId()));
        sendMessageUtils.sendMessage(command.getChatId().toString(), "Категории успешно добавлены");

    }
}
