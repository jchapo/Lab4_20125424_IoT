package com.example.tele_clima_20125424.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tele_clima_20125424.CityAdapter;
import com.example.tele_clima_20125424.R;
import com.example.tele_clima_20125424.databinding.FragmentGeolocalizationBinding;
import com.example.tele_clima_20125424.dto.CityDTO;
import com.example.tele_clima_20125424.services.OWTMService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeolocalizationFragment extends Fragment {

    private CityAdapter cityAdapter;
    FragmentGeolocalizationBinding binding;
    OWTMService owtmService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGeolocalizationBinding.inflate(inflater, container, false);
        setupRecyclerView();
        setupButton();
        createRetrofitService();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        cityAdapter = new CityAdapter(requireContext());
        binding.recyclerViewGeo.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewGeo.setAdapter(cityAdapter);
    }

    private void setupButton() {
        binding.botonBuscarGeolocalizacion.setOnClickListener(v -> cargarListaWebService());
    }

    public void createRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        owtmService = retrofit.create(OWTMService.class);
    }

    public void cargarListaWebService() {
        String cityToSearch = binding.editTextCiudad.getText().toString();
        owtmService.getCityDetails(cityToSearch, "8dd6fc3be19ceb8601c2c3e811c16cf1").enqueue(new Callback<CityDTO>() {
            @Override
            public void onResponse(Call<CityDTO> call, Response<CityDTO> response) {
                if (response.isSuccessful()) {
                    CityDTO city = response.body();
                    if (city != null) {
                        cityAdapter.addCity(city);
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron datos para esa ciudad", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CityDTO> call, Throwable t) {
                Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
