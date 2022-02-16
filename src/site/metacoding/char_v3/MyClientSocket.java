package site.metacoding.char_v3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;

    // write 스레드
    Scanner sc;
    BufferedWriter writer;

    // read 스레드
    BufferedReader reader;

    public MyClientSocket() {
        try {
            // localhost = 127.0.0.1 루프백 주소
            // IP = 192.168.0.132
            socket = new Socket("192.168.0.132", 2000); // 연결

            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 새로운 스레드(읽기 전용)
            new Thread(new 읽기전담스레드()).start();

            // 메인 스레드(쓰기 전용)
            while (true) {
                String keyboardInputData = sc.nextLine();

                // 중계자(서버 소켓)에게만 write하면 됨
                // 끝에 "\n" 필수 -> 이거 안쓰려면 PrintWrite 쓰면 됨
                writer.write(keyboardInputData + "\n"); // 내 버퍼에 담기
                writer.flush(); // 버퍼에 담긴 데이터 Stream으로 흘려보내기
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 내부 클래스로 만들면 좋은점
    // MyClientSocket의 전역변수를 모두 new 없이 사용가능
    class 읽기전담스레드 implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    String inputData = reader.readLine();
                    System.out.println("받은 메시지 : " + inputData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }

}
