package ru.pavlikov.categorybot.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message){
        super("Категории с именем "+message+" не существует");
    }
}
