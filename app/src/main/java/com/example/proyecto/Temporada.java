package com.example.proyecto;

public class Temporada {

    private String idTemporada;
    private String numTemporada;
    private String numEpisodios;
    private String idSerie;

    public Temporada(String idTemporada, String numTemporada, String numEpisodios, String idSerie) {
        this.idTemporada = idTemporada;
        this.numTemporada = numTemporada;
        this.numEpisodios = numEpisodios;
        this.idSerie = idSerie;
    }

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
}
