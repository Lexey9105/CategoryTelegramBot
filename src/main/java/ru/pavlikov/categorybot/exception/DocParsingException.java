package ru.pavlikov.categorybot.exception;

/**
 * @author pavlikov
 */
public class DocParsingException extends RuntimeException {

    public DocParsingException() {
        super("Возникла ошибка при чтении документа");
    }
}
