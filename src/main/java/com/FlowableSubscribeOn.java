package com;

import io.reactivex.Scheduler;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class FlowableSubscribeOn<T> extends Flowable<T> {

    public FlowableSubscribeOn(Scheduler scheduler){

    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {

    }


    public static class FlowableSubscribeOnSubscriber<T> implements Subscriber<T>,Subscription{

        @Override
        public void onSubscribe(Subscription s) {

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
    }
}
