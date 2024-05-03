package com.example.tele_clima_20125424;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tele_clima_20125424.Navigation.GeolocalizationFragment;
import com.example.tele_clima_20125424.Navigation.WeatherFragment;
import com.example.tele_clima_20125424.databinding.ActivityNavigationBinding;
import com.example.tele_clima_20125424.dto.CityDTO;
import com.example.tele_clima_20125424.services.OWTMService;
import com.example.tele_clima_20125424.viewModels.NavigationActivityViewModel;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NavigationActivity extends AppCompatActivity {
    ActivityNavigationBinding binding;
    List<CityDTO> cities = new ArrayList<>();
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
                    .addToBackStack(null)
                    .commit();
        });

        binding.buttonClima.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.fragmentContainer.getId(), new WeatherFragment())
                    .addToBackStack(null)
                    .commit();
        });

    }
}