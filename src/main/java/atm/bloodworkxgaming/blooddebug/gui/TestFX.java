package atm.bloodworkxgaming.blooddebug.gui;

import atm.bloodworkxgaming.blooddebug.tiles.TileCollector;
import atm.bloodworkxgaming.blooddebug.tiles.TileManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.minecraft.client.Minecraft;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button button = new Button();
        button.setText("Say 'Hello World'");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(Minecraft.getMinecraft().world.playerEntities.get(0).posX);
                // System.out.println("Hello World");
            }
        });

        TileManager manager = new TileManager();
        manager.collectTileList(null);



        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (TileCollector tileCollector : manager.getSortedList()) {
            items.add(tileCollector.toString());
        }
        listView.setItems(items);
        listView.setEditable(false);



        StackPane root = new StackPane();
        root.getChildren().addAll(button, listView);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("MAAAGIC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
