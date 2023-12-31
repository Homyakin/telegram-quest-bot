package ru.homyakin.quest.bot.telegram.command;

import java.util.Arrays;
import java.util.Optional;

public enum CommandType {
    START("/start"),
    ABOUT("/about"),
    ;

    private final String text;

    CommandType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Optional<CommandType> getFromString(String text) {
        return Arrays.stream(values())
            .filter(type -> type.check(text))
            .findFirst();
    }

    private boolean check(String text) {
        return this.text.equals(text);
    }
}
