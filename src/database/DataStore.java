package database;

import model.Piloto;
import model.Equipo;
import model.Circuito;
import model.Vehiculo;

import java.util.*;

// Base de datos en memoria para el simulador
public class DataStore {
    private static Map<Integer, Piloto> pilotos = new HashMap<>();
    private static Map<String, Equipo> equipos = new HashMap<>();
    private static Map<String, Circuito> circuitos = new HashMap<>();
    private static Map<String, Vehiculo> vehiculos = new HashMap<>();

    static {
        // Inicializar pilotos
        addPiloto(new Piloto(1, "Max Verstappen", "Red Bull Racing", "Líder", "https://upload.wikimedia.org/wikipedia/commons/8/89/Max_Verstappen_2023_Austria_FP2_%28cropped%29.jpg"));
        addPiloto(new Piloto(2, "Sergio Pérez", "Red Bull Racing", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/4/4e/Sergio_Perez_2022_Imola.jpg"));
        addPiloto(new Piloto(3, "Lewis Hamilton", "Mercedes-AMG Petronas", "Líder", "https://upload.wikimedia.org/wikipedia/commons/8/87/Lewis_Hamilton_2022_Imola.jpg"));
        addPiloto(new Piloto(4, "George Russell", "Mercedes-AMG Petronas", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/3/3d/George_Russell_2022_Imola.jpg"));
        addPiloto(new Piloto(5, "Charles Leclerc", "Ferrari", "Líder", "https://upload.wikimedia.org/wikipedia/commons/5/52/Charles_Leclerc_2022_Imola_%28cropped%29.jpg"));
        addPiloto(new Piloto(6, "Carlos Sainz", "Ferrari", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/a/af/Carlos_Sainz_Jr._2022_Imola.jpg"));
        addPiloto(new Piloto(7, "Lando Norris", "McLaren", "Líder", "https://upload.wikimedia.org/wikipedia/commons/5/50/Lando_Norris_2022_Imola.jpg"));
        addPiloto(new Piloto(8, "Oscar Piastri", "McLaren", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/4/4b/Oscar_Piastri_2023.jpg"));
        addPiloto(new Piloto(9, "Fernando Alonso", "Aston Martin", "Líder", "https://upload.wikimedia.org/wikipedia/commons/8/85/Fernando_Alonso_2022_Imola.jpg"));
        addPiloto(new Piloto(10, "Lance Stroll", "Aston Martin", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/7/75/Lance_Stroll_2022_Imola.jpg"));
        addPiloto(new Piloto(11, "Esteban Ocon", "Alpine", "Líder", "https://upload.wikimedia.org/wikipedia/commons/2/2a/Esteban_Ocon_2022_Imola.jpg"));
        addPiloto(new Piloto(12, "Pierre Gasly", "Alpine", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/b/b3/Pierre_Gasly_2022_Imola.jpg"));
        addPiloto(new Piloto(13, "Valtteri Bottas", "Alfa Romeo", "Líder", "https://upload.wikimedia.org/wikipedia/commons/c/c5/Valtteri_Bottas_2022_Imola_%28cropped%29.jpg"));
        addPiloto(new Piloto(14, "Zhou Guanyu", "Alfa Romeo", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/5/5b/Zhou_Guanyu_2022_Imola_%28cropped%29.jpg"));
        addPiloto(new Piloto(15, "Kevin Magnussen", "Haas", "Líder", "https://upload.wikimedia.org/wikipedia/commons/1/1a/Kevin_Magnussen_2022_Imola.jpg"));
        addPiloto(new Piloto(16, "Nico Hülkenberg", "Haas", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/4/4f/Nico_H%C3%BClkenberg_2020.jpg"));
        addPiloto(new Piloto(17, "Yuki Tsunoda", "AlphaTauri", "Líder", "https://upload.wikimedia.org/wikipedia/commons/0/00/Yuki_Tsunoda_2022_Imola.jpg"));
        addPiloto(new Piloto(18, "Daniel Ricciardo", "AlphaTauri", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/8/87/Daniel_Ricciardo_2022_Imola.jpg"));
        addPiloto(new Piloto(19, "Alexander Albon", "Williams", "Líder", "https://upload.wikimedia.org/wikipedia/commons/7/72/Alexander_Albon_2022_Imola.jpg"));
        addPiloto(new Piloto(20, "Logan Sargeant", "Williams", "Escudero", "https://upload.wikimedia.org/wikipedia/commons/0/07/Logan_Sargeant_2023_F1_United_States_GP.jpg"));

        // Inicializar equipos
        addEquipo(new Equipo("Red Bull Racing", "Austria", "Honda", Arrays.asList(1, 2), ""));
        addEquipo(new Equipo("Mercedes-AMG Petronas", "Alemania", "Mercedes", Arrays.asList(3, 4), ""));
        addEquipo(new Equipo("Ferrari", "Italia", "Ferrari", Arrays.asList(5, 6), ""));
        addEquipo(new Equipo("McLaren", "Reino Unido", "Mercedes", Arrays.asList(7, 8), ""));
        addEquipo(new Equipo("Aston Martin", "Reino Unido", "Mercedes", Arrays.asList(9, 10), ""));
        addEquipo(new Equipo("Alpine", "Francia", "Renault", Arrays.asList(11, 12), ""));
        addEquipo(new Equipo("Alfa Romeo", "Suiza", "Ferrari", Arrays.asList(13, 14), ""));
        addEquipo(new Equipo("Haas", "Estados Unidos", "Ferrari", Arrays.asList(15, 16), ""));
        addEquipo(new Equipo("AlphaTauri", "Italia", "Honda", Arrays.asList(17, 18), ""));
        addEquipo(new Equipo("Williams", "Reino Unido", "Mercedes", Arrays.asList(19, 20), ""));

        // Inicializar circuitos
        // Monaco
        Circuito.Record monacoRecord = new Circuito.Record("1:10.166", "Lewis Hamilton", 2019);
        List<Circuito.Ganador> monacoGanadores = Arrays.asList(
            new Circuito.Ganador(2021, 1),
            new Circuito.Ganador(2022, 2),
            new Circuito.Ganador(2023, 1)
        );
        addCircuito(new Circuito("Circuito de Mónaco", "Mónaco", 3.34, 78, 
            "Uno de los circuitos más prestigiosos y difíciles del calendario, conocido por sus calles angostas.", 
            monacoRecord, monacoGanadores, "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Monte_Carlo_Formula_1_track_map.svg/320px-Monte_Carlo_Formula_1_track_map.svg.png"));

        // Silverstone
        Circuito.Record silverstoneRecord = new Circuito.Record("1:27.097", "Max Verstappen", 2020);
        List<Circuito.Ganador> silverstoneGanadores = Arrays.asList(
            new Circuito.Ganador(2021, 3),
            new Circuito.Ganador(2022, 5),
            new Circuito.Ganador(2023, 1)
        );
        addCircuito(new Circuito("Silverstone", "Reino Unido", 5.89, 52, 
            "Uno de los circuitos más rápidos del calendario, con curvas de alta velocidad como Maggotts.", 
            silverstoneRecord, silverstoneGanadores, "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Silverstone_Circuit_2020_layout.png/320px-Silverstone_Circuit_2020_layout.png"));

        // Spa-Francorchamps
        Circuito.Record spaRecord = new Circuito.Record("1:46.286", "Valtteri Bottas", 2018);
        List<Circuito.Ganador> spaGanadores = Arrays.asList(
            new Circuito.Ganador(2021, 1),
            new Circuito.Ganador(2022, 1),
            new Circuito.Ganador(2023, 1)
        );
        addCircuito(new Circuito("Spa-Francorchamps", "Bélgica", 7.00, 44, 
            "Uno de los circuitos históricos más desafiantes, famoso por curvas rápidas y desniveles como Eau Rouge.", 
            spaRecord, spaGanadores, "https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/Spa-Francorchamps_of_Belgium.svg/320px-Spa-Francorchamps_of_Belgium.svg.png"));

        // Monza
        Circuito.Record monzaRecord = new Circuito.Record("1:18.887", "Lewis Hamilton", 2020);
        List<Circuito.Ganador> monzaGanadores = Arrays.asList(
            new Circuito.Ganador(2021, 3),
            new Circuito.Ganador(2022, 1),
            new Circuito.Ganador(2023, 1)
        );
        addCircuito(new Circuito("Monza", "Italia", 5.79, 53, 
            "Conocido como el Templo de la Velocidad, es el circuito más rápido y con menos curvas del calendario.", 
            monzaRecord, monzaGanadores, "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Monza_of_Italy.svg/320px-Monza_of_Italy.svg.png"));

        // Suzuka
        Circuito.Record suzukaRecord = new Circuito.Record("1:30.983", "Lewis Hamilton", 2019);
        List<Circuito.Ganador> suzukaGanadores = Arrays.asList(
            new Circuito.Ganador(2021, 3),
            new Circuito.Ganador(2022, 1),
            new Circuito.Ganador(2023, 1)
        );
        addCircuito(new Circuito("Suzuka", "Japón", 5.81, 53, 
            "El único circuito con diseño en forma de 8, famoso por curvas de alta dificultad técnica como las Eses.", 
            suzukaRecord, suzukaGanadores, "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Suzuka_of_Japan.svg/320px-Suzuka_of_Japan.svg.png"));
    }

    private static void addPiloto(Piloto p) { pilotos.put(p.getId(), p); }
    private static void addEquipo(Equipo e) { equipos.put(e.getNombre(), e); }
    private static void addCircuito(Circuito c) { circuitos.put(c.getNombre(), c); }

    public static Collection<Piloto> getPilotos() { return pilotos.values(); }
    public static Piloto getPiloto(int id) { return pilotos.get(id); }
    public static Collection<Equipo> getEquipos() { return equipos.values(); }
    public static Collection<Circuito> getCircuitos() { return circuitos.values(); }
}
