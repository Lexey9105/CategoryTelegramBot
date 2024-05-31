package ru.pavlikov.categorybot.exception;
/**
 * @author pavlikov
 */
public class DocumentDownloadException extends RuntimeException{

    public DocumentDownloadException() {
        super("Ошибка при записи файла");
    }
}
