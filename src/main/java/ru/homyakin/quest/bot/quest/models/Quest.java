package ru.homyakin.quest.bot.quest.models;

import java.util.List;

public record Quest(
    String name,
    String description,
    boolean available,
    String startStageName,
    List<QuestStage> stages
) {
    public QuestShort toShort() {
        return new QuestShort(name, description, available);
    }


}
