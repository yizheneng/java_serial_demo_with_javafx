package serialPortTool

import javafx.geometry.Insets
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import yizheneng.Driver.SerialPort
import yizheneng.Utils.JniLogger


class LeftPane : BorderPane() {
    var portNamesChoiceBox = ChoiceBox<String>()
    var baudChoiceBox = ChoiceBox<Int>()
    var openButton = Button("打  开")
    var serialOpenFlag = false

    init {
        id = "LeftPane"

        this.padding = Insets(10.0, 10.0, 10.0, 5.0)

        val baudLabel = Label("波特率：")
        baudLabel.styleClass.add("LeftPaneLabel")

        baudChoiceBox.maxWidth = 200.0;
        baudChoiceBox.items.addAll(1200, 4800, 9600, 115200)
        baudChoiceBox.value = 115200

        val portsLabel = Label("串口号：")
        portsLabel.styleClass.add("LeftPaneLabel")

        portNamesChoiceBox.maxWidth = 200.0;
        portNamesChoiceBox.items.addAll(SerialPort.listPorts())

        openButton.maxWidth = 200.0
        openButton.setOnAction {
            if(portNamesChoiceBox.value == null) {
                val alert = Alert(Alert.AlertType.WARNING)
                alert.title = "警告"
                alert.headerText = null
                alert.contentText = "请选择正确的串口！"
                alert.showAndWait()
                return@setOnAction
            }

            if(serialOpenFlag) {
                serialOpenFlag = !serialOpenFlag
                SerialPort.close()
                openButton.text = "打  开"
            } else {
                if (SerialPort.open(portNamesChoiceBox.value, baudChoiceBox.value)) {
                    serialOpenFlag = !serialOpenFlag
                    openButton.text = "关  闭"
                } else {
                    val alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "错误"
                    alert.headerText = null
                    alert.contentText = "串口打开失败！"
                    alert.showAndWait();
                }
            }
        }

        val topPane = VBox()

        topPane.children.add(portsLabel)
        topPane.children.add(portNamesChoiceBox)
        topPane.children.add(baudLabel)
        topPane.children.add(baudChoiceBox)

        this.top = topPane
        this.bottom = openButton
    }
}