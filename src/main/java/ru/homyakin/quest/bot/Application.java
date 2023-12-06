package ru.homyakin.quest.bot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.homyakin.quest.bot.locale.LocalizationInitializer;
import ru.homyakin.quest.bot.telegram.TelegramUpdateReceiver;

@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {

    private final TelegramUpdateReceiver telegramUpdateReceiver;

    public Application(TelegramUpdateReceiver telegramUpdateReceiver) {
        LocalizationInitializer.initLocale();
        this.telegramUpdateReceiver = telegramUpdateReceiver;
    }

    @Override
    public void run(String... args) throws Exception {
        final var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramUpdateReceiver);
    }

    public static void main(String[] args) {
        // test commit
        SpringApplication.run(Application.class, args);
    }
}

