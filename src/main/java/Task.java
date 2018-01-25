import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Task implements Runnable{

    private final ReentrantLock mLock;
    private final Condition condition;


    public Task(ReentrantLock reentrantLock, Condition condition){
        mLock = reentrantLock;
        this.condition = condition;
    }


    @Override
    public void run() {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

            Flowable.just(1)
                    .delay(1, TimeUnit.SECONDS)
                    .observeOn(Schedulers.from(executorService))
                    .map(integer -> {
                        System.out.println(Thread.currentThread() + " integer: " + integer);
                        return integer;
                    })

                    .zipWith(Flowable.just(2), new BiFunction<Integer, Integer, Integer>() {
                        @Override
                        public Integer apply(Integer integer, Integer integer2) throws Exception {
                            return integer * integer2;
                        }
                    })
                    .observeOn(Schedulers.newThread())
                    .map(integer -> integer << 2)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            System.out.println("integegr " + integer + " " + Thread.currentThread());
                            mLock.lock();
                            try{
                                condition.signal();

                            }finally {
                                mLock.unlock();
                            }
                            System.out.println("wwwwwwwwwwwwwwwwww");
                        }
                    });

    }
}
