package com;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import io.reactivex.internal.subscribers.LambdaSubscriber;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


public abstract class Flowable<T> implements Publisher<T> {


    protected abstract void subscribeActual(Subscriber<? super T> s);

    @Override
    public void subscribe(Subscriber<? super T> s) {
            subscribeActual(s);
    }

    public static <R> Flowable<R> just(R r){
        return new FlowableJust<R>(r);
    }

    public final <R> Flowable<R> map(Function<? super T,? extends R> mapper){
        return new FlowableMap<T,R>(this,mapper);
    }

    public final Flowable<T> subscribeOn(Scheduler scheduler){
        return new FlowableSubscribeOn<T>(this,scheduler);
    }

    public final void subscribe(FlowableSubscriber<? super T> s){

        Subscriber<? super T> z = s;

        subscribe(z);
    }

    public final Disposable subscribe(Consumer<? super T> onNext){
        return subscribe(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION,
                FlowableInternalHelper.RequestMax.INSTANCE);
    }

    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
                                          Action onComplete, Consumer<? super Subscription> onSubscribe){
        LambdaSubscriber<T> ls = new LambdaSubscriber<T>(onNext, onError, onComplete, onSubscribe);

        subscribe(ls);

        return ls;
    }
}
