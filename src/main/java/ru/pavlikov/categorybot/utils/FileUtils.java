package ru.pavlikov.categorybot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pavlikov.categorybot.bot.TelegramBotImpl;
import ru.pavlikov.categorybot.exception.DocumentUploadException;

import java.io.File;
/**
 * Воспомогательный класс для загрузки фалов из чата
 * @author pavlikov
 */
@Component
@RequiredArgsConstructor
public class FileUtils {

    private final TelegramBotImpl bot;

    public  File uploadDocument(Update update)  {
        try{
        org.telegram.telegrambots.meta.api.objects.File file=bot.execute(new GetFile(update.getMessage().getDocument().getFileId()));

        java.io.File fileCategories1= java.io.File.createTempFile("Categories",".xlsx");
        return bot.downloadFile(file,fileCategories1);}catch (Exception e){
            throw new DocumentUploadException();

        }
    }
}
