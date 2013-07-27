import org.junit.Test

/**
 * User: Dmitry Fateev
 * Date: 02.07.13
 * Time: 20:01
 * e-mail: wearing.fateev@gmail.com
 */
class PQTest {
    @Test
    public void priorityQueueTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>(10);
        for (int i = 0; i < 100; i++) {
            queue.add(i)
        }
        queue.each { println(it) };
    }
}
