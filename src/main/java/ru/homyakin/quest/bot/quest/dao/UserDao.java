package ru.homyakin.quest.bot.quest.dao;

import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;
import ru.homyakin.quest.bot.quest.models.UserAnswer;
import ru.homyakin.quest.bot.quest.models.UserAnswerResult;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    void setQuestStage(String questName, Long userId, QuestStage questStage);

    void saveUserAnswer(String questName, QuestStage questStage, StageAvailableAnswer availableAnswer, Long userId, UserAnswer answer);

    Optional<String> getUserCurrentQuest(Long userId);

    Optional<String> getUserCurrentStage(String questName, Long userId);

    void clear(Long userId);

    List<UserAnswerResult> getAnswers();
}
