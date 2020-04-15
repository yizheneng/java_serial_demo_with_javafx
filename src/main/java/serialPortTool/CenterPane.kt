package serialPortTool

import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import yizheneng.Driver.SerialPort
import java.nio.charset.Charset
import java.util.*

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
            if(Serial.serialPort.isOpened) {
                Serial.serialPort.send(sendText.text.toByteArray(Charsets.UTF_8))
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
                var i:Long = 0
                while (runFlag) {
                    Thread.sleep(5)

                    println("-------")
                    if(Serial.serialPort.isOpened) {
                        val dataBuf = Serial.serialPort.readData()
                        if(dataBuf.isNotEmpty()) {
//                            receiverText.text = receiverText.text + String(dataBuf)
                            println(Date().toString() + "   receive:" + String(dataBuf));
                        }

                        i = i.plus(1)
                        Serial.serialPort.send((i.toString()).toByteArray(Charset.defaultCharset()))
                    }
                }
            }
        }

        Thread(mRunnable).start()
    }
}