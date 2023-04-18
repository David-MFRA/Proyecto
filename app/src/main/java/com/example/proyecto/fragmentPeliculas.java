package com.example.proyecto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentPeliculas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentPeliculas extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private List<Movie> movieList; // Lista para almacenar las películas
    private RecyclerView recyclerView; // RecyclerView para mostrar la lista de películas
    private MovieAdapter movieAdapter; // Adaptador para el RecyclerView

    public fragmentPeliculas() {
        // Required empty public constructor
    }

    public static fragmentPeliculas newInstance(String param1, String param2) {
        fragmentPeliculas fragment = new fragmentPeliculas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Inicializar la lista de películas
        movieList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peliculas, container, false);
        // Obtener una instancia de SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MiPref", Context.MODE_PRIVATE);

        // Obtener el valor String almacenado con la clave "nombreUsuario" o un valor por defecto si no se encuentra
        String nombre = sharedPreferences.getString("nombreUsuario", "David1");

        // Inicializar el RecyclerView y el adaptador
        recyclerView = view.findViewById(R.id.rvMovies);
        movieAdapter = new MovieAdapter(this.getContext(),movieList);

        // Configurar el RecyclerView con un LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(movieAdapter);

        // Realizar la petición al archivo PHP para obtener la información de las películas
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2/php/listaPelisUsuario.php?nombre=" + nombre,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Convertir la respuesta en un array JSON
                            JSONArray jsonArray = new JSONArray(response);

                            // Recorrer el array JSON y obtener la información de las películas
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String idPelicula = String.valueOf(jsonObject.getInt("idPelicula"));
                                String titulo = jsonObject.getString("titulo");
                                String imagen = jsonObject.getString("imagen");
                                String duracion = String.valueOf(jsonObject.getInt("duracion"));
                                String fecha = jsonObject.getString("fecha");
                                String genero = jsonObject.getString("genero");
                                String vista = jsonObject.getString("vista");

                                // Crear un objeto Movie y agregarlo a la lista
                                Movie pelicula = new Movie(idPelicula, titulo, imagen, duracion, fecha, genero, vista);
                                movieList.add(pelicula);
                            }

                            // Notificar al adaptador que los datos han cambiado
                            movieAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Agregar la petición a la cola de peticiones
        Volley.newRequestQueue(getActivity()).add(stringRequest);

        return view;
    }

}