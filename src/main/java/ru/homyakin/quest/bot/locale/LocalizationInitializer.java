package ru.homyakin.quest.bot.locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import java.io.File;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.homyakin.quest.bot.locale.common.CommonLocalization;
import ru.homyakin.quest.bot.locale.common.CommonResource;
import ru.homyakin.quest.bot.utils.ResourceUtils;

public class LocalizationInitializer {
    private static final String LOCALIZATION_PATH = "localization";
    private static final String COMMON_PATH = File.separator + "common.toml";

    private static final Logger logger = LoggerFactory.getLogger(LocalizationInitializer.class);

    public static void initLocale() {
        logger.info("Filling localization");
        final var mapper = TomlMapper.builder().build();
        ResourceUtils.getResourcePath(LOCALIZATION_PATH + COMMON_PATH)
            .ifPresent(it -> CommonLocalization.add(extractClass(mapper, it, CommonResource.class)));
        logger.info("Localization loaded");
    }

    private static <T> T extractClass(ObjectMapper mapper, InputStream stream, Class<T> clazz) {
        try {
            return mapper.readValue(stream, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Can't parse locale for " + clazz.getSimpleName(), e);
        }
    }
}
