package ru.pavlikov.categorybot.exception;

/**
 * @author pavlikov
 */
public class RootCategoryException extends RuntimeException {

    public RootCategoryException() {
        super("Кореневая категория уже создана");
    }
}
