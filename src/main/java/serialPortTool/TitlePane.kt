package serialPortTool

import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.HBox

class TitlePane : HBox() {
    val titleLabel = Label("串口调试助手")

    init {
        id = "TitlePane"
        this.padding = Insets(0.0, 0.0, 0.0, 20.0)

        titleLabel.id = "TitleLabel"
        titleLabel.minHeightProperty().bind(this.heightProperty().add(-20))
        titleLabel.maxHeightProperty().bind(this.heightProperty().add(-20))


        this.children.add(titleLabel)
    }
}