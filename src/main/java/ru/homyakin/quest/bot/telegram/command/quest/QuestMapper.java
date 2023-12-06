package ru.homyakin.quest.bot.telegram.command.quest;

import java.util.List;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import ru.homyakin.quest.bot.quest.models.AnswerType;
import ru.homyakin.quest.bot.quest.models.QuestShort;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;
import ru.homyakin.quest.bot.telegram.utils.ReplyKeyboardBuilder;
import ru.homyakin.quest.bot.telegram.utils.TelegramMessage;

public class QuestMapper {
    public static TelegramMessage questStageToTelegramMessage(QuestStage questStage, long chatId) {
        final var builder = TelegramMessage
            .builder()
            .chatId(chatId)
            .text(questStage.text());
        if (questStage.isFinal()) {
            builder.removeKeyboard();
        } else {
            builder.keyboard(answersToKeyboard(questStage.availableAnswers()).orElse(null));
        }
        return builder.build();
    }

    public static ReplyKeyboard questsToKeyboard(List<QuestShort> quests) {
        // TODO обработка, если нет доступных квестов
        final var builder = ReplyKeyboardBuilder.builder();
        quests.stream()
            .filter(QuestShort::available)
            .forEach(quest -> {
                builder.addRow();
                builder.addButton(KeyboardButton.builder().text(quest.name()).build());
            });
        return builder.build();
    }

    private static Optional<ReplyKeyboard> answersToKeyboard(List<StageAvailableAnswer> answers) {
        final var buttonAnswers = answers.stream()
            .filter(answer -> answer.answerType() == AnswerType.NO_INLINE_BUTTON)
            .toList();
        if (buttonAnswers.size() == 0) {
            return Optional.empty();
        }
        final var builder = ReplyKeyboardBuilder.builder();
        int answersInRow = 0;
        for (final var answer: buttonAnswers) {
            if (answersInRow % 2 == 0) {
                builder.addRow();
            }
            builder.addButton(KeyboardButton.builder().text(answer.text().orElseThrow()).build());
            answersInRow++;
        }
        return Optional.of(builder.build());
    }
}
