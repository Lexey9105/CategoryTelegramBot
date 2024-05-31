package ru.pavlikov.categorybot.exception;
/**
 * @author pavlikov
 */
public class NoCategoryException extends RuntimeException {

    public NoCategoryException(){
        super("Вы не указали параметры запроса");
    }
}
