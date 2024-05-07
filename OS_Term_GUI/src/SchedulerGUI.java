import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SchedulerGUI extends JFrame {
    public SchedulerGUI() {
        // 프레임 타이틀 설정
        setTitle("Basic Swing GUI");
        // 프레임 크기 설정
        setSize(800, 600);
        // 프레임을 화면 가운데에 위치
        setLocationRelativeTo(null);
        // 프레임 닫을 때 프로그램 종료 처리
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 레이아웃 설정
        setLayout(new BorderLayout());

        // 텍스트 필드 생성
        JTextField textField = new JTextField();
        add(textField, BorderLayout.NORTH);

        // 버튼 생성
        JButton button = new JButton("Click Me!");
        add(button, BorderLayout.SOUTH);

        // 버튼 이벤트 리스너 설정
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 버튼 클릭 시 텍스트 필드에 문자열 설정
                textField.setText("Hello, Swing!");
            }
        });

        // GUI를 화면에 표시
        setVisible(true);
    }

    public static void main(String[] args) {
        // 안전하게 GUI를 생성하기 위해 이벤트 디스패치 스레드에서 실행
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SchedulerGUI();
            }
        });
    }
}
