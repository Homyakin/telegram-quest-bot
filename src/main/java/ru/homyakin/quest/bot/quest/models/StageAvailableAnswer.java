package ru.homyakin.quest.bot.quest.models;

import java.util.Optional;

public record StageAvailableAnswer(
        String name,
        AnswerType answerType,
        Optional<QuestStage> nextStage,
        String value
) {
    public boolean isMatchUserAnswer(UserAnswer userAnswer) {
        return switch (answerType) {
            case NO_INLINE_BUTTON -> userAnswer.text().equalsIgnoreCase(value);
            case USER_INPUT -> userAnswer.text().matches(value);
        };
    }
}
