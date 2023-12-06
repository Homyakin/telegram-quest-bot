package ru.homyakin.quest.bot.quest.dao;

import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestShort;
import ru.homyakin.quest.bot.quest.models.QuestStage;

import java.util.List;
import java.util.Optional;

public interface QuestDao {
    Optional<Quest> getQuest(String questName);

    List<QuestShort> getAllQuest();

    Optional<QuestStage> getStage(String questName, String stageName);

    void save(Quest quest);
}
