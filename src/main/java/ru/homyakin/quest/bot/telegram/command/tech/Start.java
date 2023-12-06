package ru.homyakin.quest.bot.telegram.command.tech;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.homyakin.quest.bot.telegram.command.Command;

public record Start(
    long userId
) implements Command {
    public static Start from(Message message) {
        return new Start(message.getFrom().getId());
    }
}
