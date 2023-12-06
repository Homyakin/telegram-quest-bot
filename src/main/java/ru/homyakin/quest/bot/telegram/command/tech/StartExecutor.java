package ru.homyakin.quest.bot.telegram.command.tech;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
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
        telegramSender.send(SendMessageBuilder.builder()
            .chatId(command.userId())
            .text("Hello world")
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
