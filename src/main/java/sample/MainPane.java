package sample;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import yizheneng.Utils.JniLogger;

public class MainPane extends AnchorPane {
    String LOG_TAG = "MainPane";
    MainPane() {
        Button helloBtn = new Button("你好");
        this.getChildren().add(helloBtn);

        helloBtn.setOnAction(e -> {
            JniLogger.i(LOG_TAG, "nihaos");
        });
    }
}
