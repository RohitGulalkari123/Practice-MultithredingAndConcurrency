package com.core;

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

        // 4. BLOCKED state — t2 holds lock, t3 waits for it
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
            }
        }, "T2-LockHolder");

        Thread t3 = new Thread(() -> {
            synchronized (lock) { // will BLOCK waiting for t2
                System.out.println("T3 got the lock");
            }
        }, "T3-LockWaiter");

        t2.start();
        Thread.sleep(100); // ensure t2 grabs lock first
        t3.start();
        Thread.sleep(100); // ensure t3 tries synchronized block

        System.out.println("T2 state: " + t2.getState()); // TIMED_WAITING
        System.out.println("T3 state: " + t3.getState()); // BLOCKED

        // 5. WAITING state
        Object lock2 = new Object();
        Thread t5 = new Thread(() -> {
            synchronized (lock2) {
                try { lock2.wait(); } // releases lock, waits indefinitely
                catch (InterruptedException e) {}
            }
        }, "T5-WaitThread");
        t5.start();
        Thread.sleep(100);
        System.out.println("T5 state: " + t5.getState()); // WAITING

        // 6. TERMINATED state
        Thread t6 = new Thread(() ->
                System.out.println("T6 done"), "T6-Quick");
        t6.start();
        t6.join(); // wait for t6 to finish
        System.out.println("T6 state: " + t6.getState()); // TERMINATED

        // Cleanup
        t1.interrupt(); t5.interrupt(); t2.interrupt();


    }
}
