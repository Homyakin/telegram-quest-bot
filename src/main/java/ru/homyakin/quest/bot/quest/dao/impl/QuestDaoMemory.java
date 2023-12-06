package ru.homyakin.quest.bot.quest.dao.impl;

import org.springframework.stereotype.Repository;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.models.AnswerType;
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

    private static final String questName = "testDemo";

    private static final StageAvailableAnswer sSt1A = new StageAvailableAnswer(
            "firstStageFirstAnswer",
            Optional.empty(),
            AnswerType.USER_INPUT,
            Optional.empty(),
            Optional.of("[a-z]")
    );

    private static final QuestStage oneSecondStage = new QuestStage(
            "secondStage1",
            "Вы выбрали первый вариант. Молодцы, а теперь оставьте своё мнение о данном проекте:",
            List.of(sSt1A)
    );

    private static final StageAvailableAnswer sSt2A = new StageAvailableAnswer(
            "firstStageSecondAnswer",
            Optional.of("Вариант 2"),
            AnswerType.USER_INPUT,
            Optional.empty(),
            Optional.of("[0-9]")
    );

    private static final QuestStage twoSecondStage = new QuestStage(
            "secondStage2",
            "Вы выбрали второй вариант, неплохо. Оцените ваш опыт от 1 до 10",
            List.of(sSt2A)
    );

    private static final StageAvailableAnswer fSt1A = new StageAvailableAnswer(
            "firstStageFirstAnswer",
            Optional.of("Вариант 1"),
            AnswerType.NO_INLINE_BUTTON,
            Optional.of(oneSecondStage),
            Optional.of("Вариант 1")
    );

    private static final StageAvailableAnswer fSt2A = new StageAvailableAnswer(
            "firstStageSecondAnswer",
            Optional.of("Вариант 2"),
            AnswerType.NO_INLINE_BUTTON,
            Optional.of(twoSecondStage),
            Optional.of("Вариант 2")
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

    private static final Map<String, List<QuestStage>> quest2Stage = Map.of(
            questName, List.of(firstStage, oneSecondStage, twoSecondStage)
    );

    @Override
    public Optional<Quest> getQuest(String questName) {
        if (quest.name().equals(questName)) {
            return Optional.of(quest);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<QuestShort> getAllQuest() {
        return List.of(quest.toShort());
    }

    @Override
    public Optional<QuestStage> getStage(String questName, String stageName) {
        return Optional.ofNullable(quest2Stage.get(questName))
                .flatMap(stages -> stages.stream().filter(stage -> Objects.equals(stage.name(), stageName)).findFirst());
    }
}
