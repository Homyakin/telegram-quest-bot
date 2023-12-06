package ru.homyakin.quest.bot.quest.models;

import java.util.Optional;

public record StageAvailableAnswer(String name, String text, AnswerType answerType, QuestStage nextStage, Optional<String> answerFilter) { }
