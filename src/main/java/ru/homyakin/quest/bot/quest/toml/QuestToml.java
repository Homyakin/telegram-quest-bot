package ru.homyakin.quest.bot.quest.toml;

import ru.homyakin.quest.bot.quest.models.AnswerType;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public record QuestToml(
    String name,
    String description,
    boolean available,
    List<Stage> stages,
    String startStageName
){
    public record Stage(
        String name,
        String text,
        Optional<String> photoPath,
        List<StageSelection> nextStages
    ) { }

    public record StageSelection(
        String name,
        AnswerType answerType,
        String value
    ) { }

    public Quest toQuest() {
        final var startStage = getByName(startStageName);
        return new Quest(
            name,
            description,
            available,
            new QuestStage(
                startStage.name(),
                startStage.text(),
                toAvailableAnswers(name, startStage.name(), startStage.nextStages(), new HashMap<>()),
                startStage.photoPath()
            )
        );
    }

    private Stage getByName(String stageName) {
        return stages.stream().filter(stage -> stage.name.equals(stageName)).findFirst().orElseThrow();
    }

    private List<StageAvailableAnswer> toAvailableAnswers(
        String questName,
        String stageName,
        List<StageSelection> selections,
        HashMap<String, QuestStage> mappedStages
    ) {
        if (selections == null) {
            return new ArrayList<>();
        }
        final var answers = new ArrayList<StageAvailableAnswer>();
        selections.stream()
            .forEach(selection -> {
                final var stage = getByName(selection.name);
                final var questStage = Optional.ofNullable(mappedStages.get(stage.name))
                    .orElseGet(() -> new QuestStage(
                        stage.name(),
                        stage.text(),
                        toAvailableAnswers(questName, stage.name(), stage.nextStages(), mappedStages),
                        stage.photoPath()
                    ));
                mappedStages.put(questStage.name(), questStage);
                answers.add(
                    new StageAvailableAnswer(
                            // надо что бы имя ответа было уникальным :(
                            questName + "-" + stageName + "-" + selection.name(),
                        selection.answerType(),
                        Optional.of(questStage),
                        selection.value
                    )
                );
            });
        return answers;
    }
}
