package ru.pavlikov.categorybot.exception;

public class NoCategoryException extends RuntimeException {

    public NoCategoryException(){
        super("Вы не указали параметры запроса");
    }
}
