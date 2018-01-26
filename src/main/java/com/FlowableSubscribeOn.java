package com;

import io.reactivex.Scheduler;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
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
        private final Scheduler.Worker worker;
        private Publisher<T> source;

        private AtomicLong requested;
        private AtomicReference<Subscription> s;

        public SubscribeOnSubscriber(Subscriber<? super T> actual, Publisher<T> source, Scheduler.Worker worker) {
            this.actual = actual;
            this.worker = worker;
            this.source = source;
            this.requested = new AtomicLong();
            this.s = new AtomicReference<>();
        }

        @Override
        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.s, s)) {
                long r = requested.getAndSet(0L);
                if (r != 0) {
                    requestUpstream(r, s);
                }
            }
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void request(long n) {
            Subscription s = this.s.get();
            if (s != null) {
                requestUpstream(n, s);
            } else {
                BackpressureHelper.add(requested, n);
                s = this.s.get();
                if (s != null) {
                    long r = requested.getAndSet(0);
                    if (r != 0) {
                        requestUpstream(n, s);
                    }
                }
            }
        }

        @Override
        public void cancel() {

        }

        @Override
        public void run() {
            lazySet(Thread.currentThread());
            Publisher<T> src = source;
            source = null;
            System.out.println(this + " runnable");
            src.subscribe(this);
        }


        void requestUpstream(long n, Subscription subscription) {
            worker.schedule(new Runnable() {
                @Override
                public void run() {
                    subscription.request(n);
                }
            });
        }
    }
}
