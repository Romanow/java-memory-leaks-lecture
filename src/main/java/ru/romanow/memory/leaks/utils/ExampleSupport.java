/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks.utils;

import java.util.Random;

public final class ExampleSupport {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new Random();

    private ExampleSupport() {
    }

    public static String randomString(int length) {
        final var result = new StringBuilder();
        int len = ALPHABET.length();
        for (int i = 0; i < length; i++) {
            result.append(ALPHABET.charAt(RANDOM.nextInt(len)));
        }
        return result.toString();
    }

    public static void logMemory() {
        long memory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Used memory: " + memory);
    }
}
