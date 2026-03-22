package com.core;

import java.util.concurrent.*;

public class ThreadCreations {
    // Method 1: Extend Thread
    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Method 1 - Extend Thread: " + Thread.currentThread().getName());
        }
    }

    // Method 2: Implement Runnable
    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Method 2 - Runnable: " + Thread.currentThread().getName());
        }
    }

    // Method 3: Implement Callable
    static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            return "Method 3 - Callable: " + Thread.currentThread().getName();
        }
    }

    public static void main(String[] args) throws Exception {
        // Method 1: Extend Thread
        MyThread t1 = new MyThread();
        t1.start();

        // Method 2: Runnable
        Thread t2 = new Thread(new MyRunnable());
        t2.start();

        // Method 3: Callable + ExecutorService
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(new MyCallable());

        // get() blocks until result is available
        String result = future.get();
        System.out.println(result);

        executor.shutdown();

        // Method 4: Lambda (Runnable)
        Thread t4 = new Thread(() -> {
            System.out.println("Method 4 - Lambda Runnable: " + Thread.currentThread().getName());
        });
        t4.start();
    }
}