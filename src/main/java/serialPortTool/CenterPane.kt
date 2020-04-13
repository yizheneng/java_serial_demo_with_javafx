package serialPortTool

import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import yizheneng.Driver.SerialPort

class CenterPane : VBox() {
    private var mRunnable = Runnable{}
    private var runFlag = true
    private val LOG_TAG = "CenterPane"
    private val receiverText = TextArea()
    private val sendText = TextArea()

    init {
        id = "CenterPane"
//        children.add(Button())
        val receiverLabel = Label("接收数据:")
        receiverText.maxHeight = 3080.0
        receiverText.isWrapText = true

        val sendLabel = Label("发送数据:")
        sendText.maxHeight = 50.0
        sendText.isWrapText = true

        val sendButton = Button("发  送")
        sendButton.setOnAction {
            if(SerialPort.isOpened()) {
                SerialPort.send(sendText.text.toByteArray(Charsets.UTF_8))
            } else {
                val alert = Alert(Alert.AlertType.WARNING)
                alert.title = "警告"
                alert.headerText = null
                alert.contentText = "请打开串口！"
                alert.showAndWait()
            }
        }

        this.children.addAll(receiverLabel, receiverText, sendLabel, sendText, sendButton)
        VBox.setVgrow(receiverText, Priority.ALWAYS)
        start()
    }

    fun stop() {
        runFlag = false
    }

    fun start() {
        mRunnable = Runnable {
            run {
                while (runFlag) {
                    Thread.sleep(5)
                    if(SerialPort.isOpened()) {
                        val dataBuf = SerialPort.readData()
                        if(dataBuf.isNotEmpty()) {
                            receiverText.text = receiverText.text + String(dataBuf)
                        }
                    }
                }
            }
        }

        Thread(mRunnable).start()
    }
}