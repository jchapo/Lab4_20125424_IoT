package com.example.tele_clima_20125424;

import com.example.tele_clima_20125424.dto.CityDTO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tele_clima_20125424.dto.CityDTO;
import com.example.tele_clima_20125424.databinding.ItemCityBinding;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<CityDTO> cities;
    private Context context;

    public CityAdapter(Context context) {
        this.context = context;
        cities = new ArrayList<>();
    }

    public void addCities(List<CityDTO> newCities) {
        cities.clear(); // Limpiar la lista antes de agregar nuevos resultados
        cities.add(newCities);
        notifyDataSetChanged(); // Notificar al RecyclerView que se han agregado nuevos elementos
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCityBinding binding = ItemCityBinding.inflate(layoutInflater, parent, false);
        return new CityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        CityDTO city = cities.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {

        private final ItemCityBinding binding;

        public CityViewHolder(ItemCityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CityDTO city) {
            binding.ciudadTextView.setText(city.getName());
            binding.latitudTextView.setText(String.valueOf(city.getLat()));
            binding.longitudeTextView.setText(String.valueOf(city.getLon()));
        }
    }
}
