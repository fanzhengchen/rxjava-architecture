package com;

import io.reactivex.functions.Function;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


public class FlowableMap<T,R> extends Flowable<R> {


    private final Flowable<T> source;
    private final Function<? super T, ? extends R> mapper;

    public FlowableMap(Flowable<T>source, Function<? super T,? extends R> mapper){
        this.source =source;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(Subscriber<? super R> s) {
        source.subscribe(new FlowableMapSubscriber<T,R>(s,mapper));
    }

    @Override
    public void subscribe(Subscriber<? super R> s) {
        subscribeActual(s);
    }

    public static class FlowableMapSubscriber<T,R> implements Subscriber<T>, Subscription{

        private final Subscriber<? super R> actual;
        private final Function<? super T,? extends R> mapper;
        private Subscription mSubscription;
        public FlowableMapSubscriber(Subscriber<? super R> source, Function<? super T,? extends R> mapper){
            this.actual = source;
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe(Subscription s) {
            mSubscription = s;
            actual.onSubscribe(s);
        }

        @Override
        public void onNext(T t) {
            System.out.println(" nnnnnnnnnnnnnnnnnnnnnnnnnnnnnn onNext " + t );
            try {
                R r = mapper.apply(t);
                actual.onNext(r);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void request(long n) {
            mSubscription.request(n);
        }

        @Override
        public void cancel() {

        }
    }
}
