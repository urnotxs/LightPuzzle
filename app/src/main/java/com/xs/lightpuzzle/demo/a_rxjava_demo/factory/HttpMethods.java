package com.xs.lightpuzzle.demo.a_rxjava_demo.factory;//package com.xs.lightpuzzle.demo.a_demo_rxjava.factory;
//
//import com.xs.lightpuzzle.demo.a_demo_rxjava.RetrofitPractice;
//import com.xs.lightpuzzle.demo.a_demo_rxjava.model.MovieEntity;
//
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * Author: xs
// * Create on: 2019/06/19
// * Description: _
// */
//public class HttpMethods {
//
//    private static final int DEFAULT_TIMEOUT = 5;
//    private static final String BASE_URL = "https://api.douban.com/v2/movie/";
//
//    private Retrofit retrofit;
//    private RetrofitPractice.MovieService2 movieService;
//
//    // 单例模式
//    public static class SingletonHolder {
//        private static final HttpMethods INSTANCE = new HttpMethods();
//    }
//
//    public static HttpMethods getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    public HttpMethods() {
//        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//
//        retrofit = new Retrofit.Builder()
//                .client(httpClientBuilder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(BASE_URL)
//                .build();
//
//        movieService = retrofit.create(RetrofitPractice.MovieService2.class);
//    }
//
//    public void getTopMovie(Subscriber<MovieEntity> subscriber, int start, int count) {
//        movieService.getTopMovie(start, count)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }
//}
