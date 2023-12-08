package ru.homyakin.quest.bot.quest.toml;

import ru.homyakin.quest.bot.quest.models.AnswerType;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;

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
        public QuestStage toQuestStage() {
            return new QuestStage(
                name,
                text,
                toAvailableAnswers(nextStages()),
                photoPath
            );
        }

        private List<StageAvailableAnswer> toAvailableAnswers(List<StageSelection> selections) {
            if (selections == null) {
                return List.of();
            }
            return selections.stream().map(StageSelection::toStageAvailableAnswer).toList();
        }
    }

    public record StageSelection(
        String name,
        AnswerType answerType,
        String value
    ) {
        public StageAvailableAnswer toStageAvailableAnswer() {
            return new StageAvailableAnswer(
                answerType,
                name,
                value
            );
        }
    }

    public Quest toQuest() {
        return new Quest(
            name,
            description,
            available,
            startStageName,
            stages.stream().map(Stage::toQuestStage).toList()
        );
    }
}
