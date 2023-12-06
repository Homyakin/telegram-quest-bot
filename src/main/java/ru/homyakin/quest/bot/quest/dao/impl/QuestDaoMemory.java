package ru.homyakin.quest.bot.quest.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Repository;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.models.AnswerType;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestShort;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class QuestDaoMemory implements QuestDao {

    private static final String questName = "testDemo";

    private static final QuestStage finalStage = new QuestStage(
        "finalStage",
        "Вот и всё, ребята:)",
        Collections.emptyList()
    );

    private static final StageAvailableAnswer sSt1A = new StageAvailableAnswer(
        "firstStageFirstAnswer",
        AnswerType.USER_INPUT,
        Optional.of(finalStage),
        ".*"
    );

    private static final QuestStage oneSecondStage = new QuestStage(
        "secondStage1",
        "Вы выбрали первый вариант. Молодцы, а теперь оставьте своё мнение о данном проекте:",
        List.of(sSt1A)
    );

    private static final StageAvailableAnswer sSt2A = new StageAvailableAnswer(
        "firstStageSecondAnswer",
        AnswerType.USER_INPUT,
        Optional.of(finalStage),
        "^(?:[1-9]|10)$"
    );

    private static final QuestStage twoSecondStage = new QuestStage(
        "secondStage2",
        "Вы выбрали второй вариант, неплохо. Оцените ваш опыт от 1 до 10",
        List.of(sSt2A)
    );

    private static final StageAvailableAnswer fSt1A = new StageAvailableAnswer(
        "firstStageFirstAnswer",
        AnswerType.NO_INLINE_BUTTON,
        Optional.of(oneSecondStage),
        "Вариант 1"
    );

    private static final StageAvailableAnswer fSt2A = new StageAvailableAnswer(
        "firstStageSecondAnswer",
        AnswerType.NO_INLINE_BUTTON,
        Optional.of(twoSecondStage),
        "Вариант 2"
    );

    private static final QuestStage firstStage = new QuestStage(
        "",
        "Вас приветствует демонстрационный опросник, выберите один из двух вариантов",
        List.of(fSt1A, fSt2A)
    );

    private static final Quest quest = new Quest(
        questName,
        "Демонстрационный опросник",
        true,
        firstStage
    );

    private static final Map<String, List<QuestStage>> quest2Stage = new HashMap<>() {{
        put(questName, List.of(firstStage, oneSecondStage, twoSecondStage));
    }};

    private static final List<Quest> quests = new ArrayList<>() {{
        add(quest);
    }};

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
