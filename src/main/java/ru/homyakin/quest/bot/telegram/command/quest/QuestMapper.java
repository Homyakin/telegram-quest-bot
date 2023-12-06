package ru.homyakin.quest.bot.telegram.command.quest;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
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
            builder.keyboard(answersToKeyboard(questStage.availableAnswers()));
        }
        return builder.build();
    }

    private static ReplyKeyboard answersToKeyboard(List<StageAvailableAnswer> answers) {
        final var builder = ReplyKeyboardBuilder.builder();
        int answersInRow = 0;
        for (final var answer: answers) {
            if (answersInRow % 2 == 0) {
                builder.addRow();
            }
            builder.addButton(KeyboardButton.builder().text(answer.text()).build());
            answersInRow++;
        }
        return builder.build();
    }
}
