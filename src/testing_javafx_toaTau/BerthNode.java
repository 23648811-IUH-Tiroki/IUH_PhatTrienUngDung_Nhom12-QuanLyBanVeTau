package testing_javafx_toaTau;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

/**
* Một Module "Chỗ" linh hoạt, có thể là Ghế hoặc Giường.
* Sử dụng BorderPane để đảm bảo layout luôn chính xác.
*/
public class BerthNode extends BorderPane {

 // Enum để xác định loại chỗ
 public enum Type {
     SEAT, // Ghế
     BED   // Giường
 }

 // Enum để xác định hướng của lưng/thành
 public enum Orientation {
     LEFT,  // Lưng ghế bên trái
     RIGHT, // Lưng ghế bên phải
     BOTTOM // Thành giường bên dưới
 }

 public BerthNode(String number, boolean isSold, Type type, Orientation orientation) {
     // 1. Tạo các thành phần con
     Label numberLabel = new Label(number);
     Region back = new Region(); // Đây là lưng ghế hoặc thành giường

     // 2. Áp dụng các style class từ CSS
     numberLabel.getStyleClass().add("berth-label");
     back.getStyleClass().add("berth-back");

     // 3. Cấu hình layout bằng BorderPane
     this.setCenter(numberLabel); // Số luôn ở giữa

     switch (orientation) {
         case LEFT:
             this.setLeft(back);
             // Thêm padding cho vùng center để số không bị dính vào lưng ghế
             BorderPane.setMargin(numberLabel, new Insets(0, 0, 0, 1));
             break;
         case RIGHT:
             this.setRight(back);
             BorderPane.setMargin(numberLabel, new Insets(0, 1, 0, 0));
             break;
         case BOTTOM:
             this.setBottom(back);
             BorderPane.setMargin(numberLabel, new Insets(1, 0, 0, 0));
             break;
     }

     // 4. Áp dụng style cho toàn bộ component
     this.getStyleClass().add("berth"); // Style chung
     this.getStyleClass().add(isSold ? "sold" : "available"); // Style trạng thái
     this.getStyleClass().add(type == Type.SEAT ? "seat-type" : "bed-type"); // Style loại chỗ
 }
}