package ru.homyakin.quest.bot.quest.services;

import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.UserAnswer;

import java.util.Optional;

public interface QuestProcessor {

    // empty если нет такого квеста, нужно вызывать при старте либо перезапуске квеста
    Optional<QuestStage> startQuest(String questName, Long userId);

    // empty если нет такого квеста
    Optional<QuestStage> processStage(String questName, Long userId, UserAnswer answer);

    // empty если нет такого квеста или квест не начат
    Optional<QuestStage> getUserCurrentStage(String questName, Long userId);

    Optional<Quest> getUserQuest(Long userId);
}
