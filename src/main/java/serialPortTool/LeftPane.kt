package serialPortTool

import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import yizheneng.Driver.SerialPort

class LeftPane : BorderPane() {
    var portNamesChoiceBox = ChoiceBox<String>()
    var baudChoiceBox = ChoiceBox<Int>()
    var openButton = Button("打开")

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

        val topPane = VBox()

        topPane.children.add(portsLabel)
        topPane.children.add(portNamesChoiceBox)
        topPane.children.add(baudLabel)
        topPane.children.add(baudChoiceBox)

        this.top = topPane
        this.bottom = openButton
    }
}