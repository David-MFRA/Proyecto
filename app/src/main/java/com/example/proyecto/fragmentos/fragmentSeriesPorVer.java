package com.example.proyecto.fragmentos;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.R;
import com.example.proyecto.adaptadores.ShowAdapter;
import com.example.proyecto.adaptadores.TemporadaAdapter;
import com.example.proyecto.info.Show;
import com.example.proyecto.info.Season;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentSeriesPorVer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentSeriesPorVer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Show> showList; // Lista para almacenar las series
    private RecyclerView recyclerView; // RecyclerView para mostrar la lista de películas
    private ShowAdapter showAdapter; // Adaptador para el RecyclerView

    private TemporadaAdapter temporadaAdapter;
    private List<Season> seasonList;

    public fragmentSeriesPorVer() {
        // Required empty public constructor
    }

    public static fragmentSeriesPorVer newInstance(String param1, String param2) {
        fragmentSeriesPorVer fragment = new fragmentSeriesPorVer();
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
        showList  = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_series_lista, container, false);

        // Obtener una instancia de SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MiPref", Context.MODE_PRIVATE);

        // Obtener el valor String almacenado con la clave "nombreUsuario" o un valor por defecto si no se encuentra
        String nombre = sharedPreferences.getString("nombreUsuario", "David1");

        // Inicializar el RecyclerView y el adaptador
        recyclerView = view.findViewById(R.id.rvShows);
        showAdapter = new ShowAdapter(this.getContext(),showList);

        // Configurar el RecyclerView con un LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(showAdapter);

        actualizarLista(nombre);

        return view;
    }

    public void actualizarLista(String nombre) {
        // Realizar la petición al archivo PHP para obtener la información de las películas
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2/php//listaSeriesUsuario.php?nombre=" + nombre,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Convertir la respuesta en un array JSON
                            JSONArray jsonArray = new JSONArray(response);

                            // Recorrer el array JSON y obtener la información de las películas
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String idSerie = String.valueOf(jsonObject.getInt("idSerie"));
                                String titulo = jsonObject.getString("titulo");
                                String temporadas = String.valueOf(jsonObject.getInt("temporadas"));
                                String plataforma = jsonObject.getString("plataforma");
                                String imagen = jsonObject.getString("imagen");
                                String puntuacion = jsonObject.getString("puntuacion");
                                String sinopsis = jsonObject.getString("sinopsis");
                                String enEmision = jsonObject.getString("enEmision");
                                String genero = jsonObject.getString("genero");
                                String vista = jsonObject.getString("vista");

                                // Crear un objeto Movie y agregarlo a la lista
                                Show serie = new Show(idSerie, titulo, temporadas, plataforma, imagen, puntuacion, sinopsis,enEmision, genero, vista);

                                if(serie.getVista().equals("0")) {
                                    showList.add(serie);
                                }
                            }

                            // Notificar al adaptador que los datos han cambiado
                            showAdapter.notifyDataSetChanged();

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
    }
}