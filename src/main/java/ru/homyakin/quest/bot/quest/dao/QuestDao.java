package ru.homyakin.quest.bot.quest.dao;

import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;
import ru.homyakin.quest.bot.quest.models.UserAnswer;

import java.util.Optional;

public interface QuestDao {
    Optional<Quest> getQuest(String questName);

    void saveUserAnswer(String questName, QuestStage questStage, StageAvailableAnswer availableAnswer, Long userId, UserAnswer answer);
}
