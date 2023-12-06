package ru.homyakin.quest.bot.quest.toml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.homyakin.quest.bot.quest.models.AnswerType;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;

public record QuestToml(
    String name,
    String description,
    boolean available,
    List<Stage> stages,
    String startStageName,
    List<StageSelection> nextStages
){
    public record Stage(
        String name,
        String text
    ) { }

    public record StageSelection(
        String name,
        AnswerType answerType,
        String value,
        List<StageSelection> nextStages
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
                toAvailableAnswers(nextStages)
            )
        );
    }

    private Stage getByName(String stageName) {
        return stages.stream().filter(stage -> stage.name.equals(stageName)).findFirst().orElseThrow();
    }

    private List<StageAvailableAnswer> toAvailableAnswers(List<StageSelection> selections) {
        if (selections == null) {
            return List.of();
        }
        final var answers = new ArrayList<StageAvailableAnswer>();
        selections.stream()
            .forEach(selection -> {
                final var stage = getByName(selection.name);
                answers.add(
                    new StageAvailableAnswer(
                        selection.name(),
                        selection.answerType(),
                        Optional.of(
                            new QuestStage(
                                stage.name(),
                                stage.text(),
                                toAvailableAnswers(selection.nextStages)
                            )
                        ),
                        selection.value
                    )
                );
            });
        return answers;
    }
}
