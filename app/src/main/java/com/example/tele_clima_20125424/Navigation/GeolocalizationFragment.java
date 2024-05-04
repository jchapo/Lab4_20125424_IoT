package com.example.tele_clima_20125424.Navigation;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
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

import com.example.tele_clima_20125424.CityAdapter;
import com.example.tele_clima_20125424.AccEventListener;
import com.example.tele_clima_20125424.databinding.FragmentGeolocalizationBinding;
import com.example.tele_clima_20125424.dto.CityDTO;
import com.example.tele_clima_20125424.services.OWTMService;
import com.example.tele_clima_20125424.viewModels.NavigationActivityViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeolocalizationFragment extends Fragment {

    private static final float UMBRAL_ACCELERACION = 15.0f; // Umbral de aceleración en m/s^2

    private CityAdapter cityAdapter;
    private FragmentGeolocalizationBinding binding;
    private NavigationActivityViewModel navigationActivityViewModel;
    private OWTMService owtmService;
    private SensorManager sensorManager;
    private AccEventListener sensorEventListener;
    private boolean isDialogVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGeolocalizationBinding.inflate(inflater, container, false);
        setupRecyclerView();
        setupButton();
        createRetrofitService();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorEventListener = new AccEventListener(new AccEventListener.SensorListenerCallback() {
            @Override
            public void onAccelerationChanged(float accelerationTotal) {
                if (accelerationTotal > UMBRAL_ACCELERACION && !isDialogVisible) {
                    mostrarDialogoDeshacer();
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerAccelerometerListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterAccelerometerListener();
    }

    private void registerAccelerometerListener() {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void unregisterAccelerometerListener() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewGeo.setLayoutManager(layoutManager);
        navigationActivityViewModel = new ViewModelProvider(requireActivity()).get(NavigationActivityViewModel.class);
        cityAdapter = new CityAdapter(getContext());
        List<CityDTO> cities = navigationActivityViewModel.getCities();
        cityAdapter.setCities(cities);
        binding.recyclerViewGeo.setAdapter(cityAdapter);
    }

    private void setupButton() {
        binding.botonBuscarGeolocalizacion.setOnClickListener(v -> {
            String ciudad = binding.editTextCiudad.getText().toString().trim();
            if (!ciudad.isEmpty()) {
                binding.botonBuscarGeolocalizacion.setEnabled(false);
                navigationActivityViewModel.setEnableNavigation(false);
                cargarListaWebService();
                binding.editTextCiudad.setText("");
                binding.editTextCiudad.requestFocus();
            } else {
                binding.editTextCiudad.requestFocus();
                Toast.makeText(requireContext(), "Por favor ingresa el nombre de una ciudad", Toast.LENGTH_SHORT).show();
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

    public void cargarListaWebService() {
        String cityToSearch = binding.editTextCiudad.getText().toString();
        owtmService.getCityDetails(cityToSearch, 1, "8dd6fc3be19ceb8601c2c3e811c16cf1").enqueue(new Callback<List<CityDTO>>() {
            @Override
            public void onResponse(Call<List<CityDTO>> call, Response<List<CityDTO>> response) {
                binding.botonBuscarGeolocalizacion.setEnabled(true);
                if (response.isSuccessful()) {
                    List<CityDTO> city = response.body();
                    List<CityDTO> cities = navigationActivityViewModel.getCities();
                    if (cities == null) {
                        cities = new ArrayList<>();
                    }

                    for (CityDTO c : cities) {
                        Log.d("CityData", "City: " + c.getName() + ", Latitud: " + c.getLat() + ", Longitud: " + c.getLon());
                    }
                    if (city != null && !city.isEmpty()) {
                        String countryCode = city.get(0).getCountry();

                        // Calcular el código Unicode base del emoji de la bandera
                        int baseCodePoint = 0x1F1E6; // Primer código de la bandera (A)
                        int offset = 0x41; // Offset desde el código A

                        // Obtener los caracteres individuales del código del país
                        char firstChar = countryCode.charAt(0);
                        char secondChar = countryCode.charAt(1);

                        // Calcular los puntos de código Unicode para cada carácter
                        int firstCodePoint = baseCodePoint + (firstChar - offset);
                        int secondCodePoint = baseCodePoint + (secondChar - offset);

                        // Convertir los puntos de código Unicode en caracteres
                        String flagEmoji = new String(Character.toChars(firstCodePoint)) + new String(Character.toChars(secondCodePoint));

                        city.get(0).setCountryFlagEmoji(flagEmoji);
                        cities.addAll(0, city);
                        cityAdapter.setCities(cities);
                        navigationActivityViewModel.setCities(cities);
                        navigationActivityViewModel.setEnableNavigation(true);
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron datos para esa ciudad", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CityDTO>> call, Throwable t) {
                binding.botonBuscarGeolocalizacion.setEnabled(true);
                Log.d("juan", t.getMessage());
                Toast.makeText(requireContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoDeshacer() {
        isDialogVisible = true;
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Deshacer acción")
                .setMessage("¿Deseas deshacer la última acción?")
                .setPositiveButton("Deshacer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eliminarUltimaCiudad();
                        isDialogVisible = false;
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        isDialogVisible = false;
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isDialogVisible = false;
                    }
                })
                .show();
    }

    private void eliminarUltimaCiudad() {
        List<CityDTO> cities = navigationActivityViewModel.getCities();
        if (cities != null && !cities.isEmpty()) {
            cities.remove(0); // Elimina el primer elemento de la lista (posición 0)
            cityAdapter.setCities(cities);
            navigationActivityViewModel.setCities(cities);
        }
    }
}
