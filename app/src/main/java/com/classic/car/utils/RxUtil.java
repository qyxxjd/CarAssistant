package com.classic.car.utils;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.utils
 *
 * 文件描述: TODO
 * 创 建 人: 续写经典
 * 创建时间: 2016/7/30 15:06
 */
// public final class RxUtil {
//
//     public static Observable.Transformer IO_TRANSFORMER = new Observable.Transformer() {
//         @Override public Object call(Object observable) {
//             return ((Observable) observable).subscribeOn(Schedulers.io())
//                                             .unsubscribeOn(Schedulers.io())
//                                             .observeOn(Schedulers.io());
//         }
//     };
//
//     public static Observable.Transformer IO_ON_UI_TRANSFORMER = new Observable.Transformer() {
//         @Override public Object call(Object observable) {
//             return ((Observable) observable).subscribeOn(Schedulers.io())
//                                             .unsubscribeOn(Schedulers.io())
//                                             .observeOn(AndroidSchedulers.mainThread());
//         }
//     };
//
//     public static Action1<Throwable> ERROR_ACTION = new Action1<Throwable>() {
//         @Override public void call(Throwable throwable) {
//             if (null != throwable && !TextUtils.isEmpty(throwable.getMessage())) {
//                 throwable.printStackTrace();
//             }
//         }
//     };
//
//     public static <T> Observable.Transformer<T, T> applySchedulers(Observable.Transformer transformer) {
//         //noinspection unchecked
//         return (Observable.Transformer<T, T>) transformer;
//     }
// }
