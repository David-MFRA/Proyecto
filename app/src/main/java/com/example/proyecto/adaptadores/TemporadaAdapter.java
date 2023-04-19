package com.example.proyecto.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.Episodios;
import com.example.proyecto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TemporadaAdapter extends BaseAdapter {
    private Context context;
    private JSONArray temporadaList;
    private String serieId;
    private String nombre;

    public TemporadaAdapter(Context context, JSONArray temporadaList, String serieId, String nombre) {
        this.context = context;
        this.temporadaList = temporadaList;
        this.serieId = serieId;
        this.nombre = nombre;
    }

    @Override
    public int getCount() {
        return temporadaList.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return temporadaList.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        try {
            return temporadaList.getJSONObject(position).getInt("idTemporada");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener la temporada actual
        JSONObject temporada = (JSONObject) getItem(position);

        // Inflar el layout de la celda de la temporada
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.temporada_item, null);
        }

        // Obtener las vistas de la celda de la temporada
        TextView textViewTituloTemporada = convertView.findViewById(R.id.textViewTituloTemporada);
        TextView textViewEpisodiosTemporada = convertView.findViewById(R.id.textViewEpisodiosTemporada);

        try {
            // Obtener los datos de la temporada actual
            String tituloTemporada = temporada.getString("nombreTemporada");
            int numeroEpisodios = temporada.getInt("episodeCount");

            // Mostrar los datos en las vistas de la celda de la temporada
            textViewTituloTemporada.setText(tituloTemporada);
            textViewEpisodiosTemporada.setText(numeroEpisodios + " episodios");

            // Agregar un listener de clics en la celda de la temporada
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crear un intent para abrir la actividad de la lista de episodios
                    Intent intent = new Intent(context, Episodios.class);

                    // Agregar el identificador de la temporada como extra en el intent
                    String temporadaId = null;
                    try {
                        temporadaId = temporada.getString("idTemporada");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    intent.putExtra("temporadaId", temporadaId);

                    // Iniciar la actividad de la lista de episodios
                    context.startActivity(intent);
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

