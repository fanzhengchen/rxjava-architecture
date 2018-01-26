import io.reactivex.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FlowableCreate {
    static FlowableEmitter<Integer> flowableEmitter;

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                flowableEmitter = emitter;
                emitter.onNext(45);
            }
        }, BackpressureStrategy.BUFFER)
                .map(integer -> integer << 1)
                .subscribe(integer -> {
                    System.out.println(integer);

                });

        flowableEmitter.onNext(78);
        flowableEmitter.onNext(4556);
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
