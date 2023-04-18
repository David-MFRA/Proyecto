package com.example.proyecto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Show  implements Parcelable {
    private String idSerie;
    private String titulo;
    private String temporadas;
    private String plataforma;
    private String imagen;
    private String puntuacion;
    private String sinopsis;
    private String enEmision;
    private String genero;

    public Show(String idSerie, String titulo, String temporadas, String plataforma, String imagen, String puntuacion, String sinopsis, String enEmision, String genero) {
        this.idSerie = idSerie;
        this.titulo = titulo;
        this.temporadas = temporadas;
        this.plataforma = plataforma;
        this.imagen = imagen;
        this.puntuacion = puntuacion;
        this.sinopsis = sinopsis;
        this.enEmision = enEmision;
        this.genero = genero;
    }

    protected Show(Parcel in) {
        idSerie = in.readString();
        titulo = in.readString();
        temporadas = in.readString();
        plataforma = in.readString();
        imagen = in.readString();
        puntuacion = in.readString();
        sinopsis = in.readString();
        enEmision = in.readString();
        genero = in.readString();
    }

    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };

    public String getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(String idSerie) {
        this.idSerie = idSerie;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(String temporadas) {
        this.temporadas = temporadas;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getEnEmision() {
        return enEmision;
    }

    public void setEnEmision(String enEmision) {
        this.enEmision = enEmision;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idSerie);
        parcel.writeString(titulo);
        parcel.writeString(temporadas);
        parcel.writeString(plataforma);
        parcel.writeString(imagen);
        parcel.writeString(puntuacion);
        parcel.writeString(sinopsis);
        parcel.writeString(enEmision);
        parcel.writeString(genero);
    }
}
