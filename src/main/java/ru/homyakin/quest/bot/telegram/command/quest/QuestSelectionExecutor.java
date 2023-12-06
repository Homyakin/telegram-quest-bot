package ru.homyakin.quest.bot.telegram.command.quest;

import org.springframework.stereotype.Component;
import ru.homyakin.quest.bot.telegram.TelegramSender;
import ru.homyakin.quest.bot.telegram.command.CommandExecutor;

@Component
public class QuestSelectionExecutor extends CommandExecutor<QuestNextStage> {
    private final TelegramSender telegramSender;

    public QuestSelectionExecutor(TelegramSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    @Override
    public void execute(QuestNextStage command) {
        // TODO
    }
}
