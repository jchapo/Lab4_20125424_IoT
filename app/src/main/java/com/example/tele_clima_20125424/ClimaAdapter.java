package com.example.tele_clima_20125424;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tele_clima_20125424.databinding.ItemClimaBinding;
import com.example.tele_clima_20125424.dto.ClimaDTO;

import java.util.ArrayList;
import java.util.List;

public class ClimaAdapter extends RecyclerView.Adapter<ClimaAdapter.ClimaViewHolder> {

    private List<ClimaDTO> climas;
    private Context context;

    public ClimaAdapter(Context context) {
        this.context = context;
        this.climas = new ArrayList<>();
    }

    public void setClimas(List<ClimaDTO> climas) {
        this.climas = climas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClimaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemClimaBinding itemBinding = ItemClimaBinding.inflate(layoutInflater, parent, false);
        return new ClimaViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClimaViewHolder holder, int position) {
        ClimaDTO clima = climas.get(position);
        holder.bind(clima);
    }

    @Override
    public int getItemCount() {
        if (climas == null) {
            return 0; // O devuelve el tamaño adecuado según tu lógica
        } else {
            return climas.size();
        }
    }

    public static class ClimaViewHolder extends RecyclerView.ViewHolder {
        private final ItemClimaBinding binding;

        public ClimaViewHolder(ItemClimaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ClimaDTO clima) {
            if (!clima.getName().isEmpty()) {
                binding.ciudadClimaTextView.setText(clima.getName());
            } else {
                binding.ciudadClimaTextView.setText("Desconocido");
            }

            binding.temperaturaClimaTextView.setText(String.valueOf(clima.getMain().getTemp())+" K");
            binding.tempMinClimaTextView.setText(String.valueOf(clima.getMain().getTemp_min())+" K");
            binding.tempMaxClimaTextView.setText(String.valueOf(clima.getMain().getTemp_max())+" K");
            binding.vientoClimaTextView.setText(String.valueOf(clima.getWind().getSpeed()));
        }
    }
}
