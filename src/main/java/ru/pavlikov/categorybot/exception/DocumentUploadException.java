package ru.pavlikov.categorybot.exception;

/**
 * @author pavlikov
 */
public class DocumentUploadException extends RuntimeException {

    public DocumentUploadException() {
        super("Ошибка загрузки документа");
    }
}
