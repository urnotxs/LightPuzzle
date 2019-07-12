package com.xs.lightpuzzle.demo.a_rxjava_demo;//package com.xs.lightpuzzle.demo.a_demo_rxjava;
//
//import com.xs.lightpuzzle.demo.a_demo_rxjava.factory.HttpMethods;
//import com.xs.lightpuzzle.demo.a_demo_rxjava.model.MovieEntity;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.GET;
//import retrofit2.http.Query;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * Author: xs
// * Create on: 2019/06/19
// * Description: _
// */
//public class RetrofitPractice {
//    public interface MovieService {
//        @GET("top250")
//        Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
//    }
//
//    public interface MovieService2 {
//        @GET("top250")
//        Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
//    }
//
//    public static void getMovie() {
//        String baseUrl = "https://api.douban.com/v2/movie/";
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//        MovieService movieService = retrofit.create(MovieService.class);
//        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
//        call.enqueue(new Callback<MovieEntity>() {
//            @Override
//            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<MovieEntity> call, Throwable t) {
//
//            }
//        });
//
//        MovieService2 movieService2 = retrofit.create(MovieService2.class);
//        movieService2.getTopMovie(0, 10)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MovieEntity>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(MovieEntity movieEntity) {
//
//                    }
//                });
//    }
//
//    public void testFactory() {
//        HttpMethods.getInstance().getTopMovie(
//                new Subscriber<MovieEntity>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(MovieEntity movieEntity) {
//
//                    }
//                }, 0, 10);
//    }
//}
