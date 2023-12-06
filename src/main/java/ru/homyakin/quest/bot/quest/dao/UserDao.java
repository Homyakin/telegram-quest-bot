package ru.homyakin.quest.bot.quest.dao;

import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestStage;

import java.util.Optional;

public interface UserDao {

    void resetQuest(String questName, Long userId);

    Optional<Quest> getUserCurrentQuest(Long userId);

    Optional<QuestStage> getUserCurrentStage(String questName, Long userId);
}
