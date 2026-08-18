package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.PrintWriter;
import database.DataStore;
import model.Equipo;
import model.Piloto;
import model.Circuito;

// Ventana principal con interfaz de F1
public class MainFrame extends JFrame {
    // Para ignorar certificados SSL caducados o desconocidos
    static {
        try {
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{
                new javax.net.ssl.X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                }
            };
            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            javax.net.ssl.HostnameVerifier allHostsValid = new javax.net.ssl.HostnameVerifier() {
                public boolean verify(String hostname, javax.net.ssl.SSLSession session) { return true; }
            };
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel dashboardHistoryContainer;

    // Colores premium F1
    private final Color bgMain = new Color(11, 13, 16);
    private final Color bgCard = new Color(21, 24, 30);
    private final Color bgSidebar = new Color(15, 17, 21);
    private final Color redF1 = new Color(225, 6, 0);
    private final Color textPrimary = Color.WHITE;
    private final Color textSecondary = new Color(148, 163, 184);

    public MainFrame() {
        // Configuracion basica
        setTitle("Simulador F1");
        setSize(1150, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Para inicializar los paneles principales
        initSidebar();
        initContentPanel();

        // Para añadir los paneles a la ventana
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    // Para configurar la barra lateral
    private void initSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(bgSidebar);
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(39, 39, 42)));

        // Logo de F1 estilizado en texto
        JLabel logoLabel = new JLabel("SIMULADOR F1");
        logoLabel.setForeground(redF1);
        logoLabel.setFont(new Font("Outfit", Font.BOLD, 22));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 40, 0));
        sidebarPanel.add(logoLabel);

        // Botones del menu
        String[] menuItems = {"Dashboard", "Equipos & Pilotos", "Circuitos", "Simulación"};
        String[] cardNames = {"dashboard", "equipos", "circuitos", "simulador"};

        for (int i = 0; i < menuItems.length; i++) {
            final String cardName = cardNames[i];
            JButton menuBtn = new JButton(menuItems[i]);
            menuBtn.setMaximumSize(new Dimension(220, 45));
            menuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuBtn.setForeground(textSecondary);
            menuBtn.setBackground(bgSidebar);
            menuBtn.setFont(new Font("Outfit", Font.PLAIN, 15));
            menuBtn.setFocusPainted(false);
            menuBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Para cambiar de pestania al pulsar
            menuBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, cardName);
                    if (cardName.equals("dashboard")) {
                        refreshDashboardHistory();
                    }
                }
            });

            sidebarPanel.add(menuBtn);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    // Para configurar los paneles de contenido
    private void initContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(bgMain);

        // Pestania 1: Dashboard
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(bgMain);
        dashboard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel northPanel = new JPanel();
        northPanel.setBackground(bgMain);
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Simulador F1");
        title.setForeground(textPrimary);
        title.setFont(new Font("Outfit", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        northPanel.add(title);

        JPanel welcomeCard = new JPanel();
        welcomeCard.setBackground(bgCard);
        welcomeCard.setBorder(BorderFactory.createLineBorder(new Color(39, 39, 42)));
        welcomeCard.setLayout(new BoxLayout(welcomeCard, BoxLayout.Y_AXIS));
        welcomeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        welcomeCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel("Bienvenido al Box");
        welcomeTitle.setForeground(textPrimary);
        welcomeTitle.setFont(new Font("Outfit", Font.BOLD, 18));
        welcomeTitle.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        JLabel welcomeDesc = new JLabel("Gestiona escuderías, configura reglajes de monoplazas y simula carreras en vivo.");
        welcomeDesc.setForeground(textSecondary);
        welcomeDesc.setFont(new Font("Outfit", Font.PLAIN, 14));
        welcomeDesc.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));

        welcomeCard.add(welcomeTitle);
        welcomeCard.add(welcomeDesc);
        northPanel.add(welcomeCard);
        dashboard.add(northPanel, BorderLayout.NORTH);

        // Centro del Dashboard: Estadísticas e historial
        dashboard.add(createDashboardCenterPanel(), BorderLayout.CENTER);

        // Pestaña Equipos y Pilotos dinamico
        JPanel equipos = new JPanel(new BorderLayout());
        equipos.setBackground(bgMain);
        equipos.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel eqTitle = new JLabel("Escuderías y Pilotos");
        eqTitle.setForeground(textPrimary);
        eqTitle.setFont(new Font("Outfit", Font.BOLD, 22));
        eqTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        equipos.add(eqTitle, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        gridPanel.setBackground(bgMain);

        for (Equipo eq : DataStore.getEquipos()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(bgCard);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(39, 39, 42)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            // Color del equipo en el borde izquierdo
            Color teamColor = getTeamColor(eq.getNombre());
            JPanel colorBar = new JPanel();
            colorBar.setBackground(teamColor);
            colorBar.setPreferredSize(new Dimension(5, 0));
            card.add(colorBar, BorderLayout.WEST);

            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setBackground(bgCard);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

            JLabel nameLabel = new JLabel(eq.getNombre() + " (" + eq.getPais() + ")");
            nameLabel.setForeground(textPrimary);
            nameLabel.setFont(new Font("Outfit", Font.BOLD, 16));

            JLabel motorLabel = new JLabel("Motor: " + eq.getMotor());
            motorLabel.setForeground(textSecondary);
            motorLabel.setFont(new Font("Outfit", Font.PLAIN, 13));

            infoPanel.add(nameLabel);
            infoPanel.add(motorLabel);
            card.add(infoPanel, BorderLayout.CENTER);

            // Pilotos
            JPanel pilotsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            pilotsPanel.setBackground(bgCard);

            for (int pId : eq.getPilotosIds()) {
                Piloto p = DataStore.getPiloto(pId);
                if (p != null) {
                    JPanel pBadge = new JPanel(new GridBagLayout());
                    pBadge.setBackground(bgMain);
                    pBadge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                    JLabel pLabel = new JLabel(p.getNombre() + " (" + p.getRol() + ")");
                    pLabel.setForeground(textPrimary);
                    pLabel.setFont(new Font("Outfit", Font.PLAIN, 12));

                    pBadge.add(pLabel);
                    pilotsPanel.add(pBadge);
                }
            }
            card.add(pilotsPanel, BorderLayout.EAST);
            gridPanel.add(card);
        }

        JPanel eqWrapper = new JPanel(new BorderLayout());
        eqWrapper.setBackground(bgMain);
        eqWrapper.add(gridPanel, BorderLayout.NORTH);
        JScrollPane eqScroll = new JScrollPane(eqWrapper);
        eqScroll.setBorder(null);
        eqScroll.getVerticalScrollBar().setUnitIncrement(16);
        equipos.add(eqScroll, BorderLayout.CENTER);

        // Pestaña Circuitos dinamico
        JPanel circuitos = new JPanel(new BorderLayout());
        circuitos.setBackground(bgMain);
        circuitos.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel circTitle = new JLabel("Circuitos de Carrera");
        circTitle.setForeground(textPrimary);
        circTitle.setFont(new Font("Outfit", Font.BOLD, 22));
        circTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        circuitos.add(circTitle, BorderLayout.NORTH);

        JPanel circGrid = new JPanel(new GridLayout(0, 1, 15, 15));
        circGrid.setBackground(bgMain);

        for (Circuito c : DataStore.getCircuitos()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(bgCard);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(39, 39, 42)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JPanel mainInfo = new JPanel(new GridLayout(3, 1));
            mainInfo.setBackground(bgCard);
            mainInfo.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

            JLabel nameLabel = new JLabel(c.getNombre() + " (" + c.getPais() + ")");
            nameLabel.setForeground(textPrimary);
            nameLabel.setFont(new Font("Outfit", Font.BOLD, 16));

            JLabel specLabel = new JLabel("Longitud: " + c.getLongitudKm() + " km | Vueltas: " + c.getVueltas());
            specLabel.setForeground(textSecondary);
            specLabel.setFont(new Font("Outfit", Font.PLAIN, 13));

            JLabel descLabel = new JLabel(c.getDescripcion());
            descLabel.setForeground(textSecondary);
            descLabel.setFont(new Font("Outfit", Font.ITALIC, 12));

            mainInfo.add(nameLabel);
            mainInfo.add(specLabel);
            mainInfo.add(descLabel);
            card.add(mainInfo, BorderLayout.CENTER);

            // Record
            JPanel recordPanel = new JPanel(new GridLayout(2, 1));
            recordPanel.setBackground(bgCard);

            JLabel recTitle = new JLabel("Récord de Vuelta", SwingConstants.RIGHT);
            recTitle.setForeground(redF1);
            recTitle.setFont(new Font("Outfit", Font.BOLD, 12));

            Circuito.Record r = c.getRecordVuelta();
            JLabel recValue = new JLabel(r.getTiempo() + " (" + r.getPiloto() + ", " + r.getAnio() + ")", SwingConstants.RIGHT);
            recValue.setForeground(textPrimary);
            recValue.setFont(new Font("Outfit", Font.PLAIN, 12));

            recordPanel.add(recTitle);
            recordPanel.add(recValue);
            card.add(recordPanel, BorderLayout.EAST);

            circGrid.add(card);
        }

        JPanel circWrapper = new JPanel(new BorderLayout());
        circWrapper.setBackground(bgMain);
        circWrapper.add(circGrid, BorderLayout.NORTH);
        JScrollPane circScroll = new JScrollPane(circWrapper);
        circScroll.setBorder(null);
        circScroll.getVerticalScrollBar().setUnitIncrement(16);
        circuitos.add(circScroll, BorderLayout.CENTER);

        // Pestaña Simulador y Reglajes
        JPanel simulador = new JPanel(new BorderLayout());
        simulador.setBackground(bgMain);
        simulador.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel simTitle = new JLabel("Simulación y Telemetría");
        simTitle.setForeground(textPrimary);
        simTitle.setFont(new Font("Outfit", Font.BOLD, 22));
        simTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        simulador.add(simTitle, BorderLayout.NORTH);

        // Contenedor de tres columnas
        JPanel simBody = new JPanel(new BorderLayout(15, 0));
        simBody.setBackground(bgMain);

        // Columna 1: Formulario de reglajes
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(bgCard);
        leftPanel.setPreferredSize(new Dimension(260, 0));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 39, 42)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        leftPanel.setLayout(new GridLayout(0, 1, 5, 5));

        leftPanel.add(createFormLabel("Selecciona Piloto"));
        JComboBox<String> pilotoCombo = new JComboBox<>();
        pilotoCombo.setBackground(bgMain);
        pilotoCombo.setForeground(textPrimary);
        pilotoCombo.setFont(new Font("Outfit", Font.PLAIN, 13));
        for (Piloto p : DataStore.getPilotos()) {
            pilotoCombo.addItem(p.getNombre());
        }
        leftPanel.add(pilotoCombo);

        leftPanel.add(createFormLabel("Selecciona Circuito"));
        JComboBox<String> circuitoCombo = new JComboBox<>();
        circuitoCombo.setBackground(bgMain);
        circuitoCombo.setForeground(textPrimary);
        circuitoCombo.setFont(new Font("Outfit", Font.PLAIN, 13));
        for (Circuito c : DataStore.getCircuitos()) {
            circuitoCombo.addItem(c.getNombre());
        }
        leftPanel.add(circuitoCombo);

        leftPanel.add(createFormLabel("Carga Aerodinámica"));
        String[] aeroOptions = {"Baja", "Media", "Alta"};
        JComboBox<String> aeroCombo = new JComboBox<>(aeroOptions);
        aeroCombo.setBackground(bgMain);
        aeroCombo.setForeground(textPrimary);
        aeroCombo.setFont(new Font("Outfit", Font.PLAIN, 13));
        leftPanel.add(aeroCombo);

        leftPanel.add(createFormLabel("Presión de Neumáticos"));
        String[] tireOptions = {"Baja", "Estándar", "Alta"};
        JComboBox<String> tireCombo = new JComboBox<>(tireOptions);
        tireCombo.setBackground(bgMain);
        tireCombo.setForeground(textPrimary);
        tireCombo.setFont(new Font("Outfit", Font.PLAIN, 13));
        leftPanel.add(tireCombo);

        leftPanel.add(createFormLabel("Modo de Conducción"));
        String[] driveOptions = {"Normal", "Agresiva", "Ahorro de combustible"};
        JComboBox<String> driveCombo = new JComboBox<>(driveOptions);
        driveCombo.setBackground(bgMain);
        driveCombo.setForeground(textPrimary);
        driveCombo.setFont(new Font("Outfit", Font.PLAIN, 13));
        leftPanel.add(driveCombo);

        leftPanel.add(createFormLabel("Estrategia de Combustible"));
        String[] fuelOptions = {"Balanceada", "Agresiva", "Ahorro"};
        JComboBox<String> fuelCombo = new JComboBox<>(fuelOptions);
        fuelCombo.setBackground(bgMain);
        fuelCombo.setForeground(textPrimary);
        fuelCombo.setFont(new Font("Outfit", Font.PLAIN, 13));
        leftPanel.add(fuelCombo);

        leftPanel.add(createFormLabel("Número de Vueltas"));
        String[] lapsOptions = {"5 Vueltas (Rápida)", "10 Vueltas", "20 Vueltas", "50 Vueltas"};
        JComboBox<String> lapsCombo = new JComboBox<>(lapsOptions);
        lapsCombo.setBackground(bgMain);
        lapsCombo.setForeground(textPrimary);
        lapsCombo.setFont(new Font("Outfit", Font.PLAIN, 13));
        leftPanel.add(lapsCombo);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JButton simularBtn = new JButton("Simular Carrera");
        simularBtn.setBackground(redF1);
        simularBtn.setForeground(Color.WHITE);
        simularBtn.setFont(new Font("Outfit", Font.BOLD, 15));
        simularBtn.setFocusPainted(false);
        simularBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        simularBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leftPanel.add(simularBtn);

        simBody.add(leftPanel, BorderLayout.WEST);

        // Columna 2: Fichas del piloto y del circuito
        JPanel middlePanel = new JPanel(new GridLayout(2, 1, 0, 15));
        middlePanel.setBackground(bgMain);
        middlePanel.setPreferredSize(new Dimension(280, 0));

        // Subpanel 2A: Ficha Piloto
        JPanel pilotCard = new JPanel();
        pilotCard.setBackground(bgCard);
        pilotCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 39, 42)),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        pilotCard.setLayout(new BoxLayout(pilotCard, BoxLayout.Y_AXIS));

        JLabel pCardHeader = new JLabel("Ficha de Piloto", SwingConstants.CENTER);
        pCardHeader.setForeground(redF1);
        pCardHeader.setFont(new Font("Outfit", Font.BOLD, 15));
        pCardHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        pilotCard.add(pCardHeader);
        pilotCard.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel photoLabel = new JLabel("Cargando foto...", SwingConstants.CENTER);
        photoLabel.setForeground(textSecondary);
        photoLabel.setPreferredSize(new Dimension(100, 100));
        photoLabel.setMinimumSize(new Dimension(100, 100));
        photoLabel.setMaximumSize(new Dimension(100, 100));
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        photoLabel.setBorder(BorderFactory.createLineBorder(new Color(39, 39, 42)));
        pilotCard.add(photoLabel);
        pilotCard.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel pilotNameLabel = new JLabel("", SwingConstants.CENTER);
        pilotNameLabel.setForeground(textPrimary);
        pilotNameLabel.setFont(new Font("Outfit", Font.BOLD, 16));
        pilotNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pilotCard.add(pilotNameLabel);

        JLabel pilotTeamLabel = new JLabel("", SwingConstants.CENTER);
        pilotTeamLabel.setForeground(textSecondary);
        pilotTeamLabel.setFont(new Font("Outfit", Font.PLAIN, 12));
        pilotTeamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pilotCard.add(pilotTeamLabel);

        middlePanel.add(pilotCard);

        // Subpanel 2B: Ficha Circuito
        JPanel circuitCard = new JPanel();
        circuitCard.setBackground(bgCard);
        circuitCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 39, 42)),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        circuitCard.setLayout(new BoxLayout(circuitCard, BoxLayout.Y_AXIS));

        JLabel cCardHeader = new JLabel("Trazado del Circuito", SwingConstants.CENTER);
        cCardHeader.setForeground(redF1);
        cCardHeader.setFont(new Font("Outfit", Font.BOLD, 15));
        cCardHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        circuitCard.add(cCardHeader);
        circuitCard.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel trackImageLabel = new JLabel("Cargando mapa...", SwingConstants.CENTER);
        trackImageLabel.setForeground(textSecondary);
        trackImageLabel.setPreferredSize(new Dimension(180, 110));
        trackImageLabel.setMinimumSize(new Dimension(180, 110));
        trackImageLabel.setMaximumSize(new Dimension(180, 110));
        trackImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackImageLabel.setBorder(BorderFactory.createLineBorder(new Color(39, 39, 42)));
        circuitCard.add(trackImageLabel);
        circuitCard.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel trackNameLabel = new JLabel("", SwingConstants.CENTER);
        trackNameLabel.setForeground(textPrimary);
        trackNameLabel.setFont(new Font("Outfit", Font.BOLD, 14));
        trackNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        circuitCard.add(trackNameLabel);

        middlePanel.add(circuitCard);

        simBody.add(middlePanel, BorderLayout.CENTER);

        // Columna 3: Tabla de posiciones de clasificación
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(bgCard);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 39, 42)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        rightPanel.setPreferredSize(new Dimension(480, 0));

        JLabel leaderHeader = new JLabel("Leaderboard - Tiempos de Carrera");
        leaderHeader.setForeground(textPrimary);
        leaderHeader.setFont(new Font("Outfit", Font.BOLD, 16));
        leaderHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        rightPanel.add(leaderHeader, BorderLayout.NORTH);

        String[] columns = {"Pos", "Piloto", "Escudería", "Tiempo", "Clima"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setBackground(bgMain);
        resultsTable.setForeground(textPrimary);
        resultsTable.setFont(new Font("Outfit", Font.PLAIN, 12));
        resultsTable.setGridColor(new Color(39, 39, 42));
        resultsTable.setRowHeight(23);
        resultsTable.getTableHeader().setBackground(bgCard);
        resultsTable.getTableHeader().setForeground(textPrimary);
        resultsTable.getTableHeader().setFont(new Font("Outfit", Font.BOLD, 12));
        resultsTable.setFillsViewportHeight(true);

        JScrollPane tableScroll = new JScrollPane(resultsTable);
        tableScroll.setBorder(null);
        tableScroll.getViewport().setBackground(bgCard);
        tableScroll.setBackground(bgCard);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        JLabel eventLogLabel = new JLabel("Esperando inicio de carrera...", SwingConstants.CENTER);
        eventLogLabel.setForeground(redF1);
        eventLogLabel.setFont(new Font("Outfit", Font.ITALIC, 13));
        eventLogLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        rightPanel.add(eventLogLabel, BorderLayout.SOUTH);

        simBody.add(rightPanel, BorderLayout.EAST);

        simulador.add(simBody, BorderLayout.CENTER);

        // Actualizar datos del piloto
        pilotoCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selName = (String) pilotoCombo.getSelectedItem();
                for (Piloto p : DataStore.getPilotos()) {
                    if (p.getNombre().equals(selName)) {
                        pilotNameLabel.setText(p.getNombre());
                        pilotTeamLabel.setText(p.getEquipo() + " - " + p.getRol());
                        updatePilotImage(photoLabel, String.valueOf(p.getId()), p.getNombre(), p.getEquipo(), p.getImagenUrl());
                        break;
                    }
                }
            }
        });

        // Actualizar datos del circuito
        circuitoCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selName = (String) circuitoCombo.getSelectedItem();
                for (Circuito c : DataStore.getCircuitos()) {
                    if (c.getNombre().equals(selName)) {
                        trackNameLabel.setText(c.getNombre());
                        String localKey;
                        if (selName.contains("Mónaco")) localKey = "monaco";
                        else if (selName.contains("Silverstone")) localKey = "silverstone";
                        else if (selName.contains("Spa")) localKey = "spa";
                        else if (selName.contains("Monza")) localKey = "monza";
                        else localKey = "suzuka";
                        updateTrackImage(trackImageLabel, localKey, c.getNombre(), c.getImagenUrl());
                        break;
                    }
                }
            }
        });

        // Simular carrera al pulsar el boton
        simularBtn.addActionListener(new ActionListener() {
            private javax.swing.Timer raceTimer;
            private int currentLap = 0;
            private int totalLaps = 5;
            private String clima;
            private String selectedPilotName;
            private String selectedCircuitName;
            private String selectedAero;
            private String selectedTires;
            private String selectedDrive;
            private String selectedFuel;
            private List<DriverState> grid;

            // Clase interna para guardar el estado de cada piloto en carrera
            class DriverState {
                String nombre;
                String equipo;
                double tiempoAcumulado;
                boolean retirado;
                String causaRetiro;

                DriverState(String nombre, String equipo) {
                    this.nombre = nombre;
                    this.equipo = equipo;
                    this.tiempoAcumulado = 0.0;
                    this.retirado = false;
                    this.causaRetiro = "";
                }
            }

            private double getStartingGridWeight(String name, String team) {
                double weight = 50.0;
                if (name.equals("Max Verstappen")) weight -= 15.0;
                else if (name.equals("Lewis Hamilton")) weight -= 10.0;
                else if (name.equals("Charles Leclerc")) weight -= 10.0;
                else if (name.equals("Lando Norris")) weight -= 8.0;

                if (team.contains("Red Bull")) weight -= 15.0;
                else if (team.contains("Mercedes")) weight -= 8.0;
                else if (team.contains("Ferrari")) weight -= 8.0;
                else if (team.contains("McLaren")) weight -= 6.0;

                weight += Math.random() * 20.0;
                return weight;
            }

            private void saveRaceHistory() {
                try (FileWriter fw = new FileWriter("historial.txt", true);
                     PrintWriter pw = new PrintWriter(fw)) {
                    pw.println("Carrera en " + selectedCircuitName + " - " + totalLaps + " Vueltas - Clima: " + clima);
                    pw.println("Reglajes de " + selectedPilotName + ": Aero=" + selectedAero + ", Neum=" + selectedTires + ", Conduccion=" + selectedDrive + ", Combustible=" + selectedFuel);
                    pw.println("Resultados Finales:");
                    int pos = 1;
                    for (DriverState d : grid) {
                        String timeStr;
                        if (d.retirado) {
                            timeStr = "RETIRADO (" + d.causaRetiro + ")";
                        } else {
                            int min = (int) (d.tiempoAcumulado / 60);
                            double seg = d.tiempoAcumulado - (min * 60);
                            timeStr = String.format("%d:%06.3f", min, seg).replace(',', '.');
                        }
                        pw.println(String.format("  %d. %s (%s) - %s", pos++, d.nombre, d.equipo, timeStr));
                    }
                    pw.println("------------------------------------------------------------------");
                } catch (Exception ex) {
                    System.out.println("Error al guardar historial de carrera");
                    ex.printStackTrace();
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                // Si la carrera ya esta corriendo, no hacer nada
                if (raceTimer != null && raceTimer.isRunning()) {
                    return;
                }

                selectedPilotName = (String) pilotoCombo.getSelectedItem();
                selectedCircuitName = (String) circuitoCombo.getSelectedItem();
                selectedAero = (String) aeroCombo.getSelectedItem();
                selectedTires = (String) tireCombo.getSelectedItem();
                selectedDrive = (String) driveCombo.getSelectedItem();
                selectedFuel = (String) fuelCombo.getSelectedItem();

                // Obtener numero de vueltas
                String selLapsStr = (String) lapsCombo.getSelectedItem();
                if (selLapsStr.contains("5 ")) totalLaps = 5;
                else if (selLapsStr.contains("10 ")) totalLaps = 10;
                else if (selLapsStr.contains("20 ")) totalLaps = 20;
                else totalLaps = 50;

                // Generar clima constante para toda la carrera
                double rand = Math.random();
                if (rand < 0.6) {
                    clima = "Seco";
                } else if (rand < 0.9) {
                    clima = "Lluvioso";
                } else {
                    clima = "Extremo";
                }

                // Inicializar parrilla de pilotos
                grid = new ArrayList<>();
                for (Piloto p : DataStore.getPilotos()) {
                    grid.add(new DriverState(p.getNombre(), p.getEquipo()));
                }

                // Generar un orden inicial (parrilla de salida aleatoria pero con peso de coche)
                Collections.sort(grid, new Comparator<DriverState>() {
                    @Override
                    public int compare(DriverState d1, DriverState d2) {
                        double val1 = getStartingGridWeight(d1.nombre, d1.equipo);
                        double val2 = getStartingGridWeight(d2.nombre, d2.equipo);
                        return Double.compare(val1, val2);
                    }
                });

                currentLap = 0;
                tableModel.setRowCount(0);
                leaderHeader.setText("Carrera Iniciada - Vuelta 0 de " + totalLaps + " [Clima: " + clima + "]");
                eventLogLabel.setText("¡Semáforos apagados! ¡Inicia la carrera en " + selectedCircuitName + "!");
                
                simularBtn.setEnabled(false);
                simularBtn.setText("En Carrera...");

                // Definir tiempos de base para circuitos
                double baseTime = selectedCircuitName.contains("Mónaco") ? 71.5 : 88.0;

                // Iniciar el temporizador por vuelta
                raceTimer = new javax.swing.Timer(800, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        currentLap++;
                        
                        if (currentLap > totalLaps) {
                            raceTimer.stop();
                            leaderHeader.setText("Carrera Completada - Resultados Finales");
                            eventLogLabel.setText("¡Bandera a cuadros! Ganador: " + grid.get(0).nombre);
                            simularBtn.setEnabled(true);
                            simularBtn.setText("Simular Carrera");
                            
                            // Guardar historial en archivo local
                            saveRaceHistory();
                            return;
                        }

                        leaderHeader.setText("Carrera en Vivo - Vuelta " + currentLap + " de " + totalLaps + " [Clima: " + clima + "]");

                        // Simular tiempos de la vuelta actual usando hilos individuales en paralelo para cada piloto
                        List<Thread> threads = new ArrayList<>();
                        for (DriverState d : grid) {
                            if (d.retirado) continue;

                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // Habilidad y coche
                                    double lapTime = baseTime;
                                    
                                    // Habilidad piloto
                                    if (d.nombre.equals("Max Verstappen")) lapTime -= 0.5;
                                    else if (d.nombre.equals("Lewis Hamilton")) lapTime -= 0.4;
                                    else if (d.nombre.equals("Charles Leclerc")) lapTime -= 0.4;
                                    else if (d.nombre.equals("Lando Norris")) lapTime -= 0.3;
                                    else if (d.nombre.equals("Fernando Alonso")) lapTime -= 0.3;

                                    // Escudería
                                    String team = d.equipo;
                                    if (team.contains("Red Bull")) lapTime -= 0.4;
                                    else if (team.contains("Mercedes")) lapTime -= 0.2;
                                    else if (team.contains("Ferrari")) lapTime -= 0.2;
                                    else if (team.contains("McLaren")) lapTime -= 0.1;
                                    else if (team.contains("Alpine")) lapTime += 0.2;
                                    else if (team.contains("Alfa Romeo")) lapTime += 0.3;
                                    else if (team.contains("Haas")) lapTime += 0.4;
                                    else if (team.contains("AlphaTauri")) lapTime += 0.4;
                                    else if (team.contains("Williams")) lapTime += 0.5;

                                    // Efecto de reglajes del usuario para el piloto del usuario
                                    if (d.nombre.equals(selectedPilotName)) {
                                        // Aero
                                        if (selectedCircuitName.contains("Mónaco")) {
                                            if (selectedAero.equals("Alta")) lapTime -= 0.6;
                                            else if (selectedAero.equals("Baja")) lapTime += 0.8;
                                        } else {
                                            if (selectedAero.equals("Baja")) lapTime -= 0.5;
                                            else if (selectedAero.equals("Alta")) lapTime += 0.6;
                                        }
                                        // Neumáticos
                                        if (clima.equals("Seco")) {
                                            if (selectedTires.equals("Alta")) lapTime -= 0.2;
                                        } else {
                                            if (selectedTires.equals("Baja")) lapTime -= 0.3;
                                            else if (selectedTires.equals("Alta")) lapTime += 0.6;
                                        }
                                        // Conducción
                                        if (selectedDrive.equals("Agresiva")) lapTime -= 0.4;
                                        else if (selectedDrive.equals("Ahorro de combustible")) lapTime += 0.6;
                                        // Combustible
                                        if (selectedFuel.equals("Agresiva")) lapTime -= 0.3;
                                        else if (selectedFuel.equals("Ahorro")) lapTime += 0.5;
                                    } else {
                                        // Reglajes aleatorios simulados para rivales
                                        double randReglaje = Math.random();
                                        if (randReglaje < 0.3) lapTime -= 0.3;
                                        else if (randReglaje < 0.6) lapTime += 0.2;
                                    }

                                    // Multiplicador de clima
                                    if (clima.equals("Lluvioso")) {
                                        lapTime += 7.0 + Math.random() * 2.0;
                                    } else if (clima.equals("Extremo")) {
                                        lapTime += 14.0 + Math.random() * 3.0;
                                    }

                                    // Varianza
                                    lapTime += (Math.random() * 0.8) - 0.3;

                                    // Probabilidad de retiro (DNF)
                                    double dnfChance = 0.005; // 0.5% base
                                    if (d.nombre.equals(selectedPilotName) && selectedDrive.equals("Agresiva")) {
                                        dnfChance = 0.02; // 2% si conduce agresivo
                                    } else if (Math.random() < 0.1) {
                                        // Algunos rivales conducen agresivo aleatoriamente
                                        dnfChance = 0.015;
                                    }

                                    if (Math.random() < dnfChance) {
                                        d.retirado = true;
                                        String[] causas = {"Accidente", "Fallo mecánico", "Motor roto", "Pinchazo", "Trompo"};
                                        d.causaRetiro = causas[(int) (Math.random() * causas.length)];
                                        
                                        // Actualizar GUI de forma segura en el Event Dispatch Thread (EDT)
                                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                eventLogLabel.setText("¡Retirado! " + d.nombre + " fuera de carrera por: " + d.causaRetiro);
                                            }
                                        });
                                    } else {
                                        d.tiempoAcumulado += lapTime;
                                    }
                                }
                            });
                            threads.add(t);
                            t.start();
                        }

                        // Sincronización: Esperar a que terminen los cálculos de todos los pilotos para esta vuelta
                        for (Thread t : threads) {
                            try {
                                t.join();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                        // Ordenar la parrilla en base al tiempo acumulado
                        Collections.sort(grid, new Comparator<DriverState>() {
                            @Override
                            public int compare(DriverState d1, DriverState d2) {
                                if (d1.retirado && !d2.retirado) return 1;
                                if (!d1.retirado && d2.retirado) return -1;
                                if (d1.retirado && d2.retirado) return 0;
                                return Double.compare(d1.tiempoAcumulado, d2.tiempoAcumulado);
                            }
                        });

                        // Actualizar tabla visual
                        tableModel.setRowCount(0);
                        int pos = 1;
                        double leaderTime = grid.get(0).tiempoAcumulado;

                        for (DriverState d : grid) {
                            String timeStr;
                            if (d.retirado) {
                                timeStr = "RETIRADO (" + d.causaRetiro + ")";
                            } else {
                                if (pos == 1) {
                                    int min = (int) (d.tiempoAcumulado / 60);
                                    double seg = d.tiempoAcumulado - (min * 60);
                                    timeStr = String.format("%d:%06.3f", min, seg).replace(',', '.');
                                } else {
                                    double gap = d.tiempoAcumulado - leaderTime;
                                    timeStr = String.format("+%.3f s", gap).replace(',', '.');
                                }
                            }
                            tableModel.addRow(new Object[]{
                                d.retirado ? "RET" : String.valueOf(pos++),
                                d.nombre,
                                d.equipo,
                                timeStr,
                                clima
                            });
                        }

                        // Comentario de carrera aleatorio si no hubo retiros
                        if (!eventLogLabel.getText().startsWith("¡Retirado!")) {
                            double logRand = Math.random();
                            if (logRand < 0.25) {
                                eventLogLabel.setText("Vuelta " + currentLap + ": " + grid.get(0).nombre + " lidera con comodidad.");
                            } else if (logRand < 0.50) {
                                eventLogLabel.setText("Vuelta " + currentLap + ": " + grid.get(1).nombre + " persigue de cerca al líder.");
                            } else if (logRand < 0.75) {
                                eventLogLabel.setText("Vuelta " + currentLap + ": Peleas intensas en la mitad de la parrilla.");
                            }
                        }
                    }
                });

                raceTimer.start();
            }
        });

        // Cargar datos iniciales de piloto
        if (pilotoCombo.getItemCount() > 0) {
            pilotoCombo.setSelectedIndex(0);
            String selName = (String) pilotoCombo.getSelectedItem();
            for (Piloto p : DataStore.getPilotos()) {
                if (p.getNombre().equals(selName)) {
                    pilotNameLabel.setText(p.getNombre());
                    pilotTeamLabel.setText(p.getEquipo() + " - " + p.getRol());
                    updatePilotImage(photoLabel, String.valueOf(p.getId()), p.getNombre(), p.getEquipo(), p.getImagenUrl());
                    break;
                }
            }
        }

        // Cargar datos iniciales del circuito
        if (circuitoCombo.getItemCount() > 0) {
            circuitoCombo.setSelectedIndex(0);
            String selName = (String) circuitoCombo.getSelectedItem();
            for (Circuito c : DataStore.getCircuitos()) {
                if (c.getNombre().equals(selName)) {
                    trackNameLabel.setText(c.getNombre());
                    String localKey;
                    if (selName.contains("Mónaco")) localKey = "monaco";
                    else if (selName.contains("Silverstone")) localKey = "silverstone";
                    else if (selName.contains("Spa")) localKey = "spa";
                    else if (selName.contains("Monza")) localKey = "monza";
                    else localKey = "suzuka";
                    updateTrackImage(trackImageLabel, localKey, c.getNombre(), c.getImagenUrl());
                    break;
                }
            }
        }

        contentPanel.add(dashboard, "dashboard");
        contentPanel.add(equipos, "equipos");
        contentPanel.add(circuitos, "circuitos");
        contentPanel.add(simulador, "simulador");
    }

    // Para obtener el color correspondiente a la escudería
    private Color getTeamColor(String name) {
        if (name.contains("Red Bull")) return new Color(30, 93, 165);
        if (name.contains("Mercedes")) return new Color(0, 168, 150);
        if (name.contains("Ferrari")) return new Color(225, 6, 0);
        if (name.contains("McLaren")) return new Color(255, 135, 0);
        if (name.contains("Aston Martin")) return new Color(0, 90, 48);
        if (name.contains("Alpine")) return new Color(0, 144, 255);
        if (name.contains("Alfa Romeo")) return new Color(144, 0, 0);
        if (name.contains("Haas")) return new Color(180, 180, 180);
        if (name.contains("AlphaTauri")) return new Color(12, 35, 64);
        if (name.contains("Williams")) return new Color(0, 163, 224);
        return textSecondary;
    }

    // Para crear etiquetas estilizadas en los formularios
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(textSecondary);
        label.setFont(new Font("Outfit", Font.BOLD, 13));
        return label;
    }

    // Para cargar la imagen del piloto de forma asíncrona (local o remota)
    private void updatePilotImage(JLabel imageLabel, String pilotId, String pilotName, String teamName, String fallbackUrl) {
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                // Intentar buscar la foto local en múltiples rutas posibles
                java.io.File[] candidates = new java.io.File[]{
                    new java.io.File("images/" + pilotId + ".jpg"),
                    new java.io.File("bin/images/" + pilotId + ".jpg"),
                    new java.io.File("src/images/" + pilotId + ".jpg"),
                    new java.io.File("../images/" + pilotId + ".jpg")
                };
                
                java.io.File localFile = null;
                for (java.io.File f : candidates) {
                    if (f.exists()) {
                        localFile = f;
                        break;
                    }
                }

                if (localFile != null) {
                    try {
                        java.awt.Image image = javax.imageio.ImageIO.read(localFile);
                        if (image != null) {
                            java.awt.Image scaledImage = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                            return new ImageIcon(scaledImage);
                        }
                    } catch (Exception e) {
                        System.out.println("Error al leer archivo local: " + localFile.getAbsolutePath());
                    }
                }

                // Fallback remoto
                if (fallbackUrl != null && fallbackUrl.startsWith("http")) {
                    try {
                        java.net.URL url = new java.net.URL(fallbackUrl);
                        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                        conn.connect();
                        try (java.io.InputStream in = conn.getInputStream()) {
                            java.awt.Image image = javax.imageio.ImageIO.read(in);
                            if (image != null) {
                                java.awt.Image scaledImage = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                                return new ImageIcon(scaledImage);
                            }
                        }
                    } catch (Exception e) {
                        // Ignorar y pasar a generar placeholder
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    } else {
                        // Si falla todo, usar el placeholder vectorizado dinámico
                        imageLabel.setIcon(createPilotPlaceholder(pilotName, teamName));
                        imageLabel.setText("");
                    }
                } catch (Exception e) {
                    imageLabel.setIcon(createPilotPlaceholder(pilotName, teamName));
                    imageLabel.setText("");
                }
            }
        }.execute();
    }

    // Para cargar la imagen de la pista de forma asíncrona (local o remota)
    private void updateTrackImage(JLabel imageLabel, String trackKey, String trackName, String fallbackUrl) {
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                // Intentar buscar el mapa local en múltiples rutas posibles
                java.io.File[] candidates = new java.io.File[]{
                    new java.io.File("images/" + trackKey + ".png"),
                    new java.io.File("bin/images/" + trackKey + ".png"),
                    new java.io.File("src/images/" + trackKey + ".png"),
                    new java.io.File("../images/" + trackKey + ".png")
                };
                
                java.io.File localFile = null;
                for (java.io.File f : candidates) {
                    if (f.exists()) {
                        localFile = f;
                        break;
                    }
                }

                if (localFile != null) {
                    try {
                        java.awt.Image image = javax.imageio.ImageIO.read(localFile);
                        if (image != null) {
                            java.awt.Image scaledImage = image.getScaledInstance(180, 110, java.awt.Image.SCALE_SMOOTH);
                            return new ImageIcon(scaledImage);
                        }
                    } catch (Exception e) {
                        System.out.println("Error al leer mapa local: " + localFile.getAbsolutePath());
                    }
                }

                // Fallback remoto
                if (fallbackUrl != null && fallbackUrl.startsWith("http")) {
                    try {
                        java.net.URL url = new java.net.URL(fallbackUrl);
                        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                        conn.connect();
                        try (java.io.InputStream in = conn.getInputStream()) {
                            java.awt.Image image = javax.imageio.ImageIO.read(in);
                            if (image != null) {
                                java.awt.Image scaledImage = image.getScaledInstance(180, 110, java.awt.Image.SCALE_SMOOTH);
                                return new ImageIcon(scaledImage);
                            }
                        }
                    } catch (Exception e) {
                        // Ignorar y pasar a generar placeholder
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    } else {
                        // Si falla todo, usar el trazado esquemático dinámico
                        imageLabel.setIcon(createTrackPlaceholder(trackName));
                        imageLabel.setText("");
                    }
                } catch (Exception e) {
                    imageLabel.setIcon(createTrackPlaceholder(trackName));
                    imageLabel.setText("");
                }
            }
        }.execute();
    }

    // Para generar un avatar vectorizado con las iniciales del piloto y color de escudería
    private ImageIcon createPilotPlaceholder(String pilotName, String teamName) {
        int size = 100;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Color de fondo temático de la escudería
        Color teamColor = getTeamColor(teamName);
        g2.setColor(teamColor);
        g2.fillOval(5, 5, size - 10, size - 10);
        
        // Borde estilizado
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(5, 5, size - 10, size - 10);
        
        // Extraer iniciales
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Outfit", Font.BOLD, 36));
        String initials = "";
        String[] parts = pilotName.split(" ");
        if (parts.length > 0 && !parts[0].isEmpty()) initials += parts[0].substring(0, 1);
        if (parts.length > 1 && !parts[1].isEmpty()) initials += parts[1].substring(0, 1);
        
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(initials)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(initials, x, y);
        g2.dispose();
        return new ImageIcon(img);
    }

    // Para generar un trazado esquemático vectorial de la pista en color rojo F1
    private ImageIcon createTrackPlaceholder(String trackName) {
        int width = 180;
        int height = 110;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fondo del cuadro de trazado
        g2.setColor(new Color(15, 17, 21));
        g2.fillRect(0, 0, width, height);
        
        // Dibujar curvas y rectas simplificadas según el circuito
        g2.setColor(redF1);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int[] xPoints;
        int[] yPoints;
        
        if (trackName.contains("Mónaco")) {
            xPoints = new int[]{30, 70, 110, 150, 140, 100, 80, 50, 30};
            yPoints = new int[]{80, 75, 80, 60, 35, 30, 50, 45, 80};
        } else if (trackName.contains("Silverstone")) {
            xPoints = new int[]{30, 90, 150, 140, 110, 80, 40, 30};
            yPoints = new int[]{70, 80, 55, 30, 40, 25, 35, 70};
        } else if (trackName.contains("Monza")) {
            xPoints = new int[]{30, 150, 140, 100, 80, 30};
            yPoints = new int[]{70, 70, 40, 40, 30, 30};
        } else if (trackName.contains("Spa")) {
            xPoints = new int[]{30, 70, 120, 150, 130, 90, 50, 30};
            yPoints = new int[]{80, 85, 65, 30, 25, 40, 35, 80};
        } else { // Suzuka o default (forma de 8)
            xPoints = new int[]{30, 70, 110, 150, 140, 100, 60, 30};
            yPoints = new int[]{60, 35, 75, 45, 75, 35, 75, 60};
        }
        
        g2.drawPolyline(xPoints, yPoints, xPoints.length);
        
        // Punto de largada / meta
        g2.setColor(Color.WHITE);
        g2.fillOval(xPoints[0] - 3, yPoints[0] - 3, 6, 6);
        
        g2.dispose();
        return new ImageIcon(img);
    }

    // Para crear el panel central del dashboard con estadísticas e historial
    private JPanel createDashboardCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(bgMain);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Panel Izquierdo: Estadísticas del Campeonato
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(bgCard);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 39, 42)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        JLabel statsHeader = new JLabel("Estadísticas del Campeonato");
        statsHeader.setForeground(redF1);
        statsHeader.setFont(new Font("Outfit", Font.BOLD, 16));
        statsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(statsHeader);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Stats grid
        JPanel statsGrid = new JPanel(new GridLayout(3, 1, 10, 10));
        statsGrid.setBackground(bgCard);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsGrid.add(createStatRow("Escuderías Registradas:", String.valueOf(DataStore.getEquipos().size())));
        statsGrid.add(createStatRow("Pilotos en la Parrilla:", String.valueOf(DataStore.getPilotos().size())));
        statsGrid.add(createStatRow("Circuitos Disponibles:", String.valueOf(DataStore.getCircuitos().size())));

        statsPanel.add(statsGrid);
        centerPanel.add(statsPanel);

        // Panel Derecho: Historial de Carreras Recientes
        dashboardHistoryContainer = new JPanel(new BorderLayout());
        dashboardHistoryContainer.setBackground(bgCard);
        dashboardHistoryContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 39, 42)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel historyHeader = new JLabel("Historial de Carreras Recientes");
        historyHeader.setForeground(textPrimary);
        historyHeader.setFont(new Font("Outfit", Font.BOLD, 16));
        historyHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        dashboardHistoryContainer.add(historyHeader, BorderLayout.NORTH);

        // Cargar historial inicial
        refreshDashboardHistory();

        centerPanel.add(dashboardHistoryContainer);
        return centerPanel;
    }

    // Para refrescar dinámicamente el historial del dashboard
    private void refreshDashboardHistory() {
        if (dashboardHistoryContainer == null) return;
        
        // Quitar componente central actual si lo hay
        BorderLayout layout = (BorderLayout) dashboardHistoryContainer.getLayout();
        Component centerComponent = layout.getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent != null) {
            dashboardHistoryContainer.remove(centerComponent);
        }

        List<String> recentRaces = readRecentRaces();
        if (recentRaces.isEmpty()) {
            JLabel noHistoryLabel = new JLabel("No se han registrado carreras simuladas todavía", SwingConstants.CENTER);
            noHistoryLabel.setForeground(textSecondary);
            noHistoryLabel.setFont(new Font("Outfit", Font.ITALIC, 13));
            dashboardHistoryContainer.add(noHistoryLabel, BorderLayout.CENTER);
        } else {
            JPanel listPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            listPanel.setBackground(bgCard);
            // Mostrar del más nuevo al más viejo (máximo 3)
            int count = 0;
            for (int i = recentRaces.size() - 1; i >= 0 && count < 3; i--) {
                JPanel itemCard = new JPanel(new BorderLayout());
                itemCard.setBackground(bgMain);
                itemCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(39, 39, 42)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                
                JLabel textLabel = new JLabel(recentRaces.get(i));
                textLabel.setForeground(textPrimary);
                textLabel.setFont(new Font("Outfit", Font.PLAIN, 13));
                itemCard.add(textLabel, BorderLayout.CENTER);
                
                listPanel.add(itemCard);
                count++;
            }
            dashboardHistoryContainer.add(listPanel, BorderLayout.CENTER);
        }
        
        dashboardHistoryContainer.revalidate();
        dashboardHistoryContainer.repaint();
    }

    // Para leer historial del archivo
    private List<String> readRecentRaces() {
        List<String> races = new ArrayList<>();
        java.io.File file = new java.io.File("historial.txt");
        if (!file.exists()) {
            return races;
        }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            String currentRace = null;
            String winner = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Carrera en ")) {
                    currentRace = line.substring("Carrera en ".length());
                } else if (line.contains("1. ")) {
                    winner = line.substring(line.indexOf("1. ") + 3);
                } else if (line.startsWith("---")) {
                    if (currentRace != null && winner != null) {
                        races.add(currentRace + " - Ganador: " + winner);
                    }
                    currentRace = null;
                    winner = null;
                }
            }
        } catch (Exception e) {
            // Ignorar errores de lectura
        }
        return races;
    }

    // Fila de estadísticas del dashboard
    private JPanel createStatRow(String labelText, String valueText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(bgCard);
        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel label = new JLabel(labelText);
        label.setForeground(textSecondary);
        label.setFont(new Font("Outfit", Font.PLAIN, 14));
        
        JLabel val = new JLabel(valueText);
        val.setForeground(textPrimary);
        val.setFont(new Font("Outfit", Font.BOLD, 18));
        
        row.add(label, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    // Para crear paneles temporales
    private JPanel createEmptyPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgMain);
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(textPrimary);
        label.setFont(new Font("Outfit", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
