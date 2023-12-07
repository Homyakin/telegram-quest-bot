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
) {
    public record Stage(
        String name,
        String text,
        Optional<String> photoPath,
        List<StageSelection> nextStages
    ) {
    }

    public record StageSelection(
        String name,
        AnswerType answerType,
        String value
    ) {
    }

    public Quest toQuest() {
        return new Quest(
            name,
            description,
            available,
            startStageName,
            stages.stream()
                .map(stage -> new QuestStage(
                    stage.name,
                    stage.text,
                    toAvailableAnswers(name, stage.name(), stage.nextStages()),
                    stage.photoPath
                ))
                .toList()
        );
    }

    private Stage getByName(String stageName) {
        return stages.stream().filter(stage -> stage.name.equals(stageName)).findFirst().orElseThrow();
    }

    private List<StageAvailableAnswer> toAvailableAnswers(
        String questName,
        String stageName,
        List<StageSelection> selections
    ) {
        if (selections == null) {
            return new ArrayList<>();
        }
        final var answers = new ArrayList<StageAvailableAnswer>();
        selections.stream()
            .forEach(selection -> {
                answers.add(
                    new StageAvailableAnswer(
                        selection.answerType(),
                        selection.name(),
                        selection.value
                    )
                );
            });
        return answers;
    }
}
