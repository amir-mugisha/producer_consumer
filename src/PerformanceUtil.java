public class PerformanceUtil {
    public static long measurePerformance(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        return (endTime - startTime)/ 1_000_000_000;
    }
}