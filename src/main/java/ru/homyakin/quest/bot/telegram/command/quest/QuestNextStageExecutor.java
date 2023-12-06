package ru.homyakin.quest.bot.telegram.command.quest;

import org.springframework.stereotype.Component;
import ru.homyakin.quest.bot.quest.models.UserAnswer;
import ru.homyakin.quest.bot.quest.services.QuestProcessor;
import ru.homyakin.quest.bot.telegram.TelegramSender;
import ru.homyakin.quest.bot.telegram.command.CommandExecutor;
import ru.homyakin.quest.bot.telegram.utils.SendMessageBuilder;

@Component
public class QuestNextStageExecutor extends CommandExecutor<QuestNextStage> {
    private final TelegramSender telegramSender;
    private final QuestProcessor questProcessor;

    public QuestNextStageExecutor(TelegramSender telegramSender, QuestProcessor questProcessor) {
        this.telegramSender = telegramSender;
        this.questProcessor = questProcessor;
    }

    @Override
    public void execute(QuestNextStage command) {
        final var next = questProcessor.processStage(
            command.quest().name(),
            command.userId(),
            new UserAnswer(
                command.text(), null //TODO
            )
        ).orElseThrow();

        telegramSender.send(
            SendMessageBuilder
                .builder()
                .chatId(command.userId())
                .text(next.text())
                .build()
        );
    }
}
