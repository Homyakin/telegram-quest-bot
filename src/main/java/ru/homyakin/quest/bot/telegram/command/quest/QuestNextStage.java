package ru.homyakin.quest.bot.telegram.command.quest;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.telegram.command.Command;

public record QuestNextStage(
    Quest quest,
    long userId,
    String text
) implements Command {
    public static QuestNextStage from(Message message, Quest quest) {
        return new QuestNextStage(
            quest,
            message.getFrom().getId(),
            message.getText()
        );
    }
}
