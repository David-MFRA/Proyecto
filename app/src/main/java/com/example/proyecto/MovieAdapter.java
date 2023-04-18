package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context mContext;
    private List<Movie> mMovieList;

    public MovieAdapter(Context context, List<Movie> movieList) {
        mContext = context;
        mMovieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        holder.textViewTitle.setText(movie.getTitulo());
        holder.textViewGenero.setText(movie.getGenero());
        holder.textViewDuracion.setText(movie.getDuracion());
        holder.textViewFecha.setText(movie.getFecha());
        holder.imageViewPoster.setImageResource(R.drawable.placeholder); // placeholder

        // Cargar la imagen de la URL
        Glide.with(mContext)
                .load("https://image.tmdb.org/t/p/w500" + movie.getImagen())
                .placeholder(R.drawable.placeholder) // Imagen de placeholder
                .error(R.drawable.placeholder) // Imagen de error
                .into(holder.imageViewPoster);

        // Configurar el OnClickListener para el elemento de la lista
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la película seleccionada
                Movie movie = mMovieList.get(holder.getAdapterPosition());

                // Crear un intent para iniciar la actividad de detalles de la película
                Intent intent = new Intent(mContext, Pelicula.class);
                intent.putExtra("movie", movie); // Pasar la película como extra en el intent
                mContext.startActivity(intent);
            }
        });

        // Acceder al botón
        Button boton = holder.itemView.findViewById(R.id.button_action);

        // Establecer un listener para el evento de clic en el botón
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una instancia de RequestQueue
                RequestQueue queue = Volley.newRequestQueue(mContext);

                // Definir la URL del archivo PHP
                String url = "http://10.0.2.2/php/cambiarVista.php";
                StringRequest stringRequest = null;

                // Cambiar el color del botón
                boton.setBackgroundColor(Color.RED); // Cambiar a color deseado

                stringRequest = getStringRequest(url, movie, "0");

                // Obtener la posición del elemento en la lista
                int adapterPosition = holder.getAdapterPosition();

                // Eliminar la película de la lista
                mMovieList.remove(adapterPosition);
                notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado

                // Agregar la solicitud a la cola de solicitudes
                queue.add(stringRequest);
            }

        });
    }

    @NonNull
    private StringRequest getStringRequest(String url, Movie movie, String vista) {
        // Crear un StringRequest para la solicitud POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor
                        try {
                            // Convertir la respuesta JSON en un objeto JSON
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {

                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Pasar los parámetros necesarios al archivo PHP
                // Obtener una referencia a SharedPreferences
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("MiPref", Context.MODE_PRIVATE);

                // Obtener el nombre de usuario desde SharedPreferences
                String nombreUsuario = sharedPreferences.getString("nombreUsuario", "");

                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombreUsuario);
                params.put("idPelicula", movie.getIdPelicula());
                params.put("vista", vista);
                return params;
            }
        };
        return stringRequest;
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewGenero;
        public TextView textViewDuracion;
        public TextView textViewFecha;
        public ImageView imageViewPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewGenero = itemView.findViewById(R.id.text_view_genero);
            textViewDuracion = itemView.findViewById(R.id.text_view_duracion);
            textViewFecha = itemView.findViewById(R.id.text_view_fecha);
            imageViewPoster = itemView.findViewById(R.id.image_view_poster);
        }
    }


}
