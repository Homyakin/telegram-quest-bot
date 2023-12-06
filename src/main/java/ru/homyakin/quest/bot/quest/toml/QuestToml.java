package ru.homyakin.quest.bot.quest.toml;

import java.util.List;
import java.util.Optional;
import ru.homyakin.quest.bot.quest.models.AnswerType;

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
        Optional<String> text, // Для типа NO_INLINE_BUTTON
        Optional<String> answerFilter, // Для типа USER_INPUT
        List<StageSelection> nextStages
    ) { }
}
