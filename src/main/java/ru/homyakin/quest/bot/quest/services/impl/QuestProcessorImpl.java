package ru.homyakin.quest.bot.quest.services.impl;

import org.springframework.stereotype.Service;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.dao.UserDao;
import ru.homyakin.quest.bot.quest.models.*;
import ru.homyakin.quest.bot.quest.services.QuestProcessor;

import java.util.List;
import java.util.Optional;

@Service
public class QuestProcessorImpl implements QuestProcessor {

    private final QuestDao questDao;
    private final UserDao userDao;

    public QuestProcessorImpl(QuestDao questDao, UserDao userDao) {
        this.questDao = questDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<QuestStage> startQuest(String questName, Long userId) {
        return getQuest(questName)
                .map(quest -> {
                    userDao.resetQuest(questName, userId);
                    return quest.startStage();
                }
        );
    }

    @Override
    public Optional<QuestStage> processStage(String questName, Long userId, UserAnswer answer) {
        return getQuest(questName)
                .map(quest -> {
                            QuestStage questStage = userDao.getUserCurrentStage(questName, userId).orElseThrow();
                            StageAvailableAnswer availableAnswer = matchAnswer(questStage, answer).orElseThrow();
                            questDao.saveUserAnswer(questName, questStage, availableAnswer, userId, answer);
                            return availableAnswer.nextStage();
                        }
                );
    }

    @Override
    public Optional<QuestStage> getUserCurrentStage(String questName, Long userId) {
        return userDao.getUserCurrentStage(questName, userId);
    }

    @Override
    public Optional<Quest> getUserQuest(Long userId) {
        return userDao.getUserCurrentQuest(userId);
    }

    @Override
    public List<QuestShort> getAllQuest() {
        return questDao.getAllQuest();
    }

    private Optional<StageAvailableAnswer> matchAnswer(QuestStage stage, UserAnswer answer) {
        return stage.availableAnswers().stream()
                .filter(ans -> ans.isMatchUserAnswer(answer))
                .findFirst();
    }

    private Optional<Quest> getQuest(String questName) {
        return questDao.getQuest(questName)
                .filter(Quest::available);
    }
}
