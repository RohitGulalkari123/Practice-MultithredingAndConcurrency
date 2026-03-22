package org.example;

public class ThreadLifeCycleDemo {
    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        // 1. NEW state
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }, "T1");
        System.out.println("After creation: " + t1.getState()); // NEW

        // 2. RUNNABLE state
        t1.start();
        System.out.println("After start(): " + t1.getState()); // RUNNABLE

        // 3. TIMED_WAITING state
        Thread.sleep(100); // give t1 time to enter sleep
        System.out.println("While sleeping: " + t1.getState()); // TIMED_WAITING



    }
}
