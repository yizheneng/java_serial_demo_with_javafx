package serialPortTool

import javafx.scene.layout.BorderPane

class MainPane  : BorderPane() {
    var LOG_TAG = "MainPane"
    var titlePane = TitlePane()
    var leftPane = LeftPane()
    var centerPane = CenterPane()

    init {
        top = titlePane
        left = leftPane
        center = centerPane

        titlePane.minHeight = 50.0
        titlePane.maxHeight = 50.0

        leftPane.minWidth = 100.0
        leftPane.maxWidth = 100.0
    }
}