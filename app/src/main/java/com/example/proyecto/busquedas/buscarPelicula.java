package com.example.proyecto.busquedas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.info.Movie;
import com.example.proyecto.R;
import com.example.proyecto.adaptadores.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class buscarPelicula extends AppCompatActivity {

    private EditText mEditTextBusqueda;
    private Button mButtonBuscar;
    private RecyclerView mRecyclerViewResultados;
    private TextView mTextViewResultados;
    private List<Movie> mMovieList;
    private MovieAdapter mMovieAdapter;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_pelicula);

        // Obtener las vistas del layout
        mEditTextBusqueda = findViewById(R.id.et_busqueda);
        mButtonBuscar = findViewById(R.id.btn_buscar);
        mRecyclerViewResultados = findViewById(R.id.rv_resultados);
        mTextViewResultados = findViewById(R.id.tv_resultados);

        // Configurar el RecyclerView
        mRecyclerViewResultados.setHasFixedSize(true);
        mRecyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));

        // Crear una lista vacía de películas
        mMovieList = new ArrayList<>();

        // Crear un adaptador para el RecyclerView
        mMovieAdapter = new MovieAdapter(this, mMovieList);

        // Establecer el adaptador para el RecyclerView
        mRecyclerViewResultados.setAdapter(mMovieAdapter);

        // Obtener la instancia de RequestQueue
        mRequestQueue = Volley.newRequestQueue(this);

        // Configurar el clic del botón de buscar
        mButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el término de búsqueda
                String busqueda = mEditTextBusqueda.getText().toString().trim();

                // Validar que se haya ingresado un término de búsqueda
                if (busqueda.isEmpty()) {
                    Toast.makeText(buscarPelicula.this, "Ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
                } else {
                    // Realizar la búsqueda
                    buscarPeliculas(busqueda);
                }
            }
        });
    }

    private void buscarPeliculas(String busqueda) {
        // Definir la URL de la API
        String url = "https://api.themoviedb.org/3/search/movie?api_key=fd3ee47d3c563fc5782362955aba4142&query=" + busqueda;

        // Realizar la solicitud GET a la API
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Convertir la respuesta JSON en un objeto JSONObject
                    JSONObject jsonObject = new JSONObject(response);

                    // Obtener la lista de resultados de la búsqueda
                    JSONArray results = jsonObject.getJSONArray("results");

                    // Verificar que haya resultados
                    if (results.length() == 0) {
                        mTextViewResultados.setText("No se encontraron resultados para la búsqueda");
                        mTextViewResultados.setVisibility(View.VISIBLE);
                        mRecyclerViewResultados.setVisibility(View.GONE);
                    } else {
                        // Limpiar la lista de películas
                        mMovieList.clear();

                        // Recorrer los resultados de la búsqueda
                        for (int i = 0; i < results.length(); i++) {
                            // Obtener el objeto JSON de la película
                            JSONObject movieObject = results.getJSONObject(i);

                            // Obtener los datos de la película
                            String idPelicula = movieObject.getString("id");
                            String titulo = movieObject.getString("title");
                            String imagen = movieObject.getString("poster_path");
                            String duracion = movieObject.has("runtime") ? movieObject.getString("runtime") + " min" : "";
                            String fecha = movieObject.getString("release_date");

                            // Obtener la lista de géneros
                            JSONArray genresArray = movieObject.getJSONArray("genre_ids");
                            String genero = "";

                            // Hacer una llamada adicional a la API para obtener la lista de géneros
                            String urlGeneros = "https://api.themoviedb.org/3/genre/movie/list?api_key=fd3ee47d3c563fc5782362955aba4142&language=es-ES";
                            obtenerInformacionPelicula(idPelicula);


                            // Crear una nueva película con los datos obtenidos
                            Movie movie = new Movie(idPelicula, titulo, imagen, duracion, fecha, genero, "0");

                            // Agregar la película a la lista de películas
                            mMovieList.add(movie);
                        }

                        // Notificar al adaptador que los datos han cambiado
                        mMovieAdapter.notifyDataSetChanged();

                        // Mostrar los resultados de la búsqueda
                        mTextViewResultados.setText("Resultados de búsqueda");
                        mTextViewResultados.setVisibility(View.VISIBLE);
                        mRecyclerViewResultados.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(buscarPelicula.this, "Error al realizar la búsqueda", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la solicitud a la cola de solicitudes
        mRequestQueue.add(request);
    }

    private void obtenerInformacionPelicula(String idPelicula) {
        new GetMovieInfoTask().execute(idPelicula);
    }

    private class GetMovieInfoTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String idPelicula = params[0];

            // Hacer una llamada a la API para obtener la información de la película
            String urlPelicula = "https://api.themoviedb.org/3/movie/" + idPelicula + "?api_key=fd3ee47d3c563fc5782362955aba4142&language=es-ES";
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL apiEndpoint = new URL(urlPelicula);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) apiEndpoint.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setRequestProperty("Content-Type", "application/json");
                httpsURLConnection.setRequestProperty("Accept", "application/json");
                httpsURLConnection.setDoInput(true);

                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                // Convertir la respuesta JSON en un objeto JSONObject
                JSONObject jsonObjectPelicula = new JSONObject(stringBuilder.toString());

                // Obtener el género de la película
                JSONArray generosArray = jsonObjectPelicula.getJSONArray("genres");
                String genero = "";
                for (int i = 0; i < generosArray.length(); i++) {
                    JSONObject jsonGenre = generosArray.getJSONObject(i);
                    if (i == generosArray.length() - 1) {
                        genero += jsonGenre.getString("name");
                    } else {
                        genero += jsonGenre.getString("name") + ", ";
                    }
                }

                // Obtener el runtime de la película
                int runtime = jsonObjectPelicula.getInt("runtime");

                // Actualizar los datos de la película
                for (int i = 0; i < mMovieList.size(); i++) {
                    Movie movie = mMovieList.get(i);
                    if (movie.getIdPelicula().equals(idPelicula)) {
                        movie.setGenero(genero);
                        movie.setDuracion(runtime + " min");
                        mMovieAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}