package ru.homyakin.quest.bot.locale.common;

public class CommonLocalization {
    private static CommonResource resource;

    public static void add(CommonResource resource) {
        CommonLocalization.resource = resource;
    }

    public static String start() {
        return resource.start();
    }
}