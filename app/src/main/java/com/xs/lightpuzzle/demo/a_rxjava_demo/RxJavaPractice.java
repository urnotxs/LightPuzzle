package com.xs.lightpuzzle.demo.a_rxjava_demo;

import android.util.Log;

import com.xs.lightpuzzle.demo.a_rxjava_demo.model.Course;
import com.xs.lightpuzzle.demo.a_rxjava_demo.model.Student;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Author: xs
 * Create on: 2019/06/18
 * Description: _
 */
public class RxJavaPractice {
    private static String Tag = "RxJavaPractice";

    public static void testOne() {
        // Observer 即观察者
        final Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(Tag, "Observer : " + "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(Tag, "Observer : " + "onError");
            }

            @Override
            public void onNext(String s) {
                Log.e(Tag, "Observer : " + "onNext : " + s);
            }
        };

        // RxJava内置了一个实现了Observer的抽象类：Subscriber
        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {
                Log.e(Tag, "Subscriber : " + "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(Tag, "Subscriber : " + "onError");
            }

            @Override
            public void onNext(String s) {
                Log.e(Tag, "Subscriber : " + "onNext : " + s);
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello1");
                subscriber.onNext("hello2");
                subscriber.onNext("hello3");
                subscriber.onCompleted();
            }
        });

        Subscription subscribe = observable.subscribe(observer);
        subscribe.unsubscribe();

        subscribe = observable.subscribe(subscriber);
        subscribe.unsubscribe();

        // just 将传入的参数依次发送出去
        observable = Observable.just("hello4", "hello5", "hello6");
        subscribe = observable.subscribe(observer);
        subscribe.unsubscribe();

        // from 将传入的数组或Iterable拆分成具体对象后，依次发送出去
        String[] words = {"hello7", "hello8", "hello9"};
        observable = Observable.from(words);
        observable.subscribe(observer);
    }

    public static void testTwo() {
        Observable<String> observable = Observable.just("hello4", "hello5", "hello6");

        // Action1 是 RxJava 的一个接口，一个参数但无返回 call(T).
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {

            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        };
        // Action0 是 RxJava 的一个接口，无参无返回 call().
        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {

            }
        };
        observable.subscribe(onNextAction);
        observable.subscribe(onNextAction, onErrorAction);
        observable.subscribe(onNextAction, onErrorAction, onCompleteAction);
    }

    /**
     * 线程控制 Scheduler调度器
     * Schedulers.immediate() 直接在当前线程运行
     * Schedulers.newThread() 总是启用新线程，在新线程执行操作
     * Schedulers.io() I/O 操作，无数量上限的线程池，可以重用空闲线程，比 new Thread() 更有效率。
     * Schedulers.computation() 固定的线程池，大小为cpu核数。CPU密集型计算
     * AndroidSchedulers.mainThread() android主线程
     */
    public static void testThree() {
        // 后台线程取数据，主线程显示
        Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.e(Tag, "Observable " + Thread.currentThread().getName());
                subscriber.onNext(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        // onNextAction
                        Log.e(Tag, "onNextAction " + Thread.currentThread().getName() + "  ; Integer " + integer);
                    }
                });
    }

    /**
     * 变换的本质：针对事件序列的处理和再发送
     *
     * 变换  map()
     * new Fun<入参对象，出参对象>
     *
     * flatMap()的原理：
     * 1、使用传入的事件对象创建一个Observable对象
     * 2、并不发送这个Observable，而是将其激发，于是它开始发送事件
     * 3、每一个创建出来的Observable发送的事件，都被汇入同一个Observable，而这个Observable负责将这些事件统一交给Subscriber的回调方法
     */
    public static void testFour() {
        // 将 Student 转化 String
        Observable.from(initFakeData())
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(Tag, "onNextAction : " + s); // 打印一组学生的名字
                    }
                });

        // 打印每个学生所需要修的所有课程名称  一个学生有多个课程  ---- 需使用 flatMap()
        // flatMap() 返回的是个Observable对象，并且这个Observable对象并不是被直接发送到了Subscriber的回调方法中
        Observable.from(initFakeData())
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourseArray());
                    }
                })
                .subscribe(new Subscriber<Course>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Course course) {
                        Log.e(Tag, "onNextAction : " + course.getName()); // 打印一组学生需修的所有课程
                    }
                });
    }

    public static void testRetrofitRxJava() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(Tag, "Subscriber : " + "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(Tag, "Subscriber : " + "onError");
            }

            @Override
            public void onNext(String s) {
                Log.e(Tag, "Subscriber : " + "onNext : " + s);
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello1");
                subscriber.onNext("hello2");
                subscriber.onNext("hello3");
                subscriber.onCompleted();
            }
        });

        observable.lift(new Observable.Operator<String, String>() {
            @Override
            public Subscriber<? super String> call(final Subscriber<? super String> subscriber) {
                return new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                };
            }
        }).throttleFirst(500, TimeUnit.MILLISECONDS); // 去抖动过滤，防抖间隔
    }

    public static void testZip() {
        Observable<Integer> observable1 = Observable.create(new Observable.OnSubscribe<Integer>(){
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        });
        Observable<String> observable2 = Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("A");
                subscriber.onNext("B");
                subscriber.onNext("C");
                subscriber.onCompleted();
            }
        });
        Observable.zip(observable1, observable2, new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(Tag, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(Tag, "onNext : " + s);
            }
        });
    }

    public static void testFlowable() {
        Flowable<Integer> upStream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        Subscriber<Integer> downStream = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(Tag, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(Tag, "onNext : " + integer);
            }
        };

//        upStream.subscribe(downStream);
    }

    private static Student[] initFakeData() {
        Course course1 = new Course("Chinese");
        Course course2 = new Course("Math");
        Course course3 = new Course("English");
        Course[] courseArr = {course1, course2, course3};
        Student student1 = new Student("song", courseArr);
        Student student2 = new Student("yong", courseArr);
        Student[] students = {student1, student2};
        return students;
    }

}
