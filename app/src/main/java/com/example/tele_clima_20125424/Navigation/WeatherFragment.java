package com.example.tele_clima_20125424.Navigation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.example.tele_clima_20125424.MagEventListener;
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

public class WeatherFragment extends Fragment {

    private ClimaAdapter climaAdapter;
    private FragmentWeatherBinding binding;
    private OWTMService owtmService;
    private SensorManager sensorManager;
    private MagEventListener magEventListener;
    private NavigationActivityViewModel navigationActivityViewModel;
    String direction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        setupRecyclerView();
        setupButton();
        createRetrofitService();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMagnetometerListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterMagnetometerListener();
    }

    private void registerMagnetometerListener() {
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetometer != null) {
            magEventListener = new MagEventListener(new MagEventListener.SensorListenerCallback() {
                @Override
                public void onMagneticFieldChanged(float x, float y, float z) {
                    double radians = Math.atan2(y, x);
                    double degrees = Math.toDegrees(radians);
                    if (degrees < 0) {
                        degrees += 360;
                    }
                    direction = getDirection(degrees);
                    Log.d("Direction", "Direction: " + direction);
                }
            });
            sensorManager.registerListener(magEventListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void unregisterMagnetometerListener() {
        if (magEventListener != null) {
            sensorManager.unregisterListener(magEventListener);
        }
    }

    private String getDirection(double degrees) {
        if ((degrees >= 337.5 && degrees <= 360) || (degrees >= 0 && degrees < 22.5)) {
            return "Norte";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            return "Noreste";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            return "Este";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            return "Sureste";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            return "Sur";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            return "Suroeste";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            return "Oeste";
        } else {
            return "Noroeste";
        }
    }



    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewWeather.setLayoutManager(layoutManager);
        navigationActivityViewModel = new ViewModelProvider(requireActivity()).get(NavigationActivityViewModel.class);
        climaAdapter = new ClimaAdapter(getContext());
        List<ClimaDTO> climas = navigationActivityViewModel.getClimas();
        climaAdapter.setClimas(climas);
        binding.recyclerViewWeather.setAdapter(climaAdapter);
    }

    private void setupButton() {
        binding.botonBuscarClima.setOnClickListener(v -> {
            String longitud = binding.editTextLongitudClima.getText().toString().trim();
            String latitud = binding.editTextLatitudClima.getText().toString().trim();

            if (!longitud.isEmpty() && !latitud.isEmpty()) {
                binding.botonBuscarClima.setEnabled(false);
                navigationActivityViewModel.setEnableNavigation(false);
                cargarListaWebService();
                binding.editTextLongitudClima.setText("");
                binding.editTextLatitudClima.setText("");
                binding.editTextLongitudClima.requestFocus();
            } else {
                if (longitud.isEmpty()) {
                    binding.editTextLongitudClima.requestFocus();
                } else {
                    binding.editTextLatitudClima.requestFocus();
                }
                Toast.makeText(requireContext(), "Por favor ingresa la longitud y la latitud", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void createRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        owtmService = retrofit.create(OWTMService.class);
    }

    public void cargarListaWebService(){
        double latitudeclimaToSearch = Double.parseDouble(binding.editTextLatitudClima.getText().toString());
        double longitudeclimaToSearch = Double.parseDouble(binding.editTextLongitudClima.getText().toString());

        owtmService.getClimaDetails(latitudeclimaToSearch, longitudeclimaToSearch, "8dd6fc3be19ceb8601c2c3e811c16cf1").enqueue(new Callback<ClimaDTO>() {
            @Override
            public void onResponse(Call<ClimaDTO> call, Response<ClimaDTO> response) {
                binding.botonBuscarClima.setEnabled(true);
                if (response.isSuccessful()) {
                    ClimaDTO clima = response.body();
                    List<ClimaDTO> climas = navigationActivityViewModel.getClimas();
                    if (climas == null) {
                        climas = new ArrayList<>();
                    }
                    for (ClimaDTO c : climas) {
                        Log.d("ClimaData", "Clima: " + c.getName() + ", Latitud: " + c.getCoord().getLat() + ", Longitud: " + c.getCoord().getLon());
                    }
                    if (clima != null) {
                        clima.getWind().setDirection(direction);
                        climas.add(0, clima);
                        climaAdapter.setClimas(climas);
                        navigationActivityViewModel.setClimas(climas);
                        navigationActivityViewModel.setEnableNavigation(true);
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron datos para esa ciudad", Toast.LENGTH_SHORT).show();
                    }
                } else {
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
                binding.botonBuscarClima.setEnabled(true);
                Log.d("juan", t.getMessage());
                Toast.makeText(requireContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
