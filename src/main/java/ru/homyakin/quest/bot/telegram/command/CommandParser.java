package ru.homyakin.quest.bot.telegram.command;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.homyakin.quest.bot.telegram.command.tech.Start;

@Component
public class CommandParser {

    public Optional<Command> parse(Update update) {
        if (!update.hasMessage()) {
            return Optional.empty();
        }
        final var message = update.getMessage();
        if (!message.hasText()) {
            return Optional.empty();
        }
        if (message.isUserMessage()) {
            return parsePrivateMessage(message);
        }
        return Optional.empty();
    }

    private Optional<Command> parsePrivateMessage(Message message) {
        return CommandType.getFromString(message.getText())
            .map(commandType -> switch (commandType) {
                case START -> Start.from(message);
            });

        // TODO проверить, если пользователь в квесте, вызвать QuestNextNodeExecutor
        // TODO если не в квесте -> QuestSelectionExecutor
    }
}
