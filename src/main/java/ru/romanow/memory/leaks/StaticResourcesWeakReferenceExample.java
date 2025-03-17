/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import static java.util.stream.IntStream.range;
import static ru.romanow.memory.leaks.utils.ExampleSupport.logMemory;
import static ru.romanow.memory.leaks.utils.ExampleSupport.randomString;

public class StaticResourcesWeakReferenceExample {
    private static final Map<UUID, WeakReference<String>> cache = new HashMap<>();

    public static void main(String[] args) {
        logMemory();

        var scanner = new Scanner(System.in);
        while (true) {
            range(1, 100).forEach(i -> cache.put(UUID.randomUUID(), new WeakReference<>(randomString(128 * 1024))));

            logMemory();
            System.out.println("Continue? (Ctrl+C for exit)");
            scanner.nextLine();
        }
    }
}
