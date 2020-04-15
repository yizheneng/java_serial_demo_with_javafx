package serialPortTool

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.File
import java.io.FileOutputStream

class MainWindows : Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val os = System.getProperty("os.name")
        val javaSdkBit = System.getProperty("os.arch")
        println("$os---$javaSdkBit")
        if (os.toLowerCase().startsWith("win")) {
            if(javaSdkBit.contains("64")) {
                loadLocalLib("/nativeSerial_x64-lib.dll")
            } else {
                loadLocalLib("/native-lib.dll")
            }
        } else {
            if(javaSdkBit.contains("64")) {
                loadLocalLib("/libnativeSerial_x86-lib.so")
            } else {
                loadLocalLib("/libnativeSerial_x86-lib.so")
            }
        }

        val mainPane = MainPane()
        val scene = Scene(mainPane, 800.0, 600.0)
        scene.stylesheets.add(javaClass.getResource("/style.css").toExternalForm())
        primaryStage.title = "串口调试助手"
        primaryStage.scene = scene
        primaryStage.show()
        primaryStage.setOnCloseRequest {
            mainPane.stop()
        }
    }

    private fun loadLocalLib(resLib: String): Boolean {
        return try { //Finds a stream to the dll. Change path/class if necessary
            val inputStream = javaClass.getResource(resLib).openStream()
            //Change name if necessary
            val temporaryDll = File.createTempFile("jacob", ".dll")
            val outputStream = FileOutputStream(temporaryDll)
            val array = ByteArray(8192)
            var i = inputStream.read(array)
            while (i != -1) {
                outputStream.write(array, 0, i)
                i = inputStream.read(array)
            }
            outputStream.close()
            temporaryDll.deleteOnExit()
            System.load(temporaryDll.path)
            true
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }

    fun show(args: Array<String>) {
        launch(*args)
    }
}

fun main(args: Array<String>) {
    MainWindows().show(args)
}