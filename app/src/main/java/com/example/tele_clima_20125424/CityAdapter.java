package com.example.tele_clima_20125424;

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
        this.cities = new ArrayList<>();
    }

    public void setCities(List<CityDTO> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCityBinding itemBinding = ItemCityBinding.inflate(layoutInflater, parent, false);
        return new CityViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        CityDTO city = cities.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        if (cities == null) {
            return 0; // O devuelve el tamaño adecuado según tu lógica
        } else {
            return cities.size();
        }
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
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
