package ru.homyakin.quest.bot.telegram.utils;

import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramUtils {

    public static boolean needToProcessUpdate(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().isUserMessage();
        }
        return false;
    }
}
