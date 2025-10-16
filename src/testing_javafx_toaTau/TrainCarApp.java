package testing_javafx_toaTau;

//KHI CHẠY, NHỚ THÊM VÀO VM ARGUMENT (Run -> Run Congifuration -> Argument -> VM Argument)
//--module-path ./lib/javafx-sdk-21.0.8/lib --add-modules javafx.controls,javafx.fxml

//TrainCarApp.java (Phiên bản cuối cùng, sử dụng Module)

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TrainCarApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		// --- CHỌN TOA ĐỂ HIỂN THỊ ---

		// 1. Toa ngồi mềm
//		SoftSeatCarPane softSeatModule = new SoftSeatCarPane();
//		Node softSeatLayout = softSeatModule.buildLayout();
//		CarriageFrame frame = new CarriageFrame("Toa số 1: Ngồi mềm", softSeatLayout);

		// 2. Ngồi cứng
		HardSeatCarPane hardSeatModule = new HardSeatCarPane();
		Node hardSeatLayout = hardSeatModule.buildLayout();
		CarriageFrame frame = new CarriageFrame("Toa số 2: Ngồi cứng", hardSeatLayout);

		// 3. Toa giường nằm (biến số chỉ số tầng từ 1-3)
//		SleeperCarPane sleeperCarModule = new SleeperCarPane(3);
//		Node sleeperCarLayout = sleeperCarModule.buildLayout();
//		CarriageFrame frame = new CarriageFrame("Toa số 3: Giường nằm", sleeperCarLayout);

		// --- Hiển thị ---
		Scene scene = new Scene(frame);

		// Bước 3: Liên kết CSS
		String cssPath = getClass().getResource("style.css").toExternalForm();
		scene.getStylesheets().add(cssPath);

		// Bước 4: Hiển thị
		primaryStage.setTitle("Sơ đồ Toa tàu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
