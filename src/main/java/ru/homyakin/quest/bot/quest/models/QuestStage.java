package ru.homyakin.quest.bot.quest.models;

import java.util.List;

public record QuestStage(String name, String text, List<StageAvailableAnswer> availableAnswers) {
    public boolean isFinal() {
        return availableAnswers.size() == 0;
    }
}
