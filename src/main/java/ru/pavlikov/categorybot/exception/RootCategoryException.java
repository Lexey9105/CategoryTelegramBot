package ru.pavlikov.categorybot.exception;

public class RootCategoryException extends RuntimeException {

    public RootCategoryException(){
        super("Кореневая категория уже создана");
    }
}
