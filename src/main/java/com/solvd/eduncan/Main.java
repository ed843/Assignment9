package com.solvd.eduncan;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        // 1a. Thread using runnable
        new Thread(new RunnableClass()).start();
        // 1b. Thread using class
        new ThreadClass().start();
    }
}
