package com;

import io.reactivex.Scheduler;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class FlowableSubscribeOn<T> extends Flowable<T> {

    private final Flowable<T> source;
    private final Scheduler scheduler;
    private final Scheduler.Worker worker;

    public FlowableSubscribeOn(Flowable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
        this.worker = scheduler.createWorker();
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        SubscribeOnSubscriber<T> sos = new SubscribeOnSubscriber<T>(s, source, worker);
        s.onSubscribe(sos);

        worker.schedule(sos);
    }


    public static class SubscribeOnSubscriber<T> extends AtomicReference<Thread> implements Subscriber<T>, Subscription, Runnable {

        private final Subscriber<? super T> actual;
        private Subscription s;
        private final Scheduler.Worker worker;
        private Publisher<T> source;

        private AtomicLong requested;

        public SubscribeOnSubscriber(Subscriber<? super T> actual, Publisher<T> source, Scheduler.Worker worker) {
            this.actual = actual;
            this.worker = worker;
            this.source = source;
            this.requested = new AtomicLong();
        }

        @Override
        public void onSubscribe(Subscription s) {
            this.s = s;

        }

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void request(long n) {

        }

        @Override
        public void cancel() {

        }

        @Override
        public void run() {

//            s.request();
        }
    }
}
