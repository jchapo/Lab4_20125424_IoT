package com.example.tele_clima_20125424;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tele_clima_20125424.Navigation.GeolocalizationFragment;
import com.example.tele_clima_20125424.Navigation.WeatherFragment;
import com.example.tele_clima_20125424.databinding.ActivityNavigationBinding;
import com.example.tele_clima_20125424.dto.CityDTO;
import com.example.tele_clima_20125424.dto.ClimaDTO;
import com.example.tele_clima_20125424.services.OWTMService;
import com.example.tele_clima_20125424.viewModels.NavigationActivityViewModel;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NavigationActivity extends AppCompatActivity {
    NavigationActivityViewModel navigationActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ActivityNavigationBinding binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.fragmentContainer.getId(), new GeolocalizationFragment());
        transaction.commit();

        binding.buttonGeolocalizacion.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.fragmentContainer.getId(), new GeolocalizationFragment())
                    .commit();
        });

        binding.buttonClima.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.fragmentContainer.getId(), new WeatherFragment())
                    .commit();
        });

        // Observar el LiveData para actualizar los botones y su color de fondo
        navigationActivityViewModel = new ViewModelProvider(this).get(NavigationActivityViewModel.class);
        navigationActivityViewModel.getEnableNavigation().observe(this, enable -> {
            binding.buttonGeolocalizacion.setEnabled(enable);
            binding.buttonClima.setEnabled(enable);

            if (enable) {
                // Si la navegación está habilitada, establece el color de fondo predeterminado
                binding.buttonGeolocalizacion.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
                binding.buttonClima.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
            } else {
                // Si la navegación está deshabilitada, establece el color de fondo como amarillo
                binding.buttonGeolocalizacion.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                binding.buttonClima.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            }
        });


    }

}