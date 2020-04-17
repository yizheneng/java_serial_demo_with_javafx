package serialPortTool

import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority

class TitlePane : HBox() {
    private val titleLabel = Label("串口调试助手")
    private var clearReceivedButton:Button
    private var clearSendButton:Button
    private var saveToFileButton:Button
    private var aboutButton:Button
    private var spacing = Label()

    init {
        id = "TitlePane"
        this.padding = Insets(5.0, 100.0, 5.0, 20.0)

        titleLabel.id = "TitleLabel"
        titleLabel.minHeightProperty().bind(this.heightProperty().add(-20))
        titleLabel.maxHeightProperty().bind(this.heightProperty().add(-20))

        clearReceivedButton = Button("清除接收")
//        clearReceivedButton.minHeightProperty().bind(this.heightProperty().add(-20))
//        clearReceivedButton.maxHeightProperty().bind(this.heightProperty().add(-20))
//        clearReceivedButton.minWidthProperty().bind(this.heightProperty())
//        clearReceivedButton.maxWidthProperty().bind(this.heightProperty())
        clearReceivedButton.maxHeight = 200.0
        clearReceivedButton.styleClass.addAll("clearReceiveButton", "titleToolButton")
        clearReceivedButton.maxWidthProperty().bind(clearReceivedButton.heightProperty().add(10))
        clearReceivedButton.minWidthProperty().bind(clearReceivedButton.heightProperty().add(10))

        clearSendButton = Button("清除发送")
        clearSendButton.styleClass.addAll("clearSendButton", "titleToolButton")
        clearSendButton.maxHeight = 200.0
        clearSendButton.maxWidthProperty().bind(clearSendButton.heightProperty().add(10))
        clearSendButton.minWidthProperty().bind(clearSendButton.heightProperty().add(10))

        saveToFileButton = Button("保存文件")
        saveToFileButton.styleClass.addAll("saveToFileButton", "titleToolButton")
        saveToFileButton.maxHeight = 200.0
        saveToFileButton.maxWidthProperty().bind(saveToFileButton.heightProperty().add(10))
        saveToFileButton.minWidthProperty().bind(saveToFileButton.heightProperty().add(10))

        aboutButton = Button("关于")
        aboutButton.styleClass.addAll("aboutButton", "titleToolButton")
        aboutButton.maxHeight = 200.0
        aboutButton.maxWidthProperty().bind(aboutButton.heightProperty().add(10))
        aboutButton.minWidthProperty().bind(aboutButton.heightProperty().add(10))

        spacing.maxWidth = 2000.0
        setHgrow(spacing, Priority.ALWAYS)
        this.children.addAll(titleLabel, spacing, clearReceivedButton, clearSendButton, saveToFileButton, aboutButton)
    }
}