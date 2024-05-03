package com.example.tele_clima_20125424.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tele_clima_20125424.dto.CityDTO;

import java.util.List;

public class NavigationActivityViewModel extends ViewModel {

    MutableLiveData<List<CityDTO>> listMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> texto = new MutableLiveData<>();

    public MutableLiveData<List<CityDTO>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public MutableLiveData<String> getTexto() {
        return texto;
    }
}
