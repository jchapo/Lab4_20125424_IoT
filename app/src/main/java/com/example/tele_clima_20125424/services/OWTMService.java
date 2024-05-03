package com.example.tele_clima_20125424.services;

import com.example.tele_clima_20125424.dto.CityDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OWTMService {

    @GET("/")
    Call<CityDTO> getCityDetails(
            @Query("q") String cityName,
            @Query("appid") String apiKey
    );

}
