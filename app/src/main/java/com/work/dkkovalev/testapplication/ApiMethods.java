package com.work.dkkovalev.testapplication;


import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface ApiMethods {
    @GET("/points")
    void getPoints(Callback<List<Point>> pointCallback);

    @GET("/points/{id}")
    void getPointById(@Path("id") String pointId, Callback<Point> pointCallback);

    @DELETE("/points/{id}")
    void deletePoint(@Path("id") String pointId, Callback<Response> callback);

    @POST("/points")
    void postPoint(@Body Point point, Callback<List<Point>> callback);

    @PUT("/points/{id}")
    void updatePoint(@Path("id") String pointId, @Body Point point, Callback<List<Point>> callback);
}
