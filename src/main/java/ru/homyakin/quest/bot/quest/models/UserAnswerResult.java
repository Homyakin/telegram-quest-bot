package ru.homyakin.quest.bot.quest.models;

import java.time.LocalDateTime;

public record UserAnswerResult(
        Long userId,
        String questName,
        String stageName,
        String answer,
        LocalDateTime answerTime
) {
}
