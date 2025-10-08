package eightoff_gui;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class EightOffVisualController {
    // Reemplazar estas variables de instancia
    private Manager manager;
    private Estado respaldo = null;
    private StackPane cartaSeleccionada = null;
    private ArrayList<StackPane> cartasSeleccionadas = new ArrayList<>(); // Para múltiples cartas
    private javafx.scene.Parent contenedorOrigen = null; // Cambiar de VBox a Parent
    private double offsetX, offsetY;
    private Mazo mazo = new Mazo();
    private ArrayList<StackPane> cartasGraficas = new ArrayList<>();

    // Undo
    @FXML Circle undoButton;
    @FXML Text undoText;

    // Foundations
    @FXML VBox f1;
    @FXML VBox f2;
    @FXML VBox f3;
    @FXML VBox f4;

    // Tableaus
    @FXML VBox t1;
    @FXML VBox t2;
    @FXML VBox t3;
    @FXML VBox t4;
    @FXML VBox t5;
    @FXML VBox t6;
    @FXML VBox t7;
    @FXML VBox t8;

    @FXML Button newGame;
    @FXML Button menu;

    // Descarte
    @FXML StackPane zonaDescarte1;
    @FXML StackPane zonaDescarte2;
    @FXML StackPane zonaDescarte3;
    @FXML StackPane zonaDescarte4;
    @FXML StackPane zonaDescarte5;
    @FXML StackPane zonaDescarte6;
    @FXML StackPane zonaDescarte7;
    @FXML StackPane zonaDescarte8;

    private ArrayList<VBox> tableaus = new ArrayList<>();
    private ArrayList<VBox> foundations = new ArrayList<>();

    // Definir tamaño fijo para las cartas
    private final double CARD_WIDTH = 64.0;  // Mismo que en FXML
    private final double CARD_HEIGHT = 94.0; // Mismo que en FXML
    private final double CARD_OFFSET = 1.0; // Desplazamiento para el efecto escalera

    @FXML
    private void initialize() throws IOException {

        //Agregar Hover Botones.
        //hoverBoton(menu);
        //hoverBoton(newGame);

        //El texto no toma los eventos
        undoText.setMouseTransparent(true);

        //Accion undo
        //undoClick();

        //Agrega hover
        //hoverUndo();

        menu.setOnAction(e -> {
            //regrearAlMenu();
        });

        newGame.setOnAction(e -> {
            //reiniciarJuego();
        });

        // Configurar VBoxes primero
        configurarVBoxes();

        // Generar los StackPane con su respectivo diseño
        generarCartas();

        // Crear arreglos para las referencias de mis VBox
        crearArreglos();

        // Agregar las cartas en forma descendente a mis Tableaus
        //agregarCartasAlTableau();

        // Agregar las cartas restantes al pozo
        //agregarCartasAlPozo();

        // Configurar eventos drag and drop
        //configurarCartasAlTomarYSoltar();
    }

    //Configuro que tanto se espacian las cartas en vertical.
    public void configurarVBoxes() {
        for (VBox vbox : new VBox[]{t1, t2, t3, t4, t5, t6, t7}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-70); // Espaciado negativo para superponer cartas
        }

        for (VBox vbox : new VBox[]{f1, f2, f3, f4}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-95);
        }


    }

    //Genero los arreglos para los tableaus y los foundations
    public void crearArreglos() {
        tableaus.add(t1);
        tableaus.add(t2);
        tableaus.add(t3);
        tableaus.add(t4);
        tableaus.add(t5);
        tableaus.add(t6);
        tableaus.add(t7);

        foundations.add(f1);
        foundations.add(f2);
        foundations.add(f3);
        foundations.add(f4);
    }

    //Genero las cartas a partir del mazo de original y les imparto un diseño ya creado.
    public void generarCartas() throws IOException {

        //A partir de la clase mazo genero todas las cartas de la baraja
        for (int i = 0; i < mazo.getCartas().size(); i++) {
            CartaInglesa carta = mazo.getCartas().eliminaInicio();
            carta.makeFaceUp();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/practica4" +
                    "_eightoff/Card.fxml"));
            StackPane cartaStackPane = loader.load();


            //se les da un tamaño fijo a las cartas
            cartaStackPane.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
            cartaStackPane.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
            cartaStackPane.setMinSize(CARD_WIDTH, CARD_HEIGHT);


            StackPane visibleCard = (StackPane) cartaStackPane.lookup("#VisibleCard");
            Label labelTop = (Label) cartaStackPane.lookup("#LabelTop");
            Label labelCenter = (Label) cartaStackPane.lookup("#LabelCenter");
            Label labelBottom = (Label) cartaStackPane.lookup("#LabelBottom");

            // Configuro la carta según sus datos
                labelTop.setText(carta.toString());
                labelTop.setStyle("-fx-text-fill: " + carta.getColor() + "; -fx-font-weight: bold;");


                labelCenter.setText(carta.toString());
                labelCenter.setStyle("-fx-text-fill: " + carta.getColor() + "; -fx-font-weight: bold;");


                labelBottom.setText(carta.getPalo().getFigura());
                labelBottom.setStyle("-fx-text-fill: " + carta.getColor() + "; -fx-font-size: 40; -fx-font-weight: bold;");


                visibleCard.setVisible(false);

            cartaStackPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a6fc4, #0d4d8c); " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-color: black; " +
                    "-fx-border-radius: 10; " +
                    "-fx-border-width: 1;");

            cartasGraficas.add(cartaStackPane);
        }
    }

}