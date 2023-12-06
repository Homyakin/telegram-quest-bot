package ru.homyakin.quest.bot.telegram.utils;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class SendMessageBuilder {
    private final SendMessage.SendMessageBuilder builder = SendMessage.builder();

    private SendMessageBuilder() {
    }

    public static SendMessageBuilder builder() {
        final var instance = new SendMessageBuilder();
        instance.builder
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true);
        return instance;
    }

    public SendMessageBuilder text(String text) {
        this.builder.text(text);
        return this;
    }

    public SendMessageBuilder keyboard(ReplyKeyboard keyboard) {
        this.builder.replyMarkup(keyboard);
        return this;
    }

    public SendMessageBuilder removeKeyboard() {
        this.builder.replyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());
        return this;
    }

    public SendMessageBuilder chatId(long chatId) {
        this.builder.chatId(chatId);
        return this;
    }

    public SendMessage build() {
        return this.builder.build();
    }
}
