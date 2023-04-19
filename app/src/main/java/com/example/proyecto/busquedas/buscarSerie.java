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
import com.example.proyecto.R;
import com.example.proyecto.adaptadores.MovieAdapter;
import com.example.proyecto.adaptadores.ShowAdapter;
import com.example.proyecto.info.Movie;
import com.example.proyecto.info.Show;

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

public class buscarSerie extends AppCompatActivity {

    private EditText mEditTextBusqueda;
    private Button mButtonBuscar;
    private RecyclerView mRecyclerViewResultados;
    private TextView mTextViewResultados;
    private List<Show> mTVShowList;
    private ShowAdapter mTVShowAdapter;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_serie);

        // Obtener las vistas del layout
        mEditTextBusqueda = findViewById(R.id.et_busqueda);
        mButtonBuscar = findViewById(R.id.btn_buscar);
        mRecyclerViewResultados = findViewById(R.id.rv_resultados);
        mTextViewResultados = findViewById(R.id.tv_resultados);

        // Configurar el RecyclerView
        mRecyclerViewResultados.setHasFixedSize(true);
        mRecyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));

        // Crear una lista vacía de series
        mTVShowList = new ArrayList<>();

        // Crear un adaptador para el RecyclerView
        mTVShowAdapter = new ShowAdapter(this, mTVShowList);

        // Establecer el adaptador para el RecyclerView
        mRecyclerViewResultados.setAdapter(mTVShowAdapter);

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
                    Toast.makeText(buscarSerie.this, "Ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
                } else {
                    // Realizar la búsqueda
                    buscarSeries(busqueda);
                }
            }
        });
    }

    private void buscarSeries(String busqueda) {
        // Definir la URL de la API
        String url = "https://api.themoviedb.org/3/search/tv?api_key=fd3ee47d3c563fc5782362955aba4142&query=" + busqueda;

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
                    if (results.length()== 0) {
                        mTextViewResultados.setText("No se encontraron resultados para la búsqueda");
                        mTextViewResultados.setVisibility(View.VISIBLE);
                        mRecyclerViewResultados.setVisibility(View.GONE);
                    } else {
                        // Limpiar la lista de series
                        mTVShowList.clear();
                        // Recorrer los resultados de la búsqueda
                        for (int i = 0; i < results.length(); i++) {
                            // Obtener el objeto JSON de la serie
                            JSONObject serieObject = results.getJSONObject(i);

                            // Obtener los datos de la serie
                            String idSerie = serieObject.getString("id");
                            String titulo = serieObject.getString("name");
                            String imagen = serieObject.getString("poster_path");
                            String duracion = "";
                            String fecha = serieObject.getString("first_air_date");

                            // Obtener la lista de géneros
                            JSONArray genresArray = serieObject.getJSONArray("genre_ids");
                            String genero = "";

                            // Hacer una llamada adicional a la API para obtener la lista de géneros
                            String urlGeneros = "https://api.themoviedb.org/3/genre/tv/list?api_key=fd3ee47d3c563fc5782362955aba4142&language=es-ES";
                            obtenerInformacionSerie(idSerie);

                            // Crear una nueva serie con los datos obtenidos
                            Show serie = new Show(idSerie, titulo, "", "", imagen, "", "", "1", genero, "1");


                            // Agregar la serie a la lista de series
                            mTVShowList.add(serie);
                        }

                        // Notificar al adaptador que los datos han cambiado
                        mTVShowAdapter.notifyDataSetChanged();

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
                Toast.makeText(buscarSerie.this, "Error al realizar la búsqueda", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la solicitud a la cola de solicitudes
        mRequestQueue.add(request);
    }

    private void obtenerInformacionSerie(String idSerie) {
        new GetSerieInfoTask().execute(idSerie);
    }

    private class GetSerieInfoTask extends AsyncTask<String, Show, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String idSerie = params[0];

            // Hacer una llamada a la API para obtener la información de la serie
            String urlSerie = "https://api.themoviedb.org/3/tv/" + idSerie + "?api_key=fd3ee47d3c563fc5782362955aba4142&language=es-ES";
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL apiEndpoint = new URL(urlSerie);
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
                // Convertir la respuesta JSON en un objeto JSONObject
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                // Obtener la lista de géneros de la serie
                JSONArray genresArray = jsonObject.getJSONArray("genres");
                StringBuilder generos = new StringBuilder();

                // Recorrer la lista de géneros y agregarlos a la cadena de géneros separados por coma
                for (int i = 0; i < genresArray.length(); i++) {
                    JSONObject genreObject = genresArray.getJSONObject(i);
                    String genre = genreObject.getString("name");

                    if (i > 0) {
                        generos.append(", ");
                    }

                    generos.append(genre);
                }

                // Buscar la serie en la lista de series y actualizar su género
                for (Show serie : mTVShowList) {
                    if (serie.getIdSerie().equals(idSerie)) {
                        serie.setGenero(generos.toString());
                        publishProgress(serie);
                        break;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}