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

import com.example.tele_clima_20125424.CityAdapter;
import com.example.tele_clima_20125424.databinding.FragmentGeolocalizationBinding;
import com.example.tele_clima_20125424.dto.CityDTO;
import com.example.tele_clima_20125424.dto.ClimaDTO;
import com.example.tele_clima_20125424.services.OWTMService;
import com.example.tele_clima_20125424.viewModels.NavigationActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeolocalizationFragment extends Fragment {

    private CityAdapter cityAdapter;
    FragmentGeolocalizationBinding binding;
    NavigationActivityViewModel navigationActivityViewModel;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewGeo.setLayoutManager(layoutManager); // Usar el layoutManager creado
        navigationActivityViewModel = new ViewModelProvider(requireActivity()).get(NavigationActivityViewModel.class);
        cityAdapter = new CityAdapter(getContext()); // Asegúrate de reemplazar CityAdapter con el nombre correcto de tu adaptador
        List<CityDTO> cities = navigationActivityViewModel.getCities();
        cityAdapter.setCities(cities); // Actualizar el adaptador con la lista actualizada de ciudades
        binding.recyclerViewGeo.setAdapter(cityAdapter);
    }


    private void setupButton() {
        binding.botonBuscarGeolocalizacion.setOnClickListener(v -> cargarListaWebService());
    }

    public void createRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        owtmService = retrofit.create(OWTMService.class);
    }

    public void cargarListaWebService(){
        String cityToSearch = binding.editTextCiudad.getText().toString();
        owtmService.getCityDetails(cityToSearch, 1, "8dd6fc3be19ceb8601c2c3e811c16cf1").enqueue(new Callback<List<CityDTO>>() {
            @Override
            public void onResponse(Call<List<CityDTO>> call, Response<List<CityDTO>> response) {
                if (response.isSuccessful()) {
                    List<CityDTO> city = response.body();
                    List<CityDTO> cities = navigationActivityViewModel.getCities();
                    if (cities == null) {
                        cities = new ArrayList<>();
                    }
                    cities.addAll(0, city); // Agregar las nuevas ciudades al principio del arreglo cities
                    for (CityDTO c : cities) {
                        Log.d("CityData", "City: " + c.getName() + ", Latitud: " + c.getLat() + ", Longitud: " + c.getLon());
                    }
                    if (city != null && !city.isEmpty()) {
                        cityAdapter.setCities(cities); // Actualizar el adaptador con la lista actualizada de ciudades
                        navigationActivityViewModel.setCities(cities);
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron datos para esa ciudad", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<CityDTO>> call, Throwable t) {
                Log.d("juan", t.getMessage());
                Toast.makeText(requireContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
