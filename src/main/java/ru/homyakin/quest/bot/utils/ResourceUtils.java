package ru.homyakin.quest.bot.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class ResourceUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    public static Optional<InputStream> getResource(String path) {
        try {
            return Optional.of(new ClassPathResource(path).getInputStream());
        } catch (IOException e) {
            logger.error("Error during getting resource " + path, e);
            return Optional.empty();
        }
    }

    public static List<Path> listAllFiles(String path) {
        try {
            final var directory = Paths.get(new ClassPathResource(path).getURI());
            return Files.list(directory)
                .filter(Files::isRegularFile)
                .toList();
        } catch (IOException e) {
            logger.error("Error during listing resource " + path, e);
            return List.of();
        }
    }
}
