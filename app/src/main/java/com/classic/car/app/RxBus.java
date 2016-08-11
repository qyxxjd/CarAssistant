package com.classic.car.app;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.app
 *
 * 文件描述：@see {https://github.com/kaushikgopal/RxJava-Android-Samples/blob/master/app/src/main/java/com/morihacky/android/rxjava/rxbus/RxBus.java}
 * 创 建 人：续写经典
 * 创建时间：16/8/11 下午7:21
 */
public class RxBus {
    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object object) {
        mBus.onNext(object);
    }

    public Observable<Object> toObserverable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }
}
