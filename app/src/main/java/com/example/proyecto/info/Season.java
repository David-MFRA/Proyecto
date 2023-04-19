package com.example.proyecto.info;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Season implements Parcelable{

    private String idTemporada;
    private String numTemporada;
    private String numEpisodios;
    private String idSerie;
    private String nombreTemporada;
    private String airDate;
    private String episodeCount;
    private String overview;
    private String posterPath;

    public Season(String idTemporada, String nombreTemporada, String numTemporada, String numEpisodios, String idSerie, String airDate, String episodeCount, String overview, String posterPath) {
        this.idTemporada = idTemporada;
        this.numTemporada = numTemporada;
        this.numEpisodios = numEpisodios;
        this.idSerie = idSerie;
        this.nombreTemporada = nombreTemporada;
        this.airDate = airDate;
        this.episodeCount = episodeCount;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    protected Season(Parcel in) {
        idTemporada = in.readString();
        numTemporada = in.readString();
        numEpisodios = in.readString();
        idSerie = in.readString();
        nombreTemporada = in.readString();
        airDate = in.readString();
        episodeCount = in.readString();
        overview = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<Season> CREATOR = new Creator<Season>() {
        @Override
        public Season createFromParcel(Parcel in) {
            return new Season(in);
        }

        @Override
        public Season[] newArray(int size) {
            return new Season[size];
        }
    };

    public String getIdTemporada() {
        return idTemporada;
    }

    public void setIdTemporada(String idTemporada) {
        this.idTemporada = idTemporada;
    }

    public String getNumTemporada() {
        return numTemporada;
    }

    public void setNumTemporada(String numTemporada) {
        this.numTemporada = numTemporada;
    }

    public String getNumEpisodios() {
        return numEpisodios;
    }

    public void setNumEpisodios(String numEpisodios) {
        this.numEpisodios = numEpisodios;
    }

    public String getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(String idSerie) {
        this.idSerie = idSerie;
    }

    public String getNombreTemporada() {
        return nombreTemporada;
    }

    public void setNombreTemporada(String nombreTemporada) {
        this.nombreTemporada = nombreTemporada;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(String episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idTemporada);
        parcel.writeString(numTemporada);
        parcel.writeString(numEpisodios);
        parcel.writeString(idSerie);
        parcel.writeString(nombreTemporada);
        parcel.writeString(airDate);
        parcel.writeString(episodeCount);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
    }
}