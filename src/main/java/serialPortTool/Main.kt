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
        println(os)
        if (os.toLowerCase().startsWith("win")) {
            loadLocalLib("/native-lib.dll")
        } else {
            loadLocalLib("/libnative-lib.so")
        }
        val scene = Scene(MainPane(), 800.0, 600.0)
        scene.stylesheets.add(javaClass.getResource("/style.css").toExternalForm())
        primaryStage.title = "串口调试助手"
        primaryStage.scene = scene
        primaryStage.show()
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