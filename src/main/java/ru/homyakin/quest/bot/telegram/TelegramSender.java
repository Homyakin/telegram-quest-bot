package ru.homyakin.quest.bot.telegram;

import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.homyakin.quest.bot.telegram.utils.TelegramMessage;

@Component
public class TelegramSender extends DefaultAbsSender {
    private static final Logger logger = LoggerFactory.getLogger(TelegramSender.class);
    private final String token;

    protected TelegramSender(TelegramBotConfig botConfig, DefaultBotOptions options) {
        super(options);
        this.token = botConfig.token();
    }

    public Either<TelegramError, Message> send(TelegramMessage message) {
        if (message.hasPhoto()) {
            return send(message.toSendPhoto());
        } else {
            return send(message.toSendText());
        }
    }

    private Either<TelegramError, Message> send(SendMessage sendMessage) {
        try {
            return Either.right(execute(sendMessage));
        } catch (Exception e) {
            logger.error(
                "Unable send message with text %s to %s".formatted(sendMessage.getText(), sendMessage.getChatId()), e
            );
            return Either.left(new TelegramError.Internal(e.getMessage()));
        }
    }

    private Either<TelegramError, Message> send(SendPhoto sendPhoto) {
        try {
            return Either.right(execute(sendPhoto));
        } catch (Exception e) {
            logger.error(
                "Unable send photo with text %s to %s".formatted(sendPhoto.getCaption(), sendPhoto.getChatId()), e
            );
            return Either.left(new TelegramError.Internal(e.getMessage()));
        }
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
