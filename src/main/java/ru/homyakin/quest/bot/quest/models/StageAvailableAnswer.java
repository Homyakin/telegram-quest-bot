package ru.homyakin.quest.bot.quest.models;


public record StageAvailableAnswer(
        AnswerType answerType,
        String nextStageName,
        String value
) {
    public boolean isMatchUserAnswer(UserAnswer userAnswer) {
        return switch (answerType) {
            case NO_INLINE_BUTTON -> userAnswer.text().equalsIgnoreCase(value);
            case USER_INPUT -> userAnswer.text().matches(value);
        };
    }
}
