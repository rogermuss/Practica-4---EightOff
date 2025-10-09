package eightoff_gui;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import eightoff_logica.FoundationDeck;
import eightoff_logica.TableauDeck;
import eightoff_logica.TableroLogico;
import eightoff_logica.WasteZone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lista_circular_simple.ListaCircularSimple;

import java.io.IOException;
import java.util.ArrayList;

public class EightOffVisualController {
    // Reemplazar estas variables de instancia
    private Manager manager;
    private Estado respaldo = null;
    private StackPane cartaSeleccionada = null;
    private ListaCircularSimple<CartaInglesa> cartasLogicas = new ListaCircularSimple<>();
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
        hoverBoton(menu);
        hoverBoton(newGame);

        //El texto no toma los eventos
        undoText.setMouseTransparent(true);

        //Accion undo
        undoClick();

        //Agrega hover
        hoverUndo();

        menu.setOnAction(e -> {
            regrearAlMenu();
        });

        newGame.setOnAction(e -> {
            reiniciarJuego();
        });

        // Configurar VBoxes primero
        configurarVBoxes();

        // Generar los StackPane con su respectivo diseño
        generarCartas();

        // Crear arreglos para las referencias de mis VBox
        crearArreglos();

        //Actualiza las cartas a partir del tablero logico
        actualizarCartas();

        //Agrego Accion de Tomar
        setDraggable();

