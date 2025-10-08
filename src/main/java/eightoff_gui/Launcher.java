package eightoff_gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    //Codigo que actua como el sistema que da inicio al codigo mediante el inicio de la escena menu
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/practica4" +
                "_eightoff/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setFullScreen(false);
        stage.setTitle("Solitario - Bienvenida");
        stage.setScene(scene);
        stage.show();
        stage.resizableProperty().setValue(Boolean.FALSE);

        Manager manager = new Manager(stage);

        // Pasar el manager al controlador de bienvenida
        MenuController controller = fxmlLoader.getController();
        controller.setManager(manager);
    }



    //MAIN
    public static void main(String[] args) {
        launch(args);
    }
}
