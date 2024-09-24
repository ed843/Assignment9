package com.solvd.eduncan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.currentThread;
import java.util.concurrent.*;


public class ConnectionPoolTest {

    // 3. Initialize pool with 5 sizes. Load Connection Pool using threads
    // and Thread Pool(7 threads). 5 threads should be able to get the connection.
    // 2 Threads should wait for the next available connection.
    // The program should wait as well.
    private static final int POOL_SIZE = 5;
    private static final int THREAD_COUNT = 7;
    private static final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

    public static void main(String[] args) throws InterruptedException {
        ConnectionPool pool = new ConnectionPool(POOL_SIZE);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    // pool the connections
                    Connection conn = pool.getConnection();
                    System.out.println(Thread.currentThread().getName() + " got a connection.");

                    // Simulate work
                    Thread.sleep(2000);

                    // release the connections
                    pool.releaseConnection(conn);
                    System.out.println(Thread.currentThread().getName() + " released a connection.");

                } catch (InterruptedException e) {
                    currentThread().interrupt();
                    e.printStackTrace();

                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Wait for all threads to finish
        executor.shutdown();
    }
}