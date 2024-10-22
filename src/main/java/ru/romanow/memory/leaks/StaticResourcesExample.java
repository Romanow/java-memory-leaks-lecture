/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.util.*;

import static ru.romanow.memory.leaks.utils.ExampleSupport.logMemory;
import static ru.romanow.memory.leaks.utils.ExampleSupport.randomString;

public class StaticResourcesExample {
    private static final Map<UUID, String> cache = new HashMap<>();

    public static void main(String[] args) {
        logMemory();

        var scanner = new Scanner(System.in);
        while (true) {
            cache.put(UUID.randomUUID(), randomString(128 * 1024));

            logMemory();
            System.out.println("Continue? (Ctrl+C for exit)");
            scanner.nextLine();
        }
    }
}
