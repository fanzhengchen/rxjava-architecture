package com;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class fzc {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        AtomicLong s = new AtomicLong();
        System.out.println(s);
        Flowable.just(12)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        System.out.println("thread " + Thread.currentThread() + " " + integer);
                        return Integer.toBinaryString(integer);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("thread: " + Thread.currentThread() + " " + s);
                        lock.lock();
                        try {
                            condition.signal();
                        } finally {
                            lock.unlock();
                        }
                    }
                });
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
