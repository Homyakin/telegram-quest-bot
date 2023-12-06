package ru.homyakin.quest.bot.telegram.utils;

import java.io.InputStream;
import java.util.UUID;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class SendPhotoBuilder {
    private final SendPhoto.SendPhotoBuilder builder = SendPhoto.builder();

    private SendPhotoBuilder() {
    }

    public static SendPhotoBuilder builder() {
        final var instance = new SendPhotoBuilder();
        instance.builder.parseMode(ParseMode.HTML);
        return instance;
    }

    public SendPhotoBuilder text(String text) {
        this.builder.caption(text);
        return this;
    }

    public SendPhotoBuilder keyboard(ReplyKeyboard keyboard) {
        this.builder.replyMarkup(keyboard);
        return this;
    }

    public SendPhotoBuilder removeKeyboard() {
        this.builder.replyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());
        return this;
    }

    public SendPhotoBuilder photo(InputStream stream) {
        this.builder.photo(new InputFile(stream, UUID.randomUUID().toString()));
        return this;
    }

    public SendPhotoBuilder chatId(long chatId) {
        this.builder.chatId(chatId);
        return this;
    }

    public SendPhoto build() {
        return this.builder.build();
    }
}
