import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class BlockingQueueExample {
    private final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
    private final int limit;

    public BlockingQueueExample(int limit) {
        this.limit = limit;
    }

    public void produce() {
        int value = 0;
        try {
            while (value < limit && !Thread.currentThread().isInterrupted()) {
                System.out.println("Producer produced: " + value);
                queue.put(value++);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Producer interrupted");
            Thread.currentThread().interrupt();  // Re-interrupt the thread
        } catch (Exception e) {
            System.out.println("Producer encountered error: " + e.getMessage());
        }
    }

    public void consume() {
        int count = 0;
        try {
            while (count < limit && !Thread.currentThread().isInterrupted()) {
                int value = queue.take();
                System.out.println("Consumer consumed: " + value);
                count++;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Consumer encountered error: " + e.getMessage());
        }
    }
}

public class BlockingQueueProducerConsumer {
    public static void main(String[] args) {
        int limit = 10;  // Set the limit for the number of produced/consumed items
        BlockingQueueExample example = new BlockingQueueExample(limit);

        Thread producerThread = new Thread(() -> {
            example.produce();
        });

        Thread consumerThread = new Thread(() -> {
            example.consume();
        });

        producerThread.start();
        consumerThread.start();

        // Add a shutdown hook to gracefully stop the threads
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            producerThread.interrupt();
            consumerThread.interrupt();
            try {
                producerThread.join();
                consumerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
    }
}