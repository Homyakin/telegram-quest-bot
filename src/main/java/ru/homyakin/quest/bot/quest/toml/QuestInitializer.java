package ru.homyakin.quest.bot.quest.toml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.File;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.utils.ResourceUtils;

@Component
public class QuestInitializer {
    private static final String QUEST_PATH = "quest";
    private static final String DEMO_PATH = File.separator + "demo_quest.toml";

    private static final Logger logger = LoggerFactory.getLogger(QuestInitializer.class);

    private final QuestDao questDao;

    public QuestInitializer(QuestDao questDao) {
        this.questDao = questDao;
    }

    public void initQuests() {
        logger.info("Filling quests");
        final var mapper = TomlMapper.builder().build();
        mapper.registerModule(new Jdk8Module());
        final var quest = ResourceUtils.getResourcePath(QUEST_PATH + DEMO_PATH).map(
            stream -> extractClass(mapper, stream, Quest.class)
        ).orElseThrow();
        questDao.save(quest);
        logger.info(
            ResourceUtils.getResourcePath(QUEST_PATH + DEMO_PATH).map(
                stream -> extractClass(mapper, stream, Quest.class)
            ).map(Quest::toString).orElse(null)
        );
        logger.info("Quests loaded");
    }

    private static <T> T extractClass(ObjectMapper mapper, InputStream stream, Class<T> clazz) {
        try {
            return mapper.readValue(stream, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Can't parse quest for " + clazz.getSimpleName(), e);
        }
    }
}
