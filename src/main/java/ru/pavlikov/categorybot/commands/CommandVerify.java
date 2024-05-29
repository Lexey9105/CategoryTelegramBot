package ru.pavlikov.categorybot.commands;

import ru.pavlikov.categorybot.model.Command;

public class CommandVerify {

    private static boolean requestParameters(Command command){

        return command.getParentCategory().equals("null");

    }


}
