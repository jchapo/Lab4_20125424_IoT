package com.example.tele_clima_20125424.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tele_clima_20125424.ClimaAdapter;
import com.example.tele_clima_20125424.databinding.FragmentWeatherBinding;
import com.example.tele_clima_20125424.dto.ClimaDTO;
import com.example.tele_clima_20125424.services.OWTMService;
import com.example.tele_clima_20125424.viewModels.NavigationActivityViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://api.openweathermap.org/data/2.5/weather?lat=-12.0621065&lon=-77.0365256&appid=792edf06f1f5ebcaf43632b55d8b03fe

public class WeatherFragment extends Fragment {

    private ClimaAdapter climaAdapter;
    FragmentWeatherBinding binding;
    OWTMService owtmService;
    NavigationActivityViewModel navigationActivityViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        setupRecyclerView();
        setupButton();
        createRetrofitService();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        climaAdapter = new ClimaAdapter(requireContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewWeather.setLayoutManager(layoutManager);
        binding.recyclerViewWeather.setAdapter(climaAdapter);
    }


    private void setupButton() {
        binding.botonBuscarClima.setOnClickListener(v -> cargarListaWebService());
    }

    public void createRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        owtmService = retrofit.create(OWTMService.class);
    }

    public void cargarListaWebService(){
        //double longitudeclimaToSearch = Double.parseDouble(binding.editTextLongitudClima.getText().toString());
        //double latitudeclimaToSearch = Double.parseDouble(binding.editTextLatitudClima.getText().toString());
        double latitudeclimaToSearch = -12.0621065;
        double longitudeclimaToSearch = -77.0365256;

        owtmService.getClimaDetails(latitudeclimaToSearch, longitudeclimaToSearch, "8dd6fc3be19ceb8601c2c3e811c16cf1").enqueue(new Callback<ClimaDTO>() {
            @Override
            public void onResponse(Call<ClimaDTO> call, Response<ClimaDTO> response) {
                if (response.isSuccessful()) {
                    ClimaDTO clima = response.body();
                    navigationActivityViewModel = new ViewModelProvider(requireActivity()).get(NavigationActivityViewModel.class);
                    List<ClimaDTO> climas = navigationActivityViewModel.getClimas();

                    climas.add(0, clima); // Agregar las nuevas ciudades al principio del arreglo climas
                    for (ClimaDTO c : climas) {
                        Log.d("ClimaData", "Clima: " + c.getName() + ", Latitud: " + c.getCoord().getLat() + ", Longitud: " + c.getCoord().getLon());
                    }
                    if (clima != null) {
                        climaAdapter.setClimas(climas); // Actualizar el adaptador con la lista actualizada de ciudades
                        navigationActivityViewModel.setClimas(climas);
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron datos para esa ciudad", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si la solicitud no fue exitosa, mostrar el mensaje de error de la respuesta
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("ErrorResponse", errorBody);
                        Toast.makeText(requireContext(), "Error al obtener los datos: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<ClimaDTO> call, Throwable t) {
                Log.d("juan", t.getMessage());
                Toast.makeText(requireContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
