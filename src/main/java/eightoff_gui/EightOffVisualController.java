package eightoff_gui;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import eightoff_logica.FoundationDeck;
import eightoff_logica.TableauDeck;
import eightoff_logica.TableroLogico;
import eightoff_logica.WasteZone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lista_circular_simple.ListaCircularSimple;

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

    private TableroLogico tableroLogico = new TableroLogico();
    private ArrayList<StackPane> wasteZone = new ArrayList<>();
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

        //Actualiza las cartas a partir del tablero logico
        actualizarCartas();

        // Configurar eventos drag and drop
        //configurarCartasAlTomarYSoltar();
    }

    //Configuro que tanto se espacian las cartas en vertical.
    public void configurarVBoxes() {
        for (VBox vbox : new VBox[]{t1, t2, t3, t4, t5, t6, t7, t8}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-70); // Espaciado negativo para superponer cartas
        }

        for (VBox vbox : new VBox[]{f1, f2, f3, f4}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-95);
        }

        for(StackPane  zonaDescarte: new StackPane[]{zonaDescarte1, zonaDescarte2,
                            zonaDescarte3, zonaDescarte4, zonaDescarte5, zonaDescarte6,
                            zonaDescarte7, zonaDescarte8}) {

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
        tableaus.add(t8);

        foundations.add(f1);
        foundations.add(f2);
        foundations.add(f3);
        foundations.add(f4);

        wasteZone.add(zonaDescarte1);
        wasteZone.add(zonaDescarte2);
        wasteZone.add(zonaDescarte3);
        wasteZone.add(zonaDescarte4);
        wasteZone.add(zonaDescarte5);
        wasteZone.add(zonaDescarte6);
        wasteZone.add(zonaDescarte7);
        wasteZone.add(zonaDescarte8);
    }

    //Genero las cartas a partir del mazo de original y les imparto un diseño ya creado.
    public void generarCartas() throws IOException {

        //A partir de la clase mazo genero todas las cartas de la baraja
        for (CartaInglesa carta:tableroLogico.getCartasGraficas()) {

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


                visibleCard.setVisible(true);

            // También restaurar estilo normal
            cartaStackPane.setStyle("-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #c39bd3;" +   // Borde morado pastel normal
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1;" +
                    "-fx-effect: null;");

            cartasGraficas.add(cartaStackPane);
        }
    }

    public void actualizarCartas() throws IOException {

        int indexTableau = 0;
        for(VBox t:tableaus){
            TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();
            ListaCircularSimple<CartaInglesa> tLogico = tableauDecks[indexTableau].getTableau();
            for(int i=0; i<tLogico.size(); i++) {
                for (StackPane carta : cartasGraficas) {
                    Label labelTop = (Label) carta.lookup("#LabelTop");
                    if (labelTop.getText().equals(tLogico.get(i).toString())) {
                        if(!tLogico.get(i).isFaceup()){
                            carta.setOpacity(0.5);
                        } else{
                            agregarHover(carta);
                        }
                        t.getChildren().add(carta);
                    }
                }
            }
            indexTableau++;
        }

        int indexFoundation = 0;
        for(VBox f:foundations){
            FoundationDeck[] foundationDecks = tableroLogico.getFoundationDecks();
            ListaCircularSimple<CartaInglesa> fLogico = foundationDecks[indexFoundation].getFoundation();
            for(int i=0; i<fLogico.size(); i++) {
                for (StackPane carta : cartasGraficas) {
                    Label labelTop = (Label) carta.lookup("#LabelTop");
                    if (labelTop.getText().equals(fLogico.get(i).toString())) {
                        f.getChildren().add(carta);
                    }
                }
            }
            indexFoundation++;
        }

        int indexWasteZone = 0;
        for(StackPane wz:wasteZone){
            WasteZone[] wasteZones = tableroLogico.getWasteZones();
            CartaInglesa cartaLogica = wasteZones[indexWasteZone].getCarta();
            if(cartaLogica!=null) {
                for (StackPane carta : cartasGraficas) {
                    Label labelTop = (Label) carta.lookup("#LabelTop");
                    if (labelTop.getText().equals(cartaLogica.toString())) {
                        if(cartaLogica.isFaceup()){
                            agregarHover(carta);
                        }
                        wz.getChildren().add(carta);
                    }
                }
            }
            indexWasteZone++;
        }
    }

    public boolean isDraggable(StackPane cartaSeleccionada){
        if(cartaSeleccionada.getOpacity() == 1.0){
            for(VBox f:foundations){
                if(f.getChildren().contains(cartaSeleccionada)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }




    //Agrega hover
    private void agregarHover(StackPane carta) {
        carta.setOnMouseEntered(event -> {
            carta.setStyle("-fx-background-color: #f0f0f0;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #d8b7dd;" +   // Morado pastel
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2;" +
                    "-fx-effect: dropshadow(gaussian, #d8b7dd, 10, 0.5, 0, 0);"); // Sombra morada pastel
        });

        carta.setOnMouseExited(event -> {
            carta.setStyle("-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #c39bd3;" +   // Borde morado pastel normal
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1;" +
                    "-fx-effect: null;");
        });
    }

    //Elimina el hover
    private void quitarHover(StackPane carta) {
        carta.setOnMouseEntered(null);
        carta.setOnMouseExited(null);

        // También restaurar estilo normal
        carta.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #c39bd3;" +   // Borde morado pastel normal
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-effect: null;");
    }



    public void configurarCartasAlTomarYSoltar(){

    }





}