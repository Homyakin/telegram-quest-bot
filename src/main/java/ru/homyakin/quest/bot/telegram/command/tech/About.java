package ru.homyakin.quest.bot.telegram.command.tech;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.homyakin.quest.bot.telegram.command.Command;

public record About(
    long userId
) implements Command {
    public static About from(Message message) {
        return new About(message.getFrom().getId());
    }
}
