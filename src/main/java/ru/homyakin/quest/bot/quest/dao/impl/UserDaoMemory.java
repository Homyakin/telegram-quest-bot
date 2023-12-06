package ru.homyakin.quest.bot.quest.dao.impl;

import org.springframework.stereotype.Repository;
import ru.homyakin.quest.bot.quest.dao.UserDao;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;
import ru.homyakin.quest.bot.quest.models.UserAnswer;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserDaoMemory implements UserDao {

    private final ConcurrentHashMap<Long, String> user2Quest = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, String> user2stage = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>>
            user2Answers = new ConcurrentHashMap<>();

    @Override
    public void setQuestStage(String questName, Long userId, QuestStage questStage) {
        user2Quest.put(userId, questName);
        user2stage.put(userId, questStage.name());
    }

    @Override
    public void saveUserAnswer(
            String questName,
            QuestStage questStage,
            StageAvailableAnswer availableAnswer,
            Long userId,
            UserAnswer answer
    ) {
        var userQuest = user2Answers.get(userId);
        if (userQuest == null) {
            userQuest = new ConcurrentHashMap<>();
            var userStages = new ConcurrentHashMap<String, String>();
            userStages.put(questStage.name(), answer.text());
            userQuest.put(questName, userStages);
            user2Answers.put(userId, userQuest);
        } else {
            var userStages = userQuest.get(questName);
            if (userStages == null) {
                userStages = new ConcurrentHashMap<>();
                userStages.put(questStage.name(), answer.text());
                userQuest.put(questName, userStages);
            } else {
                userStages.put(questStage.name(), answer.text());
            }
        }
    }

    @Override
    public Optional<String> getUserCurrentQuest(Long userId) {
        return Optional.ofNullable(user2Quest.get(userId));
    }

    @Override
    public Optional<String> getUserCurrentStage(String questName, Long userId) {
        return  Optional.ofNullable(user2stage.get(userId));
    }

    @Override
    public void clear(Long userId) {
        user2Quest.remove(userId);
        user2stage.remove(userId);
    }
}
