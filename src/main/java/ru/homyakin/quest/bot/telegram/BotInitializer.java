package ru.homyakin.quest.bot.telegram;

import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllPrivateChats;
import ru.homyakin.quest.bot.telegram.command.CommandType;

@Configuration
public class BotInitializer {
    private final TelegramSender telegramSender;

    public BotInitializer(TelegramSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initCommands() throws Exception {
        telegramSender.execute(
            SetMyCommands.builder()
                .commands(PERSONAL_COMMAND)
                .scope(new BotCommandScopeAllPrivateChats())
                .build()
        );
    }
    private static final List<BotCommand> PERSONAL_COMMAND = List.of(
        new BotCommand(CommandType.START.getText(), "Restart bot"),
        new BotCommand(CommandType.ABOUT.getText(), "Bot technical info")
    );
}
