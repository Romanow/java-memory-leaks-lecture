/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import static java.lang.String.format;

public class InternalStringsExample {
    private static final String GREETING = "Hello, world!";

    public static void main(String[] args) {
        var base = "Hello, %s!";
        var name = "world";
        System.out.println(format(base, name));
        System.out.println("This is the default greeting: " + (format(base, name).intern() == GREETING));
    }
}
