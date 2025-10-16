package testing_javafx_toaTau;

import java.util.ArrayList;
import java.util.List;

//SoftSeatCarPane.java

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
 * Module giao diện hoàn chỉnh cho một Toa Ngồi Mềm. Tự quản lý dữ liệu, giao
 * diện và sự kiện.
 */
public class SoftSeatCarPane extends VBox {
	private static final double SEAT_GAP_HORIZONTAL = 8;
	private static final double SEAT_GAP_VERTICAL = 2; // VGap = 0 theo yêu cầu
	private static final double BLOCK_GAP = 10;

	private final List<Boolean> berthsState = new ArrayList<>(64);
	private VBox trainCarriageLayout;

	public SoftSeatCarPane() {
		// --- Khởi tạo dữ liệu và các thành phần giao diện ---
		initializeBerthsState();
	}

	public Node buildLayout() {
		// trainCarriageLayout là VBox chứa sơ đồ ghế
		trainCarriageLayout = new VBox(30); // CARRIAGE_GAP
		trainCarriageLayout.setAlignment(Pos.CENTER);

		// Vẽ giao diện lần đầu
		reloadTrainCarriage();

		return trainCarriageLayout;
	}

	private void reloadTrainCarriage() {
		trainCarriageLayout.getChildren().clear();

		// Tạo lại nội dung từ dữ liệu với logic đánh số mới
		HBox topSeatBlock = createSeatBlock(true);
//		Region mainWalkway = new Region();
//		mainWalkway.getStyleClass().add("main-walkway");
		HBox bottomSeatBlock = createSeatBlock(false);

//		trainCarriageLayout.getChildren().addAll(topSeatBlock, mainWalkway, bottomSeatBlock);
		trainCarriageLayout.getChildren().addAll(topSeatBlock, bottomSeatBlock);
	}

	private HBox createSeatBlock(boolean isTopBlock) {
		HBox seatBlock = new HBox(BLOCK_GAP);
		seatBlock.setAlignment(Pos.CENTER);

		VBox leftSeatSection = new VBox(SEAT_GAP_VERTICAL);
		HBox leftRow1 = new HBox(SEAT_GAP_HORIZONTAL);
		HBox leftRow2 = new HBox(SEAT_GAP_HORIZONTAL);

		VBox rightSeatSection = new VBox(SEAT_GAP_VERTICAL);
		HBox rightRow1 = new HBox(SEAT_GAP_HORIZONTAL);
		HBox rightRow2 = new HBox(SEAT_GAP_HORIZONTAL);

		// Vòng lặp 8 cột (từ 0 đến 7)
		for (int col = 0; col < 8; col++) {
			// Xác định 4 ghế trong một cột zigzag
			int seat1_num, seat2_num, seat3_num, seat4_num;

			if (col % 2 == 0) { // Cột chẵn (0, 2, 4, 6) -> đi xuống
				seat1_num = col * 4 + 1;
				seat2_num = col * 4 + 2;
				seat3_num = col * 4 + 3;
				seat4_num = col * 4 + 4;
			} else { // Cột lẻ (1, 3, 5, 7) -> đi lên
				seat1_num = col * 4 + 4;
				seat2_num = col * 4 + 3;
				seat3_num = col * 4 + 2;
				seat4_num = col * 4 + 1;
			}

			// Tạo các BerthNode cho khối ghế trên (ghế 1, 2)
			if (isTopBlock) {
				leftRow1.getChildren().add(createBerthNode(seat1_num, BerthNode.Orientation.LEFT));
				leftRow2.getChildren().add(createBerthNode(seat2_num, BerthNode.Orientation.LEFT));

				// Ghế bên phải bắt đầu từ số 33 (col 0 của bên phải tương ứng ghế 33)
				rightRow1.getChildren().add(createBerthNode(seat1_num + 32, BerthNode.Orientation.RIGHT));
				rightRow2.getChildren().add(createBerthNode(seat2_num + 32, BerthNode.Orientation.RIGHT));
			}
			// Tạo các BerthNode cho khối ghế dưới (ghế 3, 4)
			else {
				leftRow1.getChildren().add(createBerthNode(seat3_num, BerthNode.Orientation.LEFT));
				leftRow2.getChildren().add(createBerthNode(seat4_num, BerthNode.Orientation.LEFT));

				rightRow1.getChildren().add(createBerthNode(seat3_num + 32, BerthNode.Orientation.RIGHT));
				rightRow2.getChildren().add(createBerthNode(seat4_num + 32, BerthNode.Orientation.RIGHT));
			}
		}

		leftSeatSection.getChildren().addAll(leftRow1, leftRow2);
		rightSeatSection.getChildren().addAll(rightRow1, rightRow2);
		Region aisleDivider = new Region();
		aisleDivider.getStyleClass().add("aisle-divider");
		seatBlock.getChildren().addAll(leftSeatSection, aisleDivider, rightSeatSection);
		return seatBlock;
	}

	/**
	 * Hàm trợ giúp để tạo một BerthNode và gắn sự kiện.
	 * 
	 * @param seatNumber  Số ghế (từ 1 đến 64).
	 * @param orientation Hướng của lưng ghế.
	 * @return Một BerthNode đã được cấu hình.
	 */
	private BerthNode createBerthNode(int seatNumber, BerthNode.Orientation orientation) {
		int dataIndex = seatNumber - 1;
		boolean isSold = berthsState.get(dataIndex);

		BerthNode node = new BerthNode(String.valueOf(seatNumber), isSold, BerthNode.Type.SEAT, orientation);
		node.setOnMouseClicked(_ -> handleBerthClick(dataIndex));
		return node;
	}

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
		// Khởi tạo tất cả 64 chỗ là false (còn trống)
		for (int i = 0; i < 64; i++) {
			berthsState.add(false);
		}

		// Đánh dấu các chỗ đã bán ban đầu
		int[] soldSeatsArray = { 8, 9, 16, 17, 24, 25, 32, 7, 18, 23, 26, 31, 6, 19, 22, 27, 30, 4, 5, 12, 20, 21, 33,
				40, 41, 48, 49, 58, 38, 54, 36, 37, 45, 53 };
		for (int seatNum : soldSeatsArray) {
			berthsState.set(seatNum - 1, true); // Chuyển từ số ghế (1-64) sang index (0-63)
		}
	}
}