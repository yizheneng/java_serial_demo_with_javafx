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

        titlePane.minHeight = 80.0
        titlePane.maxHeight = 80.0

        leftPane.minWidth = 200.0
        leftPane.maxWidth = 200.0
    }
}