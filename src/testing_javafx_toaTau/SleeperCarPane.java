// SleeperCarPane.java (Phiên bản cuối cùng, đã được dọn dẹp và tái cấu trúc)
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Một "nhà máy" tạo ra layout cho Toa Giường Nằm. Lớp này không phải là một
 * component giao diện, mà là nơi chứa logic để xây dựng và trả về một Node giao
 * diện hoàn chỉnh.
 */
public class SleeperCarPane {

	// --- Các biến cấu hình và dữ liệu ---
	private final int numberOfTiers;
	private final int numberOfCompartments = 7;
	private final int bedsPerCompartment;
	private final int totalBerths;

	private final List<Boolean> berthsState = new ArrayList<>();

	// Biến để giữ tham chiếu đến toàn bộ layout, cần cho việc reload
	private VBox rootLayout;

	/**
	 * Constructor chỉ nhận vào số tầng và khởi tạo dữ liệu.
	 * 
	 * @param numberOfTiers Số tầng của toa (1, 2, hoặc 3).
	 */
	public SleeperCarPane(int numberOfTiers) {
		this.numberOfTiers = Math.max(1, Math.min(numberOfTiers, 3));
		this.bedsPerCompartment = this.numberOfTiers * 2;
		this.totalBerths = this.numberOfCompartments * this.bedsPerCompartment;
		initializeBerthsState();
	}

	/**
	 * Phương thức chính: Xây dựng và trả về Node chứa toàn bộ giao diện của toa.
	 * Chỉ gọi hàm này một lần khi khởi tạo.
	 * 
	 * @return Một Node có thể được thêm vào CarriageFrame.
	 */
	public Node buildLayout() {
		// Layout chính của module này (không phải của toàn bộ cửa sổ)
		rootLayout = new VBox(10); // Spacing giữa nhãn khoang và lưới giường
		reloadTrainCarriage(); // Vẽ nội dung lần đầu

		HBox carBody = new HBox(15);
		carBody.setAlignment(Pos.CENTER_LEFT);

		VBox tierLabels = createTierLabels();

		carBody.getChildren().addAll(tierLabels, rootLayout);
		return carBody;
	}

	private void reloadTrainCarriage() {
		rootLayout.getChildren().clear();

		// === GIẢI PHÁP: THÊM MỘT "ĐỆM" LINH HOẠT Ở TRÊN CÙNG ===

		// 1. Tạo một Region đệm.
		Region spacer = new Region();

		// 2. Yêu cầu VBox (rootLayout) luôn ưu tiên cho Region này nở ra theo chiều
		// dọc.
		VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

		HBox compartmentLabels = createCompartmentLabels();
		HBox bedsLayout = createBedsLayout();

		// Thêm ĐỆM vào trước tiên, nó sẽ đẩy mọi thứ khác xuống dưới.
		rootLayout.getChildren().addAll(spacer, compartmentLabels, bedsLayout);
	}

	/**
	 * Tạo ra layout HBox chứa tất cả các khoang giường và vách ngăn.
	 */
	private HBox createBedsLayout() {
		HBox layout = new HBox();
		layout.getStyleClass().add("beds-grid-container");

		// Thêm tường ở đầu
		VBox startDivider = new VBox();
		startDivider.getStyleClass().add("compartment-divider");
		layout.getChildren().add(startDivider);

		for (int khoang = 0; khoang < numberOfCompartments; khoang++) {
			GridPane compartmentPane = new GridPane();
			compartmentPane.getStyleClass().add("compartment-pane");

			for (int hang = 0; hang < numberOfTiers; hang++) {
				int baseNum = khoang * bedsPerCompartment;
				int tierOffset = (numberOfTiers - 1 - hang) * 2;
				int seatNumLeft = baseNum + tierOffset + 1;
				int seatNumRight = baseNum + tierOffset + 2;

				BerthNode leftBed = createBerthNode(seatNumLeft);
				BerthNode rightBed = createBerthNode(seatNumRight);

				compartmentPane.add(leftBed, 0, hang);
				compartmentPane.add(rightBed, 1, hang);
			}
			layout.getChildren().add(compartmentPane);

			// Thêm tường ở cuối mỗi khoang
			VBox endDivider = new VBox();
			endDivider.getStyleClass().add("compartment-divider");
			layout.getChildren().add(endDivider);
		}
		return layout;
	}

	// --- CÁC HÀM CÒN LẠI GIỮ NGUYÊN ---
	// (Không có bất kỳ thay đổi nào trong các hàm dưới đây)

	private BerthNode createBerthNode(int seatNumber) {
		int dataIndex = seatNumber - 1;
		boolean isSold = berthsState.get(dataIndex);
		BerthNode node = new BerthNode(String.valueOf(seatNumber), isSold, BerthNode.Type.BED,
				BerthNode.Orientation.BOTTOM);
		node.setOnMouseClicked(_ -> handleBerthClick(dataIndex));
		return node;
	}

	private void handleBerthClick(int dataIndex) {
		if (!berthsState.get(dataIndex)) {
			showPurchaseDialog(dataIndex);
		}
	}

	private void showPurchaseDialog(int dataIndex) {
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
			reloadTrainCarriage(); // <--- Dòng quan trọng
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

	private VBox createTierLabels() {
		VBox labels = new VBox();
		// Thay đổi 1: Căn xuống dưới cùng
		labels.setAlignment(Pos.BOTTOM_LEFT);
		labels.setSpacing(25);
		// Thay đổi 2: Xóa padding trên, thêm padding dưới để căn chỉnh
		labels.setPadding(new Insets(0, 0, 10, 0));

		for (int i = numberOfTiers; i >= 1; i--) {
			Label tierLabel = new Label("T" + i);
			tierLabel.getStyleClass().add("tier-label");
			labels.getChildren().add(tierLabel);
		}
		return labels;
	}

	private HBox createCompartmentLabels() {
		HBox labels = new HBox();
		labels.setAlignment(Pos.CENTER);
		for (int i = 1; i <= 7; i++) {
			Label lbl = new Label("Khoang " + i);
			lbl.getStyleClass().add("compartment-label");
			HBox.setHgrow(lbl, javafx.scene.layout.Priority.ALWAYS);
			lbl.setMaxWidth(Double.MAX_VALUE);
			labels.getChildren().add(lbl);
		}
		return labels;
	}

	private void initializeBerthsState() {
		for (int i = 0; i < totalBerths; i++) {
			berthsState.add(false);
		}
		if (totalBerths == 42) {
			int[] soldSeatsArray = { 5, 6, 11, 17, 18, 29, 30, 42, 4, 9, 10, 15, 16, 21, 22, 27, 33, 34, 39, 40, 1, 2,
					7, 8, 13, 14, 19, 20, 25, 26, 31, 32, 37, 38 };
			for (int seatNum : soldSeatsArray) {
				if (seatNum <= totalBerths) {
					berthsState.set(seatNum - 1, true);
				}
			}
			berthsState.set(2, false);
		}
	}
}