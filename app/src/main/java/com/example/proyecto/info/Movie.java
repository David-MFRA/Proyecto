package com.example.proyecto.info;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Movie implements Parcelable {

    private String idPelicula;
    private String titulo;
    private String imagen;
    private String duracion;
    private String fecha;
    private String genero;
    private String vista;


    public Movie(String idPelicula, String titulo, String imagen, String duracion, String fecha, String genero, String vista) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.imagen = imagen;
        this.duracion = duracion;
        this.fecha = fecha;
        this.genero = genero;
        this.vista = vista;
    }

    public Movie(String movieId, String movieTitle, String movieReleaseDate) {
        this.idPelicula = movieId;
        this.titulo = movieTitle;
        this.fecha = movieReleaseDate;
    }


    protected Movie(Parcel in) {
        idPelicula = in.readString();
        titulo = in.readString();
        imagen = in.readString();
        duracion = in.readString();
        fecha = in.readString();
        genero = in.readString();
        vista = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getVista() {
        return vista;
    }

    public void setVista(String vista) {
        this.vista = vista;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idPelicula);
        parcel.writeString(titulo);
        parcel.writeString(imagen);
        parcel.writeString(duracion);
        parcel.writeString(fecha);
        parcel.writeString(genero);
        parcel.writeString(vista);
    }
}
