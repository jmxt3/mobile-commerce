package com.zxventures.beer.helpers;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by joaopmmachete on 9/6/17.
 */

public class RxBus {

    public RxBus() { }

    private final BehaviorSubject<Object> bus = BehaviorSubject.create();

    public void send(@NonNull Object event) {
        bus.onNext(event);
    }

    public void removeEvent(){
        bus.onNext(null);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }

}
