package eightoff_gui;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//Cambia entre escenas
public class Manager {
    private Stage stage;



    Manager(Stage stage) {
        this.stage = stage;
    }

    //Carga la escena creada para el menu
    public void iniciarEscenaMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/practica4" +
                "_eightoff/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MenuController controller = fxmlLoader.getController();
        controller.setManager(this); // <--- esto es clave
        stage.setFullScreen(false);
        stage.setTitle("Solitario - Bienvenida");
        stage.setScene(scene);
        stage.show();
    }

    //Carga la escena del juego
    public void iniciarEscenaJuego() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/practica4" +
                "_eightoff/Tablero.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setFullScreen(false);
        stage.setTitle("Solitario - Juego");
        stage.setScene(scene);
        stage.show();
    }
}
