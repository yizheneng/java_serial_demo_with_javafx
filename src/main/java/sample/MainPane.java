package sample;

import com.browniebytes.javafx.control.DateTimePicker;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import yizheneng.Driver.SerialPort;
import yizheneng.Utils.JniLogger;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

//import javafx.scene.control.*;

public class MainPane extends GridPane {
    String LOG_TAG = "MainPane";
    JFXButton openButton = new JFXButton("开始");
    JFXTextField touchTimeTextField = new JFXTextField("5");
    DateTimePicker datePicker = new DateTimePicker();
    JFXComboBox<String> portListComboBox = new JFXComboBox<>();
    Label leftTime = new Label("0");
    long endTime = 0;
    Timer timer = new Timer();
    boolean startFlag = false;
    SerialPort serialPort = new SerialPort();
    long touchTime = 0;
    boolean openFlag = false;

    MainPane() {
        openButton.setButtonType(JFXButton.ButtonType.FLAT);
        openButton.setStyle("-fx-background-color:black;-fx-background-color:#5264AE;-fx-text-fill:WHITE;");

        ObservableList<String> portList = FXCollections.observableList(Arrays.asList(SerialPort.listPorts()));
        portListComboBox.setItems(portList);
        portListComboBox.getSelectionModel().select(0);

        Label portListLabel = new Label("串口:");
        this.add(portListLabel, 0,0);
        this.add(portListComboBox, 1,0);

        this.add(new Label("持续点击时间(秒):"), 0,1);
        this.add(touchTimeTextField, 1,1);

        this.add(new Label("开始时间:"), 0, 2);
        this.add(datePicker, 1, 2);

        this.add(new Label("剩余时间:"), 0, 3);
        this.add(leftTime, 1, 3);

        this.add(openButton, 0, 4, 2, 1);

        portListLabel.setMinWidth(100);
        datePicker.minWidthProperty().bind(this.widthProperty().subtract(120));
        datePicker.maxWidthProperty().bind(this.widthProperty().subtract(120));

        portListComboBox.minWidthProperty().bind(this.widthProperty().subtract(110));
        portListComboBox.maxWidthProperty().bind(this.widthProperty().subtract(110));

        openButton.minWidthProperty().bind(this.widthProperty().subtract(10));
        openButton.maxWidthProperty().bind(this.widthProperty().subtract(10));

        GridPane.setHgrow(datePicker, Priority.ALWAYS);

        this.setPadding(new Insets(5, 5,5,5));
        this.setVgap(5);

        openButton.setOnAction(e -> {
            if(openButton.getText().equals("开始")) {
                if(!serialPort.open(portListComboBox.getSelectionModel().getSelectedItem(), 100)) {
                    JFXAlert alert = new JFXAlert();
                    alert.setTitle("警告");
                    alert.setHeaderText("串口打开错误！");
                    alert.setContentText(serialPort.getError());
                    alert.showAndWait();
                    return;
                }

                openButton.setText("停止");
//                serialPort.send(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
                portListComboBox.setDisable(true);
                touchTimeTextField.setDisable(true);
                datePicker.setDisable(true);
                endTime = datePicker.dateTimeProperty().get().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                touchTime = (int)(Double.valueOf(touchTimeTextField.getText()) * 1000);//Integer.valueOf(touchTimeTextField.getText());
                startFlag = true;
            } else {
                cancel();
            }
        });

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnce();
            }
        }, 100, 100);
    }

    public void cancel() {
        openButton.setText("开始");
        portListComboBox.setDisable(false);
        touchTimeTextField.setDisable(false);
        datePicker.setDisable(false);
        startFlag = false;
        if(openFlag) {
            openFlag = false;
            serialPort.send(new byte[]{0x00});
            JniLogger.i(LOG_TAG, "Close!");
        }

        serialPort.close();
    }

    private void runOnce() {
        if(startFlag) {
            Platform.runLater(() -> {
                leftTime.setText(Long.toString((endTime - System.currentTimeMillis()) / 1000));
            });

            long temp = (endTime - System.currentTimeMillis());
            if((temp <= 0)
                && (Math.abs(temp) < touchTime)) {
                if(!openFlag) {
                    openFlag = true;
                    serialPort.send(new byte[]{0x00});
                    JniLogger.i(LOG_TAG,"Open!");
                }
            } else {
                if(openFlag) {
                    openFlag = false;
                    serialPort.send(new byte[]{0x00});
                    JniLogger.i(LOG_TAG,"Close!");
                    Platform.runLater(() -> {
                        cancel();
                    });
                }
            }
        }
    }
}
