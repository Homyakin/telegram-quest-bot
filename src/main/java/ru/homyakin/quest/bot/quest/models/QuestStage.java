package ru.homyakin.quest.bot.quest.models;

import java.util.List;
import java.util.Optional;

public record QuestStage(
    String name,
    String text,
    List<StageAvailableAnswer> availableAnswers,
    Optional<String> photoPath
) {
    public boolean isFinal() {
        return availableAnswers.size() == 0;
    }
}
