package com.core;

public class DaemonThread {

    static class BackgroundMonitor implements Runnable {
        @Override
        public void run() {
            System.out.println("[Monitor] isDaemon: "
                    + Thread.currentThread().isDaemon());
            int count = 0;
            while (true) { // infinite loop — OK for daemon
                try {
                    Thread.sleep(500);
                    System.out.println("[Monitor] Heartbeat #" + ++count);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread daemon = new Thread(new BackgroundMonitor(), "DaemonMonitor");
        daemon.setDaemon(true); // before start()
        daemon.start();

        Thread user = new Thread(() -> {
            try {
                System.out.println("[UserTask] Working for 1.5s...");
                Thread.sleep(1500);
                System.out.println("[UserTask] Done");
            } catch (InterruptedException e) {
            }
        }, "UserWorker");
        user.start();

        user.join(); // JVM exits after user thread finishes
        System.out.println("[Main] User thread done. JVM exits.");
        // daemon thread is killed here — ~3 heartbeats seen

        // Demonstrate: setDaemon after start throws exception
        Thread t = new Thread(() -> {});
        t.start();
        try {
            t.setDaemon(true); // after start -> exception
        } catch (IllegalThreadStateException e) {
            System.out.println("Cannot set daemon after start!");
        }


    }

}
