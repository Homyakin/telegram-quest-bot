package ru.homyakin.quest.bot.telegram.command.tech;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import ru.homyakin.quest.bot.locale.common.CommonLocalization;
import ru.homyakin.quest.bot.telegram.TelegramSender;
import ru.homyakin.quest.bot.telegram.command.CommandExecutor;
import ru.homyakin.quest.bot.telegram.utils.ReplyKeyboardBuilder;
import ru.homyakin.quest.bot.telegram.utils.SendMessageBuilder;

@Component
public class StartExecutor extends CommandExecutor<Start> {
    private final TelegramSender telegramSender;

    public StartExecutor(TelegramSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    @Override
    public void execute(Start command) {
        // TODO сбросить стейт пользователя
        telegramSender.send(SendMessageBuilder.builder()
            .chatId(command.userId())
            .text(CommonLocalization.start())
            .keyboard(
                ReplyKeyboardBuilder
                    .builder()
                    .addRow()
                    .addButton(KeyboardButton.builder().text("/start").build())
                    .build()
            )
            .build()
        );
    }
}
