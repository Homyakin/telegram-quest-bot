package ru.homyakin.quest.bot.telegram.command;

import java.lang.reflect.ParameterizedType;

public abstract class CommandExecutor<T extends Command> {

    public abstract void execute(T command);

    @SuppressWarnings("unchecked")
    public final Class<T> getCommandType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
