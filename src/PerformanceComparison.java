public class PerformanceComparison {
    public static void main(String[] args) throws InterruptedException {
        int limit = 10;

        // Measure performance of BlockingQueueExample
        BlockingQueueExample blockingQueueExample = new BlockingQueueExample(limit);
        long durationBlockingQueue = PerformanceUtil.measurePerformance(() -> {
            Thread producerThread = new Thread(blockingQueueExample::produce);
            Thread consumerThread = new Thread(blockingQueueExample::consume);
            producerThread.start();
            consumerThread.start();
            try {
                producerThread.join();
                consumerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        System.out.println("BlockingQueueExample duration: " + durationBlockingQueue + " sec");

        // Measure performance of ProducerConsumer
        ProducerConsumer producerConsumer = new ProducerConsumer();
        long durationProducerConsumer = PerformanceUtil.measurePerformance(() -> {
            Thread producerThread = new Thread(() -> {
                try {
                    producerConsumer.produce();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            Thread consumerThread = new Thread(() -> {
                try {
                    producerConsumer.consume();
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
        });
        System.out.println("ProducerConsumer duration: " + durationProducerConsumer + " sec");
    }
}