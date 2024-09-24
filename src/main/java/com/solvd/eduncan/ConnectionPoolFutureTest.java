package com.solvd.eduncan;

import java.util.concurrent.*;

public class ConnectionPoolFutureTest {
    // 4. Implement 4th part but with IFuture and CompletableStage.
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ConnectionPool pool = new ConnectionPool(5);
        ExecutorService executor = Executors.newFixedThreadPool(7);

        CompletableFuture<?>[] futures = new CompletableFuture[7];

        for (int i = 0; i < 7; i++) {
            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    Connection conn = pool.getConnection();
                    System.out.println(Thread.currentThread().getName() + " got a connection");
                    Thread.sleep(1000); // Simulate work
                    pool.releaseConnection(conn);
                    System.out.println(Thread.currentThread().getName() + " released a connection");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted: " + e.getMessage());
                    throw new RuntimeException(e); // Throw exception to be handled by exceptionally()
                }
            }, executor).exceptionally(ex -> {
                ex.printStackTrace(); // Log any exception encountered in the task
                return null;
            });
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);
        allFutures.get(); // Wait for all futures to complete

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force shutdown if timeout occurs
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }
}