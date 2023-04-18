package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowViewHolder> {

    private List<Show> showList;
    private Context context;

    public ShowAdapter(Context context, List<Show> showList) {
        this.showList = showList;
        this.context = context;
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_item, parent, false);

        return new ShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder holder, int position) {
        Show show = showList.get(position);

        // Cargar la imagen de la URL
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + show.getImagen())
                .placeholder(R.drawable.placeholder) // Imagen de placeholder
                .error(R.drawable.placeholder) // Imagen de error
                .into(holder.imageView);

        // Asignar el título, género, plataforma y temporadas de la serie a los correspondientes TextViews
        holder.textViewTitle.setText(show.getTitulo());
        holder.textViewGenero.setText(show.getGenero());
        holder.textViewPlataforma.setText(show.getPlataforma());
        holder.textViewTemporadas.setText("Temporadas: " + show.getTemporadas());

        // Configurar el OnClickListener para el elemento de la lista
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la película seleccionada
                Show serie = showList.get(holder.getAdapterPosition());

                // Crear un intent para iniciar la actividad de detalles de la serie
                Intent intent = new Intent(context, Serie.class);
                intent.putExtra("serie", serie); // Pasar la serie como extra en el intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewGenero;
        TextView textViewPlataforma;
        TextView textViewTemporadas;

        public ShowViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.serie_image);
            textViewTitle = itemView.findViewById(R.id.serie_title);
            textViewGenero = itemView.findViewById(R.id.serie_genero);
            textViewPlataforma = itemView.findViewById(R.id.serie_plataforma);
            textViewTemporadas = itemView.findViewById(R.id.serie_temporadas);
        }
    }
}
