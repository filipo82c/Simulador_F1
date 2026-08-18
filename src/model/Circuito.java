package model;

import java.util.List;

// Datos de cada circuito
public class Circuito {
    private String nombre;
    private String pais;
    private double longitudKm;
    private int vueltas;
    private String descripcion;
    private Record recordVuelta;
    private List<Ganador> ganadores;
    private String imagenUrl;

    public Circuito(String nombre, String pais, double longitudKm, int vueltas, String descripcion, Record recordVuelta, List<Ganador> ganadores, String imagenUrl) {
        this.nombre = nombre;
        this.pais = pais;
        this.longitudKm = longitudKm;
        this.vueltas = vueltas;
        this.descripcion = descripcion;
        this.recordVuelta = recordVuelta;
        this.ganadores = ganadores;
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() { return nombre; }
    public String getPais() { return pais; }
    public double getLongitudKm() { return longitudKm; }
    public int getVueltas() { return vueltas; }
    public String getDescripcion() { return descripcion; }
    public Record getRecordVuelta() { return recordVuelta; }
    public List<Ganador> getGanadores() { return ganadores; }
    public String getImagenUrl() { return imagenUrl; }

    // Para el record de vuelta
    public static class Record {
        private String tiempo;
        private String piloto;
        private int anio;

        public Record(String tiempo, String piloto, int anio) {
            this.tiempo = tiempo;
            this.piloto = piloto;
            this.anio = anio;
        }

        public String getTiempo() { return tiempo; }
        public String getPiloto() { return piloto; }
        public int getAnio() { return anio; }
    }

    // Para los ganadores historicos
    public static class Ganador {
        private int temporada;
        private int pilotoId;

        public Ganador(int temporada, int pilotoId) {
            this.temporada = temporada;
            this.pilotoId = pilotoId;
        }

        public int getTemporada() { return temporada; }
        public int getPilotoId() { return pilotoId; }
    }
}
