/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.util.*;

public class StaticResourcesExample {
    private static final Map<UUID, byte[]> CACHE = new HashMap<>();

    public static void main(String[] args) {
        var input = "";
        var scanner = new Scanner(System.in);
        while (!input.equals("exit")) {
            var buffer = new byte[1024 * 1024];
            new Random().nextBytes(buffer);
            CACHE.put(UUID.randomUUID(), buffer);

            System.out.print("\nContinue? (type 'exit' for exit): ");
            input = scanner.nextLine();
        }
    }
}
