/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.util.*;
import java.util.concurrent.Executors;

import static ru.romanow.memory.leaks.utils.ExampleSupport.logMemory;
import static ru.romanow.memory.leaks.utils.ExampleSupport.randomString;

public class ThreadLocalMapExample {

    public static void main(String[] args) {
        logMemory();

        final var scanner = new Scanner(System.in);
        final var threadLocal = ThreadLocal.<List<String>>withInitial(ArrayList::new);
        final var executionService = Executors.newFixedThreadPool(10);
        while (true) {
            for (int i = 0; i < 10; i++) {
                executionService.execute(new Generator(threadLocal, 100));
            }
            logMemory();

            System.out.println("Continue? (Ctrl+C for exit)");
            scanner.nextLine();
        }
    }

    private record Generator(ThreadLocal<List<String>> threadLocal, int size)
            implements Runnable {

        @Override
        public void run() {
            List<String> strings = threadLocal.get();
            for (int i = 0; i < size; i++) {
                strings.add(randomString(32));
            }
            threadLocal.set(strings);
        }
    }

}
