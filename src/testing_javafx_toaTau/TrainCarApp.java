package testing_javafx_toaTau;

//KHI CHẠY, NHỚ THÊM VÀO VM ARGUMENT (Run -> Run Congifuration -> Argument -> VM Argument)
//--module-path ./lib/javafx-sdk-21.0.8/lib --add-modules javafx.controls,javafx.fxml

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TrainCarApp extends Application {
    private static final double SEAT_GAP = 8;
    private static final double BLOCK_GAP = 10;
    private static final double CARRIAGE_GAP = 15;

    // === NGUỒN DỮ LIỆU DUY NHẤT: MỘT LIST BOOLEAN ĐƠN GIẢN ===
    private final List<Boolean> berthsState = new ArrayList<>();
    
    // VBox này sẽ chứa toàn bộ sơ đồ toa tàu. Chúng ta sẽ xóa và vẽ lại nội dung của nó.
    private VBox trainCarriageLayout;

    @Override
    public void start(Stage primaryStage) {
        // Bước 1: Khởi tạo dữ liệu trạng thái ban đầu
        initializeBerthsState();

        // Bước 2: Thiết lập các thành phần giao diện cố định
        VBox carriageContainer = new VBox(20);
        carriageContainer.setAlignment(Pos.CENTER);
        carriageContainer.setPadding(new Insets(20));
        carriageContainer.setStyle("-fx-background-color: #f0f0f0;");

        Label title = new Label("Toa số 1: Ngồi mềm điều hòa");
        title.getStyleClass().add("carriage-title");

        // trainCarriageLayout là vùng sẽ được vẽ lại
        trainCarriageLayout = new VBox(CARRIAGE_GAP);
        trainCarriageLayout.getStyleClass().add("train-carriage");
        trainCarriageLayout.setAlignment(Pos.CENTER);

        // Bước 3: Vẽ giao diện toa tàu lần đầu tiên
        reloadTrainCarriage();

        carriageContainer.getChildren().addAll(title, trainCarriageLayout);
        
        Scene scene = new Scene(carriageContainer);
        String cssPath = getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        primaryStage.setTitle("Sơ đồ Toa tàu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Hàm quan trọng: Xóa giao diện cũ và vẽ lại toàn bộ sơ đồ toa tàu
     * dựa trên dữ liệu hiện tại trong `berthsState`.
     */
    private void reloadTrainCarriage() {
        // Xóa toàn bộ nội dung cũ
        trainCarriageLayout.getChildren().clear();

        // Tạo lại nội dung mới từ dữ liệu
        HBox topSeatBlock = createSeatBlock(0, 32);
        Region mainWalkway = new Region();
        mainWalkway.getStyleClass().add("main-walkway");
        HBox bottomSeatBlock = createSeatBlock(32, 64);
        
        // Thêm nội dung mới vào layout
        trainCarriageLayout.getChildren().addAll(topSeatBlock, mainWalkway, bottomSeatBlock);
    }
    
    /**
     * Tạo một khối ghế. Hàm này không thay đổi nhiều, chỉ cần lấy dữ liệu từ berthsState.
     */
    private HBox createSeatBlock(int startIndex, int endIndex) {
        HBox seatBlock = new HBox(BLOCK_GAP);
        seatBlock.setAlignment(Pos.CENTER);
        
        VBox leftSeatSection = new VBox(SEAT_GAP);
        HBox leftRow1 = new HBox(SEAT_GAP);
        HBox leftRow2 = new HBox(SEAT_GAP);

        VBox rightSeatSection = new VBox(SEAT_GAP);
        HBox rightRow1 = new HBox(SEAT_GAP);
        HBox rightRow2 = new HBox(SEAT_GAP);
        
        for (int i = startIndex; i < endIndex; i++) {
            // Lấy index cho list (từ 0 đến 63)
            int dataIndex = i;
            String seatNumber = String.valueOf(dataIndex + 1);
            boolean isSold = berthsState.get(dataIndex);
            
            BerthNode.Orientation orientation = ((i % 16) < 8) ? BerthNode.Orientation.LEFT : BerthNode.Orientation.RIGHT;
            BerthNode node = new BerthNode(seatNumber, isSold, BerthNode.Type.SEAT, orientation);
            
            // Gắn sự kiện click, truyền vào INDEX của chỗ đó
            node.setOnMouseClicked(event -> handleBerthClick(dataIndex));
            
            int localIndex = i - startIndex;
            if (orientation == BerthNode.Orientation.LEFT) {
                if (localIndex % 2 == 0) leftRow1.getChildren().add(node);
                else leftRow2.getChildren().add(node);
            } else {
                if (localIndex % 2 == 0) rightRow1.getChildren().add(node);
                else rightRow2.getChildren().add(node);
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
     * Xử lý click, nhận vào INDEX của chỗ.
     * @param dataIndex Index trong list `berthsState`.
     */
    private void handleBerthClick(int dataIndex) {
        // Kiểm tra trạng thái từ list
        if (!berthsState.get(dataIndex)) {
            showPurchaseDialog(dataIndex);
        }
    }

    /**
     * Dialog mua vé, logic được cập nhật theo quy trình reload.
     * @param dataIndex Index của chỗ cần mua.
     */
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

        buyButton.setOnAction(e -> {
            // Kiểm tra lại dữ liệu trước khi thực hiện
            if (berthsState.get(dataIndex)) {
                dialogStage.close();
                javafx.application.Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Mua vé thất bại", "Rất tiếc, chỗ số " + seatNumber + " vừa được người khác mua."));
                return;
            }

            // Cập nhật dữ liệu trên List<Boolean>
            berthsState.set(dataIndex, true);
            
            // Đóng dialog
            dialogStage.close();

            // === RELOAD LẠI TOÀN BỘ GIAO DIỆN ===
            reloadTrainCarriage();
            
            // Thông báo thành công SAU KHI đã reload xong
            javafx.application.Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Mua vé thành công cho chỗ số " + seatNumber + "!"));
        });
        
        cancelButton.setOnAction(e -> dialogStage.close());
        
        vbox.getChildren().addAll(infoLabel, buttonBox);
        Scene dialogScene = new Scene(vbox, 300, 150);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }
    
    /**
     * Hàm trợ giúp để hiển thị Alert, tránh lặp code.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Khởi tạo List<Boolean> với dữ liệu ban đầu.
     */
    private void initializeBerthsState() {
        // Khởi tạo tất cả 64 chỗ là false (còn trống)
        for (int i = 0; i < 64; i++) {
            berthsState.add(false);
        }
        
        // Đánh dấu các chỗ đã bán ban đầu
        int[] soldSeatsArray = {8, 9, 16, 17, 24, 25, 32, 7, 18, 23, 26, 31, 6, 19, 22, 27, 30, 4, 5, 12, 20, 21, 33, 40, 41, 48, 49, 58, 38, 54, 36, 37, 45, 53};
        for (int seatNum : soldSeatsArray) {
            berthsState.set(seatNum - 1, true); // Chuyển từ số ghế (1-64) sang index (0-63)
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}