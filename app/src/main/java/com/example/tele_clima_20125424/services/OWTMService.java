package com.example.tele_clima_20125424.services;

import com.example.tele_clima_20125424.dto.CityDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OWTMService {

    @GET("/geo/1.0/direct")
    Call<List<CityDTO>> getCityDetails(
            @Query("q") String cityName,
            @Query("limit") int limit,
            @Query("appid") String apiKey
    );

}
