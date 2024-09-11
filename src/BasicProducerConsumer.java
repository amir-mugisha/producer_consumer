import java.util.LinkedList;

class ProducerConsumer {
    private final LinkedList<Integer> list = new LinkedList<>();
    private final int CAPACITY = 5;
    private final int LIMIT = 10; // Limit to 10 items

    public void produce() throws InterruptedException {
        int value = 0;
        while (value < LIMIT) {
            synchronized (this) {
                while (list.size() == CAPACITY) {
                    wait(); // wait until there's space in the list
                }
                System.out.println("Producer produced: " + value);
                list.add(value++);
                notify(); // notify the consumer
                Thread.sleep(1000); // simulate time taken to produce
            }
        }
    }

    public void consume() throws InterruptedException {
        int count = 0;
        while (count < LIMIT) {
            synchronized (this) {
                while (list.isEmpty()) {
                    wait(); // wait until there's data to consume
                }
                int value = list.removeFirst();
                System.out.println("Consumer consumed: " + value);
                notify(); // notify the producer
                Thread.sleep(1000); // simulate time taken to consume
                count++;
            }
        }
    }
}

public class BasicProducerConsumer {
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();

        Thread producerThread = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}