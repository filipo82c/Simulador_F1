package model;

import java.util.List;
import java.util.Map;

// Datos del monoplaza y su rendimiento
public class Vehiculo {
    private String equipo;
    private String modelo;
    private String motor;
    private int velocidadMaximaKmh;
    private double aceleracion0100;
    private List<Integer> pilotosIds;
    private Map<String, ModoRendimiento> rendimiento;

    public Vehiculo(String equipo, String modelo, String motor, int velocidadMaximaKmh, double aceleracion0100, List<Integer> pilotosIds, Map<String, ModoRendimiento> rendimiento) {
        this.equipo = equipo;
        this.modelo = modelo;
        this.motor = motor;
        this.velocidadMaximaKmh = velocidadMaximaKmh;
        this.aceleracion0100 = aceleracion0100;
        this.pilotosIds = pilotosIds;
        this.rendimiento = rendimiento;
    }

    public String getEquipo() { return equipo; }
    public String getModelo() { return modelo; }
    public String getMotor() { return motor; }
    public int getVelocidadMaximaKmh() { return velocidadMaximaKmh; }
    public double getAceleracion0100() { return aceleracion0100; }
    public List<Integer> getPilotosIds() { return pilotosIds; }
    public Map<String, ModoRendimiento> getRendimiento() { return rendimiento; }

    // Para configurar los modos de rendimiento
    public static class ModoRendimiento {
        private int velocidadPromedioKmh;
        private Map<String, Double> consumoCombustible;
        private Map<String, Double> desgasteNeumaticos;

        public ModoRendimiento(int velocidadPromedioKmh, Map<String, Double> consumoCombustible, Map<String, Double> desgasteNeumaticos) {
            this.velocidadPromedioKmh = velocidadPromedioKmh;
            this.consumoCombustible = consumoCombustible;
            this.desgasteNeumaticos = desgasteNeumaticos;
        }

        public int getVelocidadPromedioKmh() { return velocidadPromedioKmh; }
        public Map<String, Double> getConsumoCombustible() { return consumoCombustible; }
        public Map<String, Double> getDesgasteNeumaticos() { return desgasteNeumaticos; }
    }
}
