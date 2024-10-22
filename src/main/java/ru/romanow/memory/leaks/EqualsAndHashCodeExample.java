/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.util.HashMap;
import java.util.Scanner;

import static ru.romanow.memory.leaks.utils.ExampleSupport.logMemory;

public class EqualsAndHashCodeExample {
    public static void main(String[] args) {
        logMemory();

        final var scanner = new Scanner(System.in);
        final var map = new HashMap<KeyHolder, Integer>();
        while (true) {
            for (int i = 0; i < 100_000; i++) {
                map.put(new KeyHolder("key"), i);
            }

            System.out.println("Map size: " + map.size());
            map.remove(new KeyHolder("key"));
            System.out.println("Map size after remove: " + map.size());

            logMemory();

            System.out.println("Continue? (Ctrl+C for exit)");
            scanner.nextLine();
        }
    }

    private static class KeyHolder {
        private final String key;

        public KeyHolder(String key) {
            this.key = key;
        }

        public String key() {
            return key;
        }
    }

//    private record KeyHolder(String key) {
//    }
}
