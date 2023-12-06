package ru.homyakin.quest.bot.telegram.command.quest;

import org.springframework.stereotype.Component;
import ru.homyakin.quest.bot.locale.common.CommonLocalization;
import ru.homyakin.quest.bot.quest.models.AnswerType;
import ru.homyakin.quest.bot.quest.models.UserAnswer;
import ru.homyakin.quest.bot.quest.services.QuestProcessor;
import ru.homyakin.quest.bot.telegram.TelegramSender;
import ru.homyakin.quest.bot.telegram.command.CommandExecutor;
import ru.homyakin.quest.bot.telegram.utils.TelegramMessage;

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
        questProcessor.processStage(
            command.quest().name(),
            command.userId(),
            new UserAnswer(
                command.text(), AnswerType.USER_INPUT
            )
        ).ifPresentOrElse(
            questStage -> {
                telegramSender.send(QuestMapper.questStageToTelegramMessage(questStage, command.userId()));
                if (questStage.isFinal()) {
                    telegramSender.send(
                        TelegramMessage.builder()
                            .chatId(command.userId())
                            .text(CommonLocalization.questEnding())
                            .keyboard(QuestMapper.questsToKeyboard(questProcessor.getAllQuest()))
                            .build()
                    );
                }
            },
            () -> telegramSender.send(
                TelegramMessage.builder().chatId(command.userId()).text(CommonLocalization.questNotFound()).build()
            )
        );
    }
}
