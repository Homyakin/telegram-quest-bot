package ru.homyakin.quest.bot.telegram.utils;

import java.io.InputStream;
import java.util.UUID;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public record TelegramMessage(
    String text,
    ReplyKeyboard keyboard,
    boolean removeKeyboard,
    long chatId,
    InputStream photo
) {

    public boolean hasPhoto() {
        return photo != null;
    }

    public SendPhoto toSendPhoto() {
        if (photo == null) {
            throw new IllegalStateException("Photo must present for send photo");
        }
        final var builder = SendPhoto
            .builder()
            .caption(text)
            .replyMarkup(keyboard)
            .chatId(chatId)
            .photo(new InputFile(photo, UUID.randomUUID().toString()));
        if (removeKeyboard) {
            builder.replyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());
        }
        return builder.build();
    }

    public SendMessage toSendText() {
        final var builder = SendMessage
            .builder()
            .disableWebPagePreview(true)
            .text(text)
            .replyMarkup(keyboard)
            .chatId(chatId);
        if (removeKeyboard) {
            builder.replyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());
        }
        return builder.build();
    }

    public static TelegramMessageBuilder builder() {
        final var instance = new TelegramMessageBuilder();
        return instance;
    }

    public static class TelegramMessageBuilder {

        private String text;
        private ReplyKeyboard keyboard;
        private boolean removeKeyboard = false;
        private long chatId;
        private InputStream photo;

        private TelegramMessageBuilder() {
        }

        public TelegramMessageBuilder text(String text) {
            this.text = text;
            return this;
        }

        public TelegramMessageBuilder keyboard(ReplyKeyboard keyboard) {
            this.keyboard = keyboard;
            return this;
        }

        public TelegramMessageBuilder removeKeyboard() {
            this.removeKeyboard = true;
            this.keyboard = null;
            return this;
        }

        public TelegramMessageBuilder chatId(long chatId) {
            this.chatId = chatId;
            return this;
        }

        public TelegramMessageBuilder photo(InputStream stream) {
            this.photo = stream;
            return this;
        }

        public TelegramMessage build() {
            if (removeKeyboard && keyboard != null) {
                throw new IllegalStateException("Keyboard is not null, and removeKeyboard is true");
            }
            return new TelegramMessage(
                text,
                keyboard,
                removeKeyboard,
                chatId,
                photo
            );
        }
    }
}
