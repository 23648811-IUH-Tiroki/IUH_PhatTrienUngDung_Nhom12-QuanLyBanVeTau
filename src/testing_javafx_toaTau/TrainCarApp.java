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

		// 1. Tạo module cho toa ngồi mềm và build layout của nó
		SoftSeatCarPane softSeatModule = new SoftSeatCarPane();
		Node softSeatLayout = softSeatModule.buildLayout();
		CarriageFrame frame = new CarriageFrame("Toa số 1: Ngồi mềm điều hòa", softSeatLayout);

		// 2. Tạo module cho toa giường nằm (ví dụ 2 tầng) và build layout của nó
//		SleeperCarPane sleeperCarModule = new SleeperCarPane(3);
//		Node sleeperCarLayout = sleeperCarModule.buildLayout();
//		CarriageFrame frame = new CarriageFrame("Toa số 3: Giường nằm khoang 6 điều hòa", sleeperCarLayout);

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
