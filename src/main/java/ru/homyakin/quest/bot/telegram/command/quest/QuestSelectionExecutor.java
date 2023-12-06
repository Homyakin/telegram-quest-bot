package ru.homyakin.quest.bot.telegram.command.quest;

import org.springframework.stereotype.Component;
import ru.homyakin.quest.bot.locale.common.CommonLocalization;
import ru.homyakin.quest.bot.quest.services.QuestProcessor;
import ru.homyakin.quest.bot.telegram.TelegramSender;
import ru.homyakin.quest.bot.telegram.command.CommandExecutor;
import ru.homyakin.quest.bot.telegram.utils.TelegramMessage;

@Component
public class QuestSelectionExecutor extends CommandExecutor<QuestSelection> {
    private final TelegramSender telegramSender;
    private final QuestProcessor questProcessor;

    public QuestSelectionExecutor(TelegramSender telegramSender, QuestProcessor questProcessor) {
        this.telegramSender = telegramSender;
        this.questProcessor = questProcessor;
    }

    @Override
    public void execute(QuestSelection command) {
        questProcessor.startQuest(command.text(), command.userId())
            .ifPresentOrElse(
                questStage -> telegramSender.send(QuestMapper.questStageToTelegramMessage(questStage, command.userId())),
                () -> telegramSender.send(
                    TelegramMessage.builder().chatId(command.userId()).text(CommonLocalization.questNotFound()).build()
                )
            );
    }
}
