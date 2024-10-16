/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.util.HashMap;
import java.util.Scanner;

public class EqualsAndHashCodeExample {
    public static void main(String[] args) {
        var input = "";
        var scanner = new Scanner(System.in);
        final var map = new HashMap<KeyHolder, Integer>();
        while (!input.equals("exit")) {
            for (int i = 0; i < 100_000; i++) {
                map.put(new KeyHolder("key"), i);
            }

            System.out.printf("Map size: %d\n", map.size());
            map.remove(new KeyHolder("key"));
            System.out.printf("Map size after remove: %d\n", map.size());

            System.out.print("Continue? (type 'exit' for exit): ");
            input = scanner.nextLine();
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
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
