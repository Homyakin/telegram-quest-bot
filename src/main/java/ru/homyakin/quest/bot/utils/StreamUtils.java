package ru.homyakin.quest.bot.utils;

import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamUtils {
    private static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);

    public static void closeInputStreamIgnoreException(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            logger.error("Unable to close input stream", e);
        }
    }
}
