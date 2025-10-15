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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.util.HashMap;
import java.util.Random;

public class EightOffVisualController {
    // Reemplazar estas variables de instancia
    private Manager manager;
    private StackPane cartaSeleccionada = null;
    private ListaCircularSimple<CartaInglesa> cartasLogicas = new ListaCircularSimple<>();
    private ArrayList<StackPane> cartasSeleccionadas = new ArrayList<>(); // Para múltiples cartas
    private javafx.scene.Parent contenedorOrigen = null; // Cambiar de VBox a Parent
    private ArrayList<StackPane> cartasGraficas = new ArrayList<>();
    private ListaCircularSimple<Estado> listaEstados = new ListaCircularSimple<>();


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
    @FXML Button clueButton;

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

    /**
     * Inicializa el controlador configurando todos los componentes visuales y sus event handlers.
     * Configura los botones con sus efectos hover, genera las cartas gráficas, las distribuye
     * en el tablero y habilita las funcionalidades de arrastrar y soltar.
     *
     * @throws IOException si hay error al cargar los recursos FXML
     */
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

        //Click ClueButton
        clueClick();

        //Hover ClueButton
        hoverBoton(clueButton);


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

    /**
     * Configura el espaciado vertical de los VBox contenedores de cartas.
     * Usa espaciado negativo para crear el efecto de cartas superpuestas en los tableaus
     * y foundations, permitiendo ver parte de cada carta en la pila.
     */
    public void configurarVBoxes() {
        for (VBox vbox : new VBox[]{t1, t2, t3, t4, t5, t6, t7, t8}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-70); // Espaciado negativo para superponer cartas
        }

