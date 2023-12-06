package ru.homyakin.quest.bot.telegram.command.quest;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.homyakin.quest.bot.telegram.command.Command;

public record QuestSelection(
    long userId,
    String text
) implements Command {
    public static QuestSelection from(Message message) {
        return new QuestSelection(message.getFrom().getId(), message.getText());
    }
}
