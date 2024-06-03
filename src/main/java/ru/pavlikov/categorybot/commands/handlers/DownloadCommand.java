package ru.pavlikov.categorybot.commands.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.service.impl.excel.ExcelBuilder;
import ru.pavlikov.categorybot.utils.SendMessageUtils;

/**
 * @author pavlikov
 */
@Component
@RequiredArgsConstructor
public class DownloadCommand {

    private final SendMessageUtils sendMessageUtils;
    private final ExcelBuilder excelBuilder;


    /**
     * Принимает {@link Command} и исполняет команду /download
     *
     * @param command - команда с аргументами
     */

    public void execute(Command command) throws TelegramApiException {
        excelBuilder.build(command);
        sendMessageUtils.sendFile(command.getChatId().toString(), command.getFile());
    }
}
