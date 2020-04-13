package serialPortTool

import yizheneng.Driver.SerialPort

object Serial {
    var mRunnable = Runnable{}
    var runnFlag = true
    val LOG_TAG = "SerialPort"

    fun start() {
        mRunnable = Runnable {
            run {
                while (runnFlag) {
                    Thread.sleep(5)
                    if(SerialPort.isOpened()) {

                    }
                }
            }
        }

        Thread(mRunnable).start()
    }

    fun stop() {
        runnFlag = false
    }
}