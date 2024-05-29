package ru.pavlikov.categorybot.exception;

public class  CategoryRequestException extends RuntimeException {

    public CategoryRequestException(){
        super("Вы отправили некоретную команду");
    }
}
