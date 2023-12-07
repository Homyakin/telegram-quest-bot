package ru.homyakin.quest.bot.telegram.command.tech;

import org.springframework.stereotype.Component;
import ru.homyakin.quest.bot.locale.common.CommonLocalization;
import ru.homyakin.quest.bot.telegram.TelegramSender;
import ru.homyakin.quest.bot.telegram.command.CommandExecutor;
import ru.homyakin.quest.bot.telegram.utils.TelegramMessage;

@Component
public class AboutExecutor extends CommandExecutor<About> {
    private final TelegramSender telegramSender;

    public AboutExecutor(TelegramSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    @Override
    public void execute(About command) {
        telegramSender.send(TelegramMessage.builder()
            .chatId(command.userId())
            .text(CommonLocalization.about())
            .build()
        );
    }
}
