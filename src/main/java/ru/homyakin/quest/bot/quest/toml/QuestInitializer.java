package ru.homyakin.quest.bot.quest.toml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.utils.ResourceUtils;

@Component
public class QuestInitializer {
    private static final String QUEST_PATH = "quest";

    private static final Logger logger = LoggerFactory.getLogger(QuestInitializer.class);

    private final QuestDao questDao;

    public QuestInitializer(QuestDao questDao) {
        this.questDao = questDao;
    }

    public void initQuests() {
        logger.info("Filling quests");
        final var mapper = TomlMapper.builder().build();
        mapper.registerModule(new Jdk8Module());

        ResourceUtils.listAllFiles(QUEST_PATH)
            .stream()
            .forEach(path -> {
                logger.info("Parsing quest: " + path.getFileName());
                try (final var stream = Files.newInputStream(path)) {
                    questDao.save(extractClass(mapper, stream, QuestToml.class).toQuest());
                } catch (IOException e) {
                    logger.error("Error during parsing " + path.toString(), e);
                }
            });
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
