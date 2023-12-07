package ru.homyakin.quest.bot.locale.common;

public class CommonLocalization {
    private static CommonResource resource;

    public static void add(CommonResource resource) {
        CommonLocalization.resource = resource;
    }

    public static String start() {
        return resource.start();
    }

    public static String questNotFound() {
        return resource.questNotFound();
    }

    public static String questEnding() {
        return resource.questEnding();
    }

    public static String about() {
        return resource.about();
    }
}
