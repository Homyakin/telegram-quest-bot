package ru.homyakin.quest.bot.quest.services.impl;

import org.springframework.stereotype.Service;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.dao.UserDao;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestShort;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;
import ru.homyakin.quest.bot.quest.models.UserAnswer;
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
                    userDao.setQuestStage(questName, userId, quest.startStage());
                    return quest.startStage();
                }
        );
    }

    @Override
    public Optional<QuestStage> processStage(String questName, Long userId, UserAnswer answer) {
        return getQuest(questName)
                .flatMap(quest -> {
                            QuestStage questStage = userDao.getUserCurrentStage(questName, userId)
                                    .flatMap(stageName -> questDao.getStage(questName, stageName))
                                    .orElseThrow();
                            if (questStage.isFinal()) {
                                return Optional.empty();
                            }
                            StageAvailableAnswer availableAnswer = matchAnswer(questStage, answer).orElseThrow();
                            userDao.saveUserAnswer(questName, questStage, availableAnswer, userId, answer);
                            return availableAnswer.nextStage();
                        }
                );
    }

    @Override
    public Optional<QuestStage> getUserCurrentStage(String questName, Long userId) {
        return userDao.getUserCurrentStage(questName, userId)
                .flatMap(stageName -> questDao.getStage(questName, stageName));
    }

    @Override
    public Optional<Quest> getUserQuest(Long userId) {
        return userDao.getUserCurrentQuest(userId)
                .flatMap(this::getQuest);
    }

    @Override
    public List<QuestShort> getAllQuest() {
        return questDao.getAllQuest();
    }

    @Override
    public void clearUserState(Long userId) {
        userDao.clear(userId);
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
