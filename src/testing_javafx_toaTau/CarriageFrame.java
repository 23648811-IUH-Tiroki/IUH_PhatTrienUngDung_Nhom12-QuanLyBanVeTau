// CarriageFrame.java
package testing_javafx_toaTau;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Module Khung Toa Chung, có kích thước cố định. Cung cấp một vùng để "cắm" nội
 * dung sơ đồ toa vào.
 */
public class CarriageFrame extends VBox {
	private static final double FIXED_FRAME_HEIGHT = 200; // Chiều cao cố định cho khung xanh
	private static final double FIXED_FRAME_WIDTH = 850; // Chiều rộng cố định cho khung xanh

	private final Label titleLabel;
	private final StackPane contentArea;

	public CarriageFrame(String title, Node carLayoutContent) {
		super(20); // Spacing giữa tiêu đề và khung

		// --- Cấu hình VBox chính ---
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(20));
		this.setStyle("-fx-background-color: #f0f0f0;");

		// --- Tiêu đề ---
		titleLabel = new Label(title);
		titleLabel.getStyleClass().add("carriage-title");

		// --- Khung Toa (StackPane) ---
		contentArea = new StackPane();
		contentArea.getStyleClass().add("train-carriage");

		// Thiết lập kích thước cố định
		contentArea.setMinSize(FIXED_FRAME_WIDTH, FIXED_FRAME_HEIGHT);
		contentArea.setPrefSize(FIXED_FRAME_WIDTH, FIXED_FRAME_HEIGHT);
		contentArea.setMaxSize(FIXED_FRAME_WIDTH, FIXED_FRAME_HEIGHT);

		// Đặt nội dung (sơ đồ toa) vào và căn chỉnh
		setContent(carLayoutContent);

		// Thêm tiêu đề và khung vào VBox chính
		this.getChildren().addAll(titleLabel, contentArea);
	}

	/**
	 * Phương thức để đặt hoặc thay đổi nội dung bên trong khung.
	 * 
	 * @param carLayoutContent Node chứa sơ đồ toa (ví dụ: một HBox hoặc VBox).
	 */
	public void setContent(Node carLayoutContent) {
		contentArea.getChildren().clear();
		if (carLayoutContent != null) {
			contentArea.getChildren().add(carLayoutContent);
			// Căn nội dung sang trái và vào giữa theo chiều dọc
			StackPane.setAlignment(carLayoutContent, Pos.CENTER_LEFT);
		}
	}

	/**
	 * Phương thức để thay đổi tiêu đề.
	 * 
	 * @param title Tiêu đề mới.
	 */
	public void setTitle(String title) {
		titleLabel.setText(title);
	}
}