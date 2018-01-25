package com;

import io.reactivex.internal.subscriptions.ScalarSubscription;
import org.reactivestreams.Subscriber;

public class FlowableJust<T> extends Flowable<T>{

    private final T value;
    public FlowableJust(T t){
        value = t;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        s.onSubscribe(new ScalarSubscription<T>(s, value));
    }
}
