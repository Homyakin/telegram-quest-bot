package ru.homyakin.quest.bot.quest.dao;

import ru.homyakin.quest.bot.quest.models.*;

import java.util.List;
import java.util.Optional;

public interface QuestDao {
    Optional<Quest> getQuest(String questName);

    void saveUserAnswer(String questName, QuestStage questStage, StageAvailableAnswer availableAnswer, Long userId, UserAnswer answer);

    List<QuestShort> getAllQuest();
}
