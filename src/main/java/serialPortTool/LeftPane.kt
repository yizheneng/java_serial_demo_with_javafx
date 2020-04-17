package serialPortTool

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import yizheneng.Driver.SerialPort
import java.util.*
import kotlin.concurrent.timerTask


class LeftPane : BorderPane() {
    private var portNamesChoiceBox = ChoiceBox<String>()
    private var baudChoiceBox = ChoiceBox<Int>()
    private var openButton = Button("打  开")
    private var serialOpenFlag = false
    private val timer = Timer()
    private val hexRev = ToggleButton("十六进制接收")
    private val hexSend = ToggleButton("十六进制发送")

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

        var portList = SerialPort.listPorts()
        if(portList.isNotEmpty()) {
            portNamesChoiceBox.items.addAll(portList)
        }

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
                Serial.serialPort.close()
                openButton.text = "打  开"
            } else {
                if (Serial.serialPort.open(portNamesChoiceBox.value, baudChoiceBox.value)) {
                    serialOpenFlag = !serialOpenFlag
                    openButton.text = "关  闭"
                } else {
                    val alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "错误"
                    alert.headerText = null
                    alert.contentText = "串口打开失败,\r\n" + Serial.serialPort.error
                    alert.showAndWait();
                }
            }
        }


        hexRev.isSelected = true
        hexRev.styleClass.add("selectButton")
        val stringRev = ToggleButton("字符串接收")
        stringRev.styleClass.add("selectButton")
        val revMode = ToggleGroup()
        revMode.toggles.addAll(hexRev, stringRev)
        val revHbox = HBox()
        revHbox.children.addAll(hexRev, stringRev)

        hexSend.isSelected = true
        hexSend.styleClass.add("selectButton")
        val stringSend = ToggleButton("字符串发送")
        stringSend.styleClass.add("selectButton")
        val sendMode = ToggleGroup()
        sendMode.toggles.addAll(hexSend, stringSend)
        val sendHbox = HBox()
        sendHbox.children.addAll(hexSend, stringSend)

        val time = CheckBox("时间戳")
        time.styleClass.add("checkBox")
        time.isDisable = true

        val wordwrapCheckbox = CheckBox("自动换行")
        wordwrapCheckbox.styleClass.add("checkBox")
        wordwrapCheckbox.onMouseClicked = EventHandler{
            time.isDisable = !wordwrapCheckbox.isSelected
        }



        val topPane = VBox()
        topPane.spacing = 10.0
        topPane.children.addAll(portsLabel, portNamesChoiceBox, baudLabel, baudChoiceBox, revHbox, sendHbox, wordwrapCheckbox, time)



        this.top = topPane
        this.bottom = openButton

        timer.schedule(timerTask {
            Platform.runLater {
                if(!Serial.serialPort.isOpened) {
                    var portList = SerialPort.listPorts().toList()
                    var portListOld = portNamesChoiceBox.items
                    var addList : List<String> = listOf()

                    for (portName in portList) {
                        if(!portListOld.contains(portName)) {
                            addList = addList + portName
                        }
                    }

                    if(addList.isNotEmpty())
                        portNamesChoiceBox.items.addAll(addList)
                }
        } },1000, 1000)
    }

    fun stop() {
        timer.cancel()
    }
}