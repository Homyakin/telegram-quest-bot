package ru.homyakin.quest.bot.telegram;

public sealed interface TelegramError {
    record Internal(String message) implements TelegramError {}
}
