package serialPortTool

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
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
        receiverText.maxHeight = 2080.0

        val sendLabel = Label("发送数据:")
        sendText.maxHeight = 50.0

        val sendButton = Button("发  送")
        sendButton.setOnAction {
            SerialPort.send(sendText.text.toByteArray(Charsets.UTF_8))
        }

        this.children.addAll(receiverLabel, receiverText, sendLabel, sendText, sendButton)
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