        //Agrego Accion de Soltaar
        setDragReleased();

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
                            quitarHover(carta);
                        } else{
                            carta.setOpacity(1);
                            agregarHover(carta);
                        }
                        t.getChildren().add(carta);
                        break;
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
                        carta.setOpacity(1);
                        quitarHover(carta);
                        f.getChildren().add(carta);
                        break;
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
                        carta.setOpacity(1);
                        agregarHover(carta);
                        wz.getChildren().add(carta);
                        break;
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

    public void regrearAlMenu(){
        manager = new Manager((Stage) t1.getScene().getWindow());
        try {
            manager.iniciarEscenaMenu();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void reiniciarJuego(){
        manager = new Manager((Stage) t1.getScene().getWindow());
        try {
            manager.iniciarEscenaJuego();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    //Agrega hover
    private void agregarHover(StackPane carta) {
        carta.setOnMouseEntered(event -> {
            carta.setStyle("-fx-background-color: #f0f0f0;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #d8b7dd;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2;" +
                    "-fx-effect: dropshadow(gaussian, #d8b7dd, 10, 0.5, 0, 0);");
        });

        carta.setOnMouseExited(event -> {
            carta.setStyle("-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #c39bd3;" +
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
                "-fx-border-color: #c39bd3;" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-effect: null;");
    }

    //Da hover a los botones de accion
    public void hoverBoton(Button boton) {
        String estiloBoton = boton.getStyle();
        boton.setOnMouseEntered(event -> {
            boton.setStyle("-fx-background-color: #f4a7a3;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #e68b88;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2;" +
                    "-fx-effect: dropshadow(gaussian, #f2b1ad, 10, 0.5, 0, 0);");
        });
        boton.setOnMouseExited(event -> {
            boton.setStyle(estiloBoton);
        });
    }


    //Hover undo
    public void hoverUndo() {
        //Hover UNDO
        undoButton.setOnMouseEntered(e -> {
            undoButton.setStyle("-fx-background-color: #fff9b0;" + // Amarillo pastel claro
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #e6d86b;" + // Borde amarillo pastel más oscuro
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2;" +
                    "-fx-effect: dropshadow(gaussian, #fff6a1, 10, 0.5, 0, 0);"); // Sombra amarilla suave
        });

        undoButton.setOnMouseExited(e -> {
            undoButton.setStyle("-fx-background-color: #28220f;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #e6d86b;" + // Mantiene borde amarillo pastel sutil
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2;");
        });
    }

    //Evento undo al hacer click
    public void undoClick(){
        //Regresa un movimiento
        undoButton.setOnMouseClicked(e -> {

        });
    }

    //Guardado del estado
    public void undoSave(){
        Estado estado;
    }



    private void limpiarGUI() {
        for(VBox t : tableaus) {
            t.getChildren().clear();
        }
        for(VBox f : foundations) {
            f.getChildren().clear();
        }
        for(StackPane w : wasteZone) {
            w.getChildren().clear();
        }
    }

    private void limpiarSeleccion() {
        cartasLogicas = null;
        cartaSeleccionada = null;
        cartasSeleccionadas.clear();
        contenedorOrigen = null;
    }






    public CartaInglesa encontrarCartaSeleccionada(StackPane carta){
        FoundationDeck[] foundationDecks = tableroLogico.getFoundationDecks();
        TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();
        WasteZone[] wasteZones = tableroLogico.getWasteZones();
        Label labelTop = (Label) carta.lookup("#LabelTop");

        for(FoundationDeck foundationDeck:foundationDecks){
            ListaCircularSimple<CartaInglesa> cartas = foundationDeck.getFoundation();
            for(int i=0;i<cartas.size();i++){
                if(cartas.get(i).toString().equals(labelTop.getText())){
                    return cartas.get(i);
                }
            }
        }

        for(TableauDeck tableauDeck:tableauDecks){
            ListaCircularSimple<CartaInglesa> cartas = tableauDeck.getTableau();
            for(int i=0;i<cartas.size();i++){
                if(cartas.get(i).toString().equals(labelTop.getText())){
                    return cartas.get(i);
                }
            }
        }

        for(WasteZone wasteZone:wasteZones){
            if(wasteZone.getCarta() != null &&
                    wasteZone.getCarta().toString().equals(labelTop.getText())){
                return wasteZone.getCarta();
            }
        }
        return null;
    }


    public VBox reachTableauOrFoundation(MouseEvent e){
        for(VBox f:foundations){
            if(f.getLayoutX() <= e.getSceneX() && f.getLayoutX()+f.getWidth() >= e.getSceneX()
                    && f.getLayoutY() <= e.getSceneY() && f.getLayoutY()+f.getHeight() >= e.getSceneY()){
                return f;
            }
        }

        for(VBox t:tableaus){
            if(t.getLayoutX() <= e.getSceneX() && t.getLayoutX()+t.getWidth() >= e.getSceneX()
                    && t.getLayoutY() <= e.getSceneY() && t.getLayoutY()+t.getHeight() >= e.getSceneY()){
                return t;
            }
        }

        return null;
    }

    public StackPane reachWasteZone(MouseEvent e){
        for (StackPane w:wasteZone){
            if(w.getLayoutX() <= e.getSceneX() && w.getLayoutX()+w.getWidth() >= e.getSceneX()
                    && w.getLayoutY() <= e.getSceneY() && w.getLayoutY()+w.getHeight() >= e.getSceneY()){
                return w;
            }
        }

        return null;
    }

    public void setDraggable(){
        for(StackPane carta:cartasGraficas){
            carta.setOnMousePressed(event -> {
                if(isDraggable(carta)){
                    cartaSeleccionada = carta;

                    identificarOrigenYCartas(carta);

                    for(StackPane c:cartasSeleccionadas){
                        c.setStyle(c.getStyle() +
                                "-fx-effect: dropshadow(gaussian, purple, 15, 0.8, 0, 0);");
                    }

                    contenedorOrigen = carta.getParent();
                    event.consume();
                }
            });
        }
    }

    public void setDragReleased(){
        for(StackPane carta:cartasGraficas){
            carta.setOnMouseReleased(event -> {
                if(carta == cartaSeleccionada) {
                    boolean movimientoExitoso = false;

                    VBox contenedorVBox = reachTableauOrFoundation(event);
                    StackPane contenedorStackPane = reachWasteZone(event);

                    if(contenedorVBox != null) {
                        // Mover a Foundation
                        if(foundations.contains(contenedorVBox)) {
                            int indice = foundations.indexOf(contenedorVBox);
                            FoundationDeck[] foundationDecks = tableroLogico.getFoundationDecks();
                            FoundationDeck fLogico = foundationDecks[indice];

                            // Solo mover la primera carta a Foundation
                            if(cartasLogicas.size() == 1 &&
                                    fLogico.ingresarCarta(cartasLogicas.get(0))){
                                removerLogicaOrigen();
                                movimientoExitoso = true;
                            }
                        }
                        // Mover a Tableau
                        else if(tableaus.contains(contenedorVBox)) {
                            int indice = tableaus.indexOf(contenedorVBox);
                            TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();
                            TableauDeck tLogico = tableauDecks[indice];

                            if(tLogico.insertarCartas(cartasLogicas)) {
                                removerLogicaOrigen();
                                movimientoExitoso = true;
                            }
                        }
                    }
                    // Mover a WasteZone
                    else if(contenedorStackPane != null) {
                        int indice = wasteZone.indexOf(contenedorStackPane);
                        WasteZone[] wasteZones = tableroLogico.getWasteZones();
                        WasteZone wLogico = wasteZones[indice];

                        // Solo cartas únicas a WasteZone
                        if(cartasLogicas.size() == 1 && wLogico.getCarta() == null){
                            wLogico.setCarta(cartasLogicas.get(0));
                            removerLogicaOrigen();
                            movimientoExitoso = true;
                        }
                    }


                    // Actualizar toda la GUI
                    try {
                        limpiarGUI();
                        actualizarCartas();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Limpiar selección
                    limpiarSeleccion();
                    event.consume();
                }
            });
        }
    }

    public ListaCircularSimple<CartaInglesa> obtenerCartasEnSecuencia(StackPane carta){
        ListaCircularSimple<CartaInglesa> logicCardsList = new ListaCircularSimple<>();
        TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();
        Label labelTop = (Label) carta.lookup("#LabelTop");

        for(int j=0; j<tableauDecks.length; j++) {
            ListaCircularSimple<CartaInglesa> tLogico = tableauDecks[j].getTableau();
            for (int i = 0; i < tLogico.size(); i++) {
                if (tLogico.get(i).toString().equals(labelTop.getText())) {
                    logicCardsList = tableauDecks[j].removerCartas(tLogico.get(i));
                    break;
                }
            }
            if(!logicCardsList.isEmpty()) break;
        }

        for(int i = 0; i < logicCardsList.size(); i++){
            for(StackPane c : cartasGraficas){
                Label label = (Label) c.lookup("#LabelTop");
                if(logicCardsList.get(i).toString().equals(label.getText())){
                    cartasSeleccionadas.add(c);
                    break;
                }
            }
        }

        return logicCardsList;
    }

    private void identificarOrigenYCartas(StackPane carta) {
        cartasSeleccionadas.clear();
        cartasLogicas = new ListaCircularSimple<>();

        Label labelTop = (Label) carta.lookup("#LabelTop");
        TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();

        for(int j=0; j<tableauDecks.length; j++) {
            ListaCircularSimple<CartaInglesa> tLogico = tableauDecks[j].getTableau();
            for (int i = 0; i < tLogico.size(); i++) {
                if (tLogico.get(i).toString().equals(labelTop.getText())) {
                    for(int k = i; k < tLogico.size(); k++) {
                        cartasLogicas.insertaFin(tLogico.get(k));
                    }

                    for(int m = 0; m < cartasLogicas.size(); m++){
                        for(StackPane c : cartasGraficas){
                            Label label = (Label) c.lookup("#LabelTop");
                            if(cartasLogicas.get(m).toString().equals(label.getText())){
                                cartasSeleccionadas.add(c);
                                break;
                            }
                        }
                    }
                    return;
                }
            }
        }

        CartaInglesa cartaLogica = encontrarCartaSeleccionada(carta);
        if(cartaLogica != null) {
            cartasLogicas.insertaFin(cartaLogica);
            cartasSeleccionadas.add(carta);
        }
    }



    // remover de la lógica
    public void removerLogicaOrigen(){
        if(contenedorOrigen != null){
            Label labelTop = (Label) cartaSeleccionada.lookup("#LabelTop");

            if(contenedorOrigen instanceof StackPane){
                StackPane contenedor = (StackPane) contenedorOrigen;
                int index = wasteZone.indexOf(contenedor);
                WasteZone[] wasteZones = tableroLogico.getWasteZones();
                wasteZones[index].removerCarta();
            }
            else if (contenedorOrigen instanceof VBox) {
                VBox vBox = (VBox) contenedorOrigen;

                if(foundations.contains(vBox)) {
                    int index = foundations.indexOf(vBox);
                    FoundationDeck[] foundationDecks = tableroLogico.getFoundationDecks();
                    foundationDecks[index].removerUltimaCarta();
                }
                else if(tableaus.contains(vBox)) {
                    int index = tableaus.indexOf(vBox);
                    TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();
                    TableauDeck tLogico = tableauDecks[index];

                    for(int i = 0; i < tLogico.getTableau().size(); i++) {
                        if(tLogico.getTableau().get(i).toString().equals(labelTop.getText())) {
                            tLogico.removerCartas(tLogico.getTableau().get(i));
                            break;
                        }
                    }
                }
            }
        }
    }




}