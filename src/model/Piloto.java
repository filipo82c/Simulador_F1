package model;

// Datos de cada piloto
public class Piloto {
    private int id;
    private String nombre;
    private String equipo;
    private String rol;
    private String imagenUrl;

    public Piloto(int id, String nombre, String equipo, String rol, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.equipo = equipo;
        this.rol = rol;
        this.imagenUrl = imagenUrl;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEquipo() { return equipo; }
    public String getRol() { return rol; }
    public String getImagenUrl() { return imagenUrl; }
}
