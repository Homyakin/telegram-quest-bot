package ru.homyakin.quest.bot.quest.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Repository;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestShort;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class QuestDaoMemory implements QuestDao {

    private static final Map<String, List<QuestStage>> quest2Stage = new HashMap<>();

    private static final List<Quest> quests = new ArrayList<>();

    @Override
    public Optional<Quest> getQuest(String questName) {
        return quests.stream().filter(quest1 -> quest1.name().equals(questName)).findFirst();
    }

    @Override
    public List<QuestShort> getAllQuest() {
        return quests.stream().map(Quest::toShort).toList();
    }

    @Override
    public Optional<QuestStage> getStage(String questName, String stageName) {
        return Optional.ofNullable(quest2Stage.get(questName))
            .flatMap(stages -> stages.stream().filter(stage -> Objects.equals(stage.name(), stageName)).findFirst());
    }

    @Override
    public void save(Quest quest) {
        quests.add(quest);
        final var stages = new ArrayList<QuestStage>();
        stages.add(quest.startStage());
        stages.addAll(getFromAvailableAnswers(quest.startStage().availableAnswers()));
        quest2Stage.put(quest.name(), stages);
    }

    private List<QuestStage> getFromAvailableAnswers(List<StageAvailableAnswer> answers) {
        final var stages = new ArrayList<QuestStage>();
        answers.stream().forEach(
            answer -> answer.nextStage().ifPresent(
                it -> {
                    stages.add(it);
                    stages.addAll(getFromAvailableAnswers(it.availableAnswers()));
                }
            )
        );
        return stages;
    }
}
