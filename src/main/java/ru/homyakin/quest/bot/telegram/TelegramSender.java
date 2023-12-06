package ru.homyakin.quest.bot.telegram;

import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class TelegramSender extends DefaultAbsSender {
    private static final Logger logger = LoggerFactory.getLogger(TelegramSender.class);
    private final String token;

    protected TelegramSender(TelegramBotConfig botConfig, DefaultBotOptions options) {
        super(options);
        this.token = botConfig.token();
    }

    public Either<TelegramError, Message> send(SendMessage sendMessage) {
        try {
            return Either.right(execute(sendMessage));
        } catch (Exception e) {
            logger.error(
                "Unable send message with text %s to %s".formatted(sendMessage.getText(), sendMessage.getChatId()), e
            );
            return Either.left(new TelegramError.Internal(e.getMessage()));
        }
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
