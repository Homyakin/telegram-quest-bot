package ru.homyakin.quest.bot.telegram.command.tech;

import org.springframework.stereotype.Component;
import ru.homyakin.quest.bot.locale.common.CommonLocalization;
import ru.homyakin.quest.bot.quest.services.QuestProcessor;
import ru.homyakin.quest.bot.telegram.TelegramSender;
import ru.homyakin.quest.bot.telegram.command.CommandExecutor;
import ru.homyakin.quest.bot.telegram.command.quest.QuestMapper;
import ru.homyakin.quest.bot.telegram.utils.TelegramMessage;

@Component
public class StartExecutor extends CommandExecutor<Start> {
    private final TelegramSender telegramSender;
    private final QuestProcessor questProcessor;

    public StartExecutor(TelegramSender telegramSender, QuestProcessor questProcessor) {
        this.telegramSender = telegramSender;
        this.questProcessor = questProcessor;
    }

    @Override
    public void execute(Start command) {
        questProcessor.clearUserState(command.userId());
        final var quests = questProcessor.getAllQuest();
        telegramSender.send(TelegramMessage.builder()
            .chatId(command.userId())
            .text(CommonLocalization.start())
            .keyboard(QuestMapper.questsToKeyboard(quests))
            .build()
        );
    }
}
