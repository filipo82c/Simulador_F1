package model;

import java.util.List;

// Datos de cada equipo
public class Equipo {
    private String nombre;
    private String pais;
    private String motor;
    private List<Integer> pilotosIds;
    private String imagenUrl;

    public Equipo(String nombre, String pais, String motor, List<Integer> pilotosIds, String imagenUrl) {
        this.nombre = nombre;
        this.pais = pais;
        this.motor = motor;
        this.pilotosIds = pilotosIds;
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() { return nombre; }
    public String getPais() { return pais; }
    public String getMotor() { return motor; }
    public List<Integer> getPilotosIds() { return pilotosIds; }
    public String getImagenUrl() { return imagenUrl; }
}
