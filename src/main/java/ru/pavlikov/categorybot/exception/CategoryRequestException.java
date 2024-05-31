package ru.pavlikov.categorybot.exception;
/**
 * @author pavlikov
 */
public class  CategoryRequestException extends RuntimeException {

    public CategoryRequestException(){
        super("Вы отправили некоретную команду");
    }
}