        for (VBox vbox : new VBox[]{f1, f2, f3, f4}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-95);
        }



    }


    /**
     * Crea los ArrayLists que almacenan referencias a los contenedores del juego.
     * Organiza los tableaus (8 columnas), foundations (4 pilas) y waste zones (8 espacios)
     * en listas para facilitar su acceso mediante índices.
     */
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

    /**
     * Genera todas las cartas gráficas del juego cargando el diseño FXML de cada carta.
     * Configura el tamaño fijo, los labels con los valores y palos, y los estilos visuales
     * de cada carta. Las cartas generadas se almacenan en el ArrayList cartasGraficas.
     *
     * @throws IOException si hay error al cargar el archivo FXML de las cartas
     */
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

    /**
     * Actualiza la interfaz gráfica sincronizándola con el estado lógico del tablero.
     * Coloca cada carta gráfica en su contenedor correspondiente (tableau, foundation o waste zone)
     * según la distribución actual del tablero lógico. Ajusta la opacidad y efectos hover
     * según si las cartas están boca arriba o boca abajo.
     *
     * @throws IOException si hay error durante la actualización
     */
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
            if(wasteZones[indexWasteZone].getCarta() != null) {
                CartaInglesa cartaLogica = wasteZones[indexWasteZone].getCarta();
                if (cartaLogica != null) {
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
            }
            indexWasteZone++;
        }
    }



    /**
     * Verifica si una carta puede ser arrastrada por el usuario.
     * Una carta es arrastrable si está boca arriba (opacidad 1.0) y no está en un foundation.
     *
     * @param cartaSeleccionada la carta a verificar
     * @return true si la carta puede ser arrastrada, false en caso contrario
     */
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

    /**
     * Verifica si el jugador ha ganado la partida.
     * La victoria ocurre cuando los 4 foundations contienen 13 cartas cada uno
     * (un palo completo del As al Rey en cada uno).
     *
     * @return true si se cumple la condición de victoria, false en caso contrario
     */
    public boolean verificarVictoria(){
        int contVictoria = 0;
        for(VBox f:foundations){
            if(f.getChildren().size() == 13){
                contVictoria++;
            }
        }
        return contVictoria == 4;
    }

    /**
     * Muestra un cuadro de diálogo de victoria cuando el jugador completa el juego.
     * Ofrece opciones para jugar nuevamente o regresar al menú principal.
     * Las respuestas del usuario ejecutan reiniciarJuego() o regrearAlMenu() respectivamente.
     */
    private void mostrarMensajeVictoria() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION);
        alert.setTitle("Felicitaciones!");
        alert.setHeaderText("Has ganado!");
        alert.setContentText("Completaste EightOff!\n¿Quieres jugar otra vez?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                reiniciarJuego();
            }
            if (response == ButtonType.NO) {
                regrearAlMenu();
            }
        });
    }

    /**
     * Regresa al menú principal del juego utilizando el Manager.
     * Obtiene la ventana actual desde el Stage de uno de los tableaus y
     * llama al método iniciarEscenaMenu() del manager.
     */
    public void regrearAlMenu(){
        manager = new Manager((Stage) t1.getScene().getWindow());
        try {
            manager.iniciarEscenaMenu();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Reinicia el juego actual iniciando una nueva partida.
     * Utiliza el Manager para cargar una nueva escena de juego con un tablero
     * recién mezclado y distribuido.
     */
    public void reiniciarJuego(){
        manager = new Manager((Stage) t1.getScene().getWindow());
        try {
            manager.iniciarEscenaJuego();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * Agrega efectos visuales de hover a una carta.
     * Cuando el mouse entra sobre la carta, cambia el color de fondo, el borde
     * y añade una sombra difuminada. Al salir, restaura el estilo original.
     *
     * @param carta el StackPane de la carta a la que se le agregará el hover
     */
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

    /**
     * Elimina los efectos de hover de una carta y restaura su estilo visual normal.
     * Remueve los event handlers de mouse y establece el estilo por defecto
     * con fondo blanco y borde morado pastel.
     *
     * @param carta el StackPane de la carta a la que se le quitará el hover
     */
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

    /**
     * Configura efectos visuales de hover para los botones de acción del juego.
     * Al pasar el mouse sobre el botón, cambia a un color rojizo pastel con sombra.
     * Al salir, restaura el estilo original del botón.
     *
     * @param boton el botón al que se le agregará el efecto hover
     */
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


    /**
     * Configura los efectos visuales de hover para el botón de deshacer (undo).
     * Cambia a un color amarillo pastel con sombra cuando el mouse está sobre él,
     * y restaura el color oscuro original cuando el mouse sale.
     */
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

    /**
     * Configura la funcionalidad del botón de deshacer (undo).
     * Al hacer click, restaura el estado anterior del juego desde la lista de estados,
     * actualizando el tablero lógico y refrescando la interfaz gráfica.
     * Solo funciona si existe al menos un estado guardado.
     */
    public void undoClick(){
        //Regresa un movimiento
        undoButton.setOnMouseClicked(e -> {
            if(!listaEstados.isEmpty()){
                Estado estado = listaEstados.eliminaFin();
                tableroLogico.setFoundationDecks(tableroLogico.clonarFoundationDecks(estado.getFoundationDecks()));
                tableroLogico.setTableauDecks(tableroLogico.clonarTableauDecks(estado.getTableauDecks()));
                tableroLogico.setWasteZones(tableroLogico.clonarWasteZones(estado.getWasteZones()));
                tableroLogico.restaurarVisibilidadCartas(estado.getVisibilidadCartas());
                limpiarGUI();
                try {
                    actualizarCartas();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * Guarda el estado actual del juego en la lista de estados para poder deshacerlo después.
     * Crea un nuevo objeto Estado con copias del estado actual de los tableaus, foundations,
     * waste zones y la visibilidad de las cartas, y lo agrega al final de la lista.
     */
    public void undoSave(){
        Estado estado = new  Estado(tableroLogico.getTableauDecks(),
                tableroLogico.getFoundationDecks(), tableroLogico.getWasteZones(), tableroLogico.guardarVisibilidadCartas());
        listaEstados.insertaFin(estado);
    }



    /**
     * Configura la funcionalidad del botón de pista (clue).
     * Al hacer click, busca un movimiento válido disponible y resalta visualmente
     * tanto la carta de origen como la carta de destino del movimiento sugerido.
     * Si no hay movimientos disponibles, imprime un mensaje en consola.
     */
    public void clueClick(){
        clueButton.setOnMouseClicked(e -> {
            if(tableroLogico.canMove()) {
                if (!tableroLogico.getMovimientosDisponibles().isEmpty()) {
                    CartaInglesa[] movimientoSeleccionado = tableroLogico.tomarMovimientoDisponible();

                    if (movimientoSeleccionado != null) {
                        for (StackPane carta : cartasGraficas) {
                            Label labelTop = (Label) carta.lookup("#LabelTop");

                            // Resaltar carta origen
                            if (labelTop.getText().equals(movimientoSeleccionado[0].toString())) {
                                resaltarCarta(carta);
                            }

                            // Resaltar carta destino (si existe)
                            if (movimientoSeleccionado[1] != null &&
                                    labelTop.getText().equals(movimientoSeleccionado[1].toString())) {
                                resaltarCarta(carta);
                            }
                        }
                    }
                }
            } else {
                // Opcional: mostrar mensaje de que no hay movimientos
                System.out.println("No hay movimientos disponibles");
            }
        });
    }

    /**
     * Resalta visualmente una carta con un efecto de sombra dorada durante 3 segundos.
     * Utiliza un hilo separado para esperar el tiempo y luego restaura el estilo original.
     * Este método es usado por la funcionalidad de pistas para indicar movimientos sugeridos.
     *
     * @param carta la carta que se va a resaltar
     */
    public void resaltarCarta(StackPane carta){
        // Guardar el estilo original
        String estiloOriginal = carta.getStyle();

        // Aplicar resaltado
        carta.setStyle(carta.getStyle() +
                "-fx-effect: dropshadow(gaussian, gold, 20, 0.9, 0, 0);");

        // Restaurar después de 3 segundos
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> {
                    carta.setStyle(estiloOriginal);
                });
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * Verifica si existen movimientos válidos disponibles en el estado actual del juego.
     * Consulta al tablero lógico para determinar si el jugador puede realizar alguna acción.
     * En caso de error, imprime el mensaje y asume que hay movimientos disponibles.
     *
     * @return true si hay movimientos disponibles, false si no hay
     */
    public boolean hayMovimientos(){
        try {
            return tableroLogico.canMove();
        } catch (Exception e) {
            System.err.println("Error al verificar movimientos: " + e.getMessage());
            e.printStackTrace();
            return true; // Asumir que hay movimientos si hay error
        }
    }

    /**
     * Muestra un cuadro de diálogo de derrota cuando no quedan movimientos válidos.
     * Ofrece al jugador las opciones de iniciar una nueva partida o regresar al menú principal.
     * Las respuestas ejecutan reiniciarJuego() o regrearAlMenu() según la elección.
     */
    public void mostrarMensajeDeDerrota(){
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION);
        alert.setTitle("Lo sientoo!");
        alert.setHeaderText("Has perdido!");
        alert.setContentText("¿Quieres jugar otra vez?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                reiniciarJuego();
            }
            if (response == ButtonType.NO) {
                regrearAlMenu();
            }
        });
    }




    /**
     * Limpia todos los contenedores visuales del juego.
     * Remueve todas las cartas de los tableaus, foundations y waste zones,
     * dejando la interfaz vacía para ser actualizada con un nuevo estado.
     */
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

    /**
     * Limpia las variables relacionadas con la selección actual de cartas.
     * Resetea la lista de cartas lógicas, la carta seleccionada visualmente,
     * el ArrayList de cartas seleccionadas y el contenedor de origen a null o vacío.
     */
    private void limpiarSeleccion() {
        cartasLogicas = null;
        cartaSeleccionada = null;
        cartasSeleccionadas.clear();
        contenedorOrigen = null;
    }





    /**
     * Busca y retorna la carta lógica correspondiente a una carta visual seleccionada.
     * Recorre todos los foundations, tableaus y waste zones comparando el texto del label
     * con el toString() de las cartas lógicas hasta encontrar la coincidencia.
     *
     * @param carta el StackPane de la carta visual seleccionada
     * @return la CartaInglesa lógica correspondiente, o null si no se encuentra
     */
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


    /**
     * Determina si el puntero del raton al soltar una carta
     * se encuentra sobre alguna fundacion o tableau.
     * Si es asi, devuelve el contenedor correspondiente para procesar el movimiento.
     * Retorna null si no se solto sobre ninguna zona valida.
     */
    public VBox reachTableauOrFoundation(MouseEvent e){
        // Recorre las fundaciones y verifica si el cursor esta dentro de sus limites
        for(VBox f:foundations){
            if(f.getLayoutX() <= e.getSceneX() && f.getLayoutX()+f.getWidth() >= e.getSceneX()
                    && f.getLayoutY() <= e.getSceneY() && f.getLayoutY()+f.getHeight() >= e.getSceneY()){
                return f;
            }
        }

        // Recorre los tableaus (columnas principales)
        for(VBox t:tableaus){
            if(t.getLayoutX() <= e.getSceneX() && t.getLayoutX()+t.getWidth() >= e.getSceneX()
                    && t.getLayoutY() <= e.getSceneY() && t.getLayoutY()+t.getHeight() >= e.getSceneY()){
                return t;
            }
        }

        return null;
    }

    /**
     * Detecta si el cursor al soltar una carta esta sobre alguna zona de descarte (waste zone).
     * Devuelve el contenedor StackPane correspondiente o null si no hay coincidencia.
     */
    public StackPane reachWasteZone(MouseEvent e){
        for (StackPane w:wasteZone){
            if(w.getLayoutX() <= e.getSceneX() && w.getLayoutX()+w.getWidth() >= e.getSceneX()
                    && w.getLayoutY() <= e.getSceneY() && w.getLayoutY()+w.getHeight() >= e.getSceneY()){
                return w;
            }
        }

        return null;
    }

    /**
     * Asigna el comportamiento de arrastre (drag) a todas las cartas graficas.
     * Detecta cuando una carta es presionada y verifica si puede moverse.
     * Si es asi, guarda su referencia y aplica un efecto visual de seleccion.
     */
    public void setDraggable(){
        for(StackPane carta:cartasGraficas){
            carta.setOnMousePressed(event -> {
                if(isDraggable(carta)){
                    cartaSeleccionada = carta;

                    // Identifica la carta logica correspondiente y posibles secuencias
                    identificarOrigenYCartas(carta);

                    // Aplica un efecto de sombra morada a las cartas seleccionadas
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

    /**
     * Asigna el comportamiento al soltar una carta (drop).
     * Determina si la carta fue soltada sobre una fundacion, tableau o waste zone
     * e intenta moverla segun las reglas del juego.
     */
    public void setDragReleased(){
        for(StackPane carta:cartasGraficas){
            carta.setOnMouseReleased(event -> {
                if(carta == cartaSeleccionada) {

                    // Detecta el contenedor donde se solto la carta
                    VBox contenedorVBox = reachTableauOrFoundation(event);
                    StackPane contenedorStackPane = reachWasteZone(event);

                    if(contenedorVBox != null) {
                        undoSave();

                        // Si se solto sobre una fundacion
                        if(foundations.contains(contenedorVBox)) {
                            int indice = foundations.indexOf(contenedorVBox);
                            FoundationDeck[] foundationDecks = tableroLogico.getFoundationDecks();
                            FoundationDeck fLogico = foundationDecks[indice];

                            // Solo se puede mover una carta a la fundacion
                            if(cartasLogicas.size() == 1 &&
                                    fLogico.ingresarCarta(cartasLogicas.get(0))){
                                removerLogicaOrigen();
                            }
                        }
                        // Si se solto sobre un tableau
                        else if(tableaus.contains(contenedorVBox)) {
                            int indice = tableaus.indexOf(contenedorVBox);
                            TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();
                            TableauDeck tLogico = tableauDecks[indice];

                            // Intenta insertar la secuencia de cartas
                            if(tLogico.insertarCartas(cartasLogicas)) {
                                removerLogicaOrigen();
                            }
                        }
                    }
                    // Si se solto sobre una zona de descarte
                    else if(contenedorStackPane != null) {
                        undoSave();
                        int indice = wasteZone.indexOf(contenedorStackPane);
                        WasteZone[] wasteZones = tableroLogico.getWasteZones();
                        WasteZone wLogico = wasteZones[indice];

                        // Solo permite mover una carta a la zona de descarte vacia
                        if(cartasLogicas.size() == 1 && wLogico.getCarta() == null){
                            wLogico.setCarta(cartasLogicas.get(0));
                            removerLogicaOrigen();
                        }
                    }

                    // Actualiza toda la interfaz grafica tras el movimiento
                    try {
                        limpiarGUI();
                        actualizarCartas();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Limpia la seleccion actual
                    limpiarSeleccion();
                    event.consume();
                }

                // Verifica condiciones de victoria o derrota
                if(verificarVictoria()){
                    mostrarMensajeVictoria();
                }
                if(!hayMovimientos()){
                    mostrarMensajeDeDerrota();
                }
            });
        }
    }

    /**
     * Obtiene una lista de cartas logicas en secuencia a partir de la carta seleccionada.
     * Busca la carta en los tableaus y retorna todas las cartas que siguen a esa en orden.
     * Tambien asocia sus representaciones graficas correspondientes.
     */
    public ListaCircularSimple<CartaInglesa> obtenerCartasEnSecuencia(StackPane carta){
        ListaCircularSimple<CartaInglesa> logicCardsList = new ListaCircularSimple<>();
        TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();
        Label labelTop = (Label) carta.lookup("#LabelTop");

        // Busca la carta en cada tableau y extrae las que siguen
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

        // Relaciona las cartas logicas con sus graficas
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

    /**
     * Identifica el origen logico (tableau o zona) y agrupa las cartas
     * que forman parte de la secuencia seleccionada para moverlas.
     */
    private void identificarOrigenYCartas(StackPane carta) {
        cartasSeleccionadas.clear();
        cartasLogicas = new ListaCircularSimple<>();

        Label labelTop = (Label) carta.lookup("#LabelTop");
        TableauDeck[] tableauDecks = tableroLogico.getTableauDecks();

        // Busca la carta seleccionada dentro de los tableaus
        for(int j=0; j<tableauDecks.length; j++) {
            ListaCircularSimple<CartaInglesa> tLogico = tableauDecks[j].getTableau();
            for (int i = 0; i < tLogico.size(); i++) {
                if (tLogico.get(i).toString().equals(labelTop.getText())) {
                    // Agrega la carta y las que siguen en secuencia
                    for(int k = i; k < tLogico.size(); k++) {
                        cartasLogicas.insertaFin(tLogico.get(k));
                    }

                    // Busca las versiones graficas de esas cartas
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

        // Si no esta en un tableau, se asume que proviene de una waste zone o fundacion
        CartaInglesa cartaLogica = encontrarCartaSeleccionada(carta);
        if(cartaLogica != null) {
            cartasLogicas.insertaFin(cartaLogica);
            cartasSeleccionadas.add(carta);
        }
    }

    /**
     * Elimina las cartas movidas de su contenedor logico de origen.
     * Determina si la carta proviene de una fundacion, tableau o waste zone
     * y actualiza la logica del tablero en consecuencia.
     */
    public void removerLogicaOrigen(){
        if(contenedorOrigen != null){
            Label labelTop = (Label) cartaSeleccionada.lookup("#LabelTop");

            // Si proviene de una waste zone
            if(contenedorOrigen instanceof StackPane){
                StackPane contenedor = (StackPane) contenedorOrigen;
                int index = wasteZone.indexOf(contenedor);
                WasteZone[] wasteZones = tableroLogico.getWasteZones();
                wasteZones[index].removerCarta();
            }
            // Si proviene de una fundacion o tableau
            else if (contenedorOrigen instanceof VBox) {
                VBox vBox = (VBox) contenedorOrigen;

                // Remueve de la fundacion
                if(foundations.contains(vBox)) {
                    int index = foundations.indexOf(vBox);
                    FoundationDeck[] foundationDecks = tableroLogico.getFoundationDecks();
                    foundationDecks[index].removerUltimaCarta();
                }
                // Remueve de un tableau
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