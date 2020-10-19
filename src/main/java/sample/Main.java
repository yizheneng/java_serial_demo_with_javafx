package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String os = System.getProperty("os.name");
        System.out.println(os);
        if(os.toLowerCase().startsWith("win")){
            loadLocalLib("/nativeSerial_x64-lib.dll");
        } else {
            loadLocalLib("/libnative-lib.so");
        }

        MainPane mainPane = new MainPane();

        primaryStage.setTitle("湖南超能机器人技术有限公司");
        primaryStage.setScene(new Scene(mainPane, 330, 160));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            mainPane.cancel();
            try {
                Thread.sleep(1000);
            }catch (InterruptedException event) {

            }
            System.exit(0);
        });
    }

    public boolean loadLocalLib(String resLib) {
        try {
            //Finds a stream to the dll. Change path/class if necessary
            InputStream inputStream = getClass().getResource(resLib).openStream();
            //Change name if necessary
            File temporaryDll = File.createTempFile("jacob", ".dll");
            FileOutputStream outputStream = new FileOutputStream(temporaryDll);
            byte[] array = new byte[8192];
            for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
                outputStream.write(array, 0, i);
            }
            outputStream.close();
            temporaryDll.deleteOnExit();
            System.load(temporaryDll.getPath());
            return true;
        } catch(Throwable e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
