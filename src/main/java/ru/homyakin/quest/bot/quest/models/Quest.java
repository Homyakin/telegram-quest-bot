package ru.homyakin.quest.bot.quest.models;

public record Quest(String name, String description, boolean available, QuestStage startStage) {
    public QuestShort toShort() {
        return new QuestShort(name, description, available);
    }

    @Override
    public String toString() {
        return "Quest[name=%s, description=%s, available=%s]".formatted(name, description, available);
    }
}
