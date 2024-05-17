import javax.swing.SwingUtilities;


// Main Class
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProcessInputGUI();              // 실행하면 GUI 윈도우가 생성되며 프로젝트 시작
        });
    }
}