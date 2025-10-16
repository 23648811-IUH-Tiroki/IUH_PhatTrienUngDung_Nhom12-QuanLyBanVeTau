// HardSeatCarPane.java
package testing_javafx_toaTau;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Module giao diện hoàn chỉnh cho một Toa Ngồi Cứng.
 */
public class HardSeatCarPane {
	private static final int TOTAL_SEATS = 72;
	private static final double SEAT_GAP_VERTICAL = 0;
	private static final double CLUSTER_GAP = 10; // Khoảng trống giữa các cụm

	private final List<Boolean> berthsState = new ArrayList<>();
	private VBox trainCarriageLayout;

	public HardSeatCarPane() {
		initializeBerthsState();
	}

	public Node buildLayout() {
		trainCarriageLayout = new VBox(35); // Khoảng cách giữa khối trên, hành lang, khối dưới
		trainCarriageLayout.setAlignment(Pos.CENTER);
		reloadTrainCarriage();
		return trainCarriageLayout;
	}

	private void reloadTrainCarriage() {
		trainCarriageLayout.getChildren().clear();

		// Tạo lại nội dung từ dữ liệu
		HBox topSeatBlock = createSeatBlock(true);
		HBox bottomSeatBlock = createSeatBlock(false);

		trainCarriageLayout.getChildren().addAll(topSeatBlock, bottomSeatBlock);
	}

	/**
	 * Tạo một khối ghế (trên hoặc dưới) cho toàn bộ toa.
	 * 
	 * @param isTopBlock True nếu là khối ghế trên, False nếu là khối ghế dưới.
	 */
	private HBox createSeatBlock(boolean isTopBlock) {
		HBox seatBlock = new HBox(); // Không dùng spacing ở đây, sẽ set margin thủ công
		seatBlock.setAlignment(Pos.CENTER);

		int totalColumns = TOTAL_SEATS / 4; // 18 cột

		for (int col = 0; col < totalColumns; col++) {
			// Mỗi cột là một VBox chứa 2 ghế
			VBox columnBox = new VBox(SEAT_GAP_VERTICAL);

			// Xác định số ghế dựa trên cột và khối (trên/dưới)
			int seatNum1, seatNum2;
			if (isTopBlock) {
				seatNum1 = col * 4 + 1;
				seatNum2 = col * 4 + 2;
			} else {
				seatNum1 = col * 4 + 3;
				seatNum2 = col * 4 + 4;
			}

			// --- ÁP DỤNG LUẬT HƯỚNG GHẾ ---
			BerthNode.Orientation orientation;
			if (col % 2 == 0) { // Cột lẻ (index 0, 2, 4...) -> quay phải
				orientation = BerthNode.Orientation.LEFT;
			} else { // Cột chẵn (index 1, 3, 5...) -> quay trái
				orientation = BerthNode.Orientation.RIGHT;
			}

			BerthNode node1 = createBerthNode(seatNum1, orientation);
			BerthNode node2 = createBerthNode(seatNum2, orientation);

			columnBox.getChildren().addAll(node1, node2);
			seatBlock.getChildren().add(columnBox);

			// --- THÊM KHOẢNG TRỐNG GIỮA CÁC CỤM ---
			// Một cụm là 2 cột. Sau mỗi cột chẵn (index 1, 3, 5...), thêm khoảng trống
			if (col % 2 != 1 && col < totalColumns - 1) {
				Region spacer = new Region();
				spacer.setMinWidth(CLUSTER_GAP);
				seatBlock.getChildren().add(spacer);
			}
		}

		return seatBlock;
	}

	private BerthNode createBerthNode(int seatNumber, BerthNode.Orientation orientation) {
		int dataIndex = seatNumber - 1;
		boolean isSold = berthsState.get(dataIndex);

		BerthNode node = new BerthNode(String.valueOf(seatNumber), isSold, BerthNode.Type.SEAT, orientation);
		node.setOnMouseClicked(_ -> handleBerthClick(dataIndex));
		return node;
	}

	// Các hàm xử lý sự kiện (handleBerthClick, showPurchaseDialog, showAlert)
	// và initializeBerthsState giữ nguyên, chỉ thay đổi số lượng ghế.

	private void handleBerthClick(int dataIndex) {
		if (!berthsState.get(dataIndex)) {
			showPurchaseDialog(dataIndex);
		}
	}

	private void showPurchaseDialog(int dataIndex) {
		// ... (Logic của hàm này giữ nguyên, không thay đổi)
		String seatNumber = String.valueOf(dataIndex + 1);

		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setTitle("Xác nhận Mua vé");

		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		Label infoLabel = new Label("Số chỗ: " + seatNumber);
		infoLabel.setStyle("-fx-font-size: 16px;");

		Button buyButton = new Button("Mua");
		Button cancelButton = new Button("Bỏ");
		HBox buttonBox = new HBox(15, buyButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);

		buyButton.setOnAction(_ -> {
			if (berthsState.get(dataIndex)) {
				dialogStage.close();
				Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Mua vé thất bại",
						"Rất tiếc, chỗ số " + seatNumber + " vừa được người khác mua."));
				return;
			}

			berthsState.set(dataIndex, true);
			dialogStage.close();
			reloadTrainCarriage();
			Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Thông báo",
					"Mua vé thành công cho chỗ số " + seatNumber + "!"));
		});

		cancelButton.setOnAction(_ -> dialogStage.close());

		vbox.getChildren().addAll(infoLabel, buttonBox);
		Scene dialogScene = new Scene(vbox, 300, 150);
		dialogStage.setScene(dialogScene);
		dialogStage.showAndWait();
	}

	private void showAlert(Alert.AlertType type, String title, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void initializeBerthsState() {
		for (int i = 0; i < TOTAL_SEATS; i++) {
			berthsState.add(false);
		}
		// Thêm một vài ghế đã bán để làm ví dụ
		berthsState.set(4, true); // Ghế 5
		berthsState.set(5, true); // Ghế 6
		berthsState.set(10, true); // Ghế 11
		berthsState.set(23, true); // Ghế 24
	}
}