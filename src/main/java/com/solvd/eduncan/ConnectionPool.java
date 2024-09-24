package com.solvd.eduncan;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.*;



public  class ConnectionPool {
        private final Semaphore semaphore;
        private final BlockingQueue<Connection> connections;

        public ConnectionPool(int size) {
            this.semaphore = new Semaphore(size);
            this.connections = new ArrayBlockingQueue<>(size);
            for (int i = 0; i < size; i++) {
                connections.offer(new Connection());
            }
        }

        public Connection getConnection() throws InterruptedException {
            semaphore.acquire();
            return connections.take();
        }

        public void releaseConnection(Connection conn) {
            connections.offer(conn);
            semaphore.release();
        }
    }

class Connection {
    // Mock Connection class
    public Connection() {

    }
}
