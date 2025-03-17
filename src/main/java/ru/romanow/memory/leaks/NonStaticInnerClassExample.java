/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.util.ArrayList;
import java.util.Scanner;

import static ru.romanow.memory.leaks.utils.ExampleSupport.logMemory;
import static ru.romanow.memory.leaks.utils.ExampleSupport.randomString;

public class NonStaticInnerClassExample {
    public static void main(String[] args) {
        final var observers = new ArrayList<Observer>();
        logMemory();

        var scanner = new Scanner(System.in);
        while (true) {
            final var holder = new Holder();
            observers.add(holder.new InnerObserver());

            logMemory();
            System.out.println("Continue? (Ctrl+C for exit)");
            scanner.nextLine();
        }
    }
}

interface Observer {
    void observe();
}

class Holder {
    private final String payload = randomString(1024 * 1024);

    public class InnerObserver
            implements Observer {

        @Override
        public void observe() {
            System.out.println(payload.substring(0, 10) + "...");
        }
    }
}