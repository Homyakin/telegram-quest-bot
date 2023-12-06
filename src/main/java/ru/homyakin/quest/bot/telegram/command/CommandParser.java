package ru.homyakin.quest.bot.telegram.command;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.homyakin.quest.bot.quest.services.QuestProcessor;
import ru.homyakin.quest.bot.telegram.command.quest.QuestNextStage;
import ru.homyakin.quest.bot.telegram.command.quest.QuestSelection;
import ru.homyakin.quest.bot.telegram.command.tech.Start;

@Component
public class CommandParser {

    private final QuestProcessor questProcessor;

    public CommandParser(QuestProcessor questProcessor) {
        this.questProcessor = questProcessor;
    }

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
            .<Command>map(commandType -> switch (commandType) {
                case START -> Start.from(message);
            })
            .or(() -> questProcessor
                .getUserQuest(message.getFrom().getId())
                .map(quest -> QuestNextStage.from(message, quest))
            )
            .or(() -> Optional.of(QuestSelection.from(message)));
    }
}
