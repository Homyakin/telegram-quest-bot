package ru.homyakin.quest.bot;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.homyakin.quest.bot.locale.LocalizationInitializer;
import ru.homyakin.quest.bot.quest.toml.QuestInitializer;
import ru.homyakin.quest.bot.telegram.TelegramUpdateReceiver;

@SpringBootApplication
@EnableScheduling
@Theme(value = "quest.bot")
public class Application implements CommandLineRunner, AppShellConfigurator {

    private final TelegramUpdateReceiver telegramUpdateReceiver;
    private final QuestInitializer questInitializer;

    public Application(TelegramUpdateReceiver telegramUpdateReceiver, QuestInitializer questInitializer) {
        this.questInitializer = questInitializer;
        this.telegramUpdateReceiver = telegramUpdateReceiver;
    }

    @Override
    public void run(String... args) throws Exception {
        LocalizationInitializer.initLocale();
        questInitializer.initQuests();
        final var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramUpdateReceiver);
    }

    public static void main(String[] args) {
        // test commit
        SpringApplication.run(Application.class, args);
    }
}

