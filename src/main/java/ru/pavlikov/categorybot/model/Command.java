package ru.pavlikov.categorybot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

/**
 * @author pavlikov
 */
@Getter
@Setter
@NoArgsConstructor
public class Command {
    private Long chatId;
    private String command;
    private String parentCategory;
    private String childrenCategory;
    private String massageText;
    private File file;

}
