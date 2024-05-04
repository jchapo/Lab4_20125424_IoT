package com.example.tele_clima_20125424.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tele_clima_20125424.dto.CityDTO;
import com.example.tele_clima_20125424.dto.ClimaDTO;

import java.util.List;

public class NavigationActivityViewModel extends ViewModel {

    private List<ClimaDTO> climas;
    private List<CityDTO> cities;

    public List<CityDTO> getCities() {
        return cities;
    }

    public void setCities(List<CityDTO> cities) {
        this.cities = cities;
    }

    public List<ClimaDTO> getClimas() {
        return climas;
    }

    public void setClimas(List<ClimaDTO> climas) {
        this.climas = climas;
    }
}
