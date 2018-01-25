package com;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class fzc {

    public static void main(String[] args){
        Flowable.just(12)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return Integer.toBinaryString(integer);
                    }
                }).subscribe(System.out::print);
    }
}
