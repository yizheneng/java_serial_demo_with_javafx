package serialPortTool

import javafx.scene.layout.BorderPane

class MainPane  : BorderPane() {
    private var LOG_TAG = "MainPane"
    private var titlePane = TitlePane()
    private var leftPane = LeftPane()
    private var centerPane = CenterPane()

    init {
        top = titlePane
        left = leftPane
        center = centerPane

        titlePane.minHeight = 80.0
        titlePane.maxHeight = 80.0

        leftPane.minWidth = 200.0
        leftPane.maxWidth = 200.0
    }

    fun stop() {
        centerPane.stop()
    }
}