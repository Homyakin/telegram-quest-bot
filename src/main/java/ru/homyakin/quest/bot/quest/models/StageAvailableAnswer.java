package ru.homyakin.quest.bot.quest.models;

import java.util.Optional;

public record StageAvailableAnswer(
        String name,
        Optional<String> text,
        AnswerType answerType,
        Optional<QuestStage> nextStage,
        Optional<String> answerFilter
) {
    public boolean isMatchUserAnswer(UserAnswer userAnswer) {
        return switch (answerType) {
            case NO_INLINE_BUTTON -> userAnswer.text().equalsIgnoreCase(text.orElseThrow());
            case USER_INPUT -> userAnswer.text().matches(answerFilter.orElseThrow());
        };
    }
}
