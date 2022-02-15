package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.event.SwingPropertyChangeSupport;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    BufferedReader reader;
    Scanner scan;
    String getMsg;

    public MyClientSocket() {
        try {

            // 스캐너 달고(반복x), 키보드로부터 입력 받는 부분(반복o)
            scan = new Scanner(System.in);

            // IP주소, 포트번호
            // 서버소켓과 연결
            socket = new Socket("localhost", 1077);
            // 쌤 IP 192.168.0.132

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            // 메시지 읽기
            new Thread(() -> {
                // 메세지가 얼마나 길지 모르니까 buffer에 계속 받기위해 while은 돌려야함
                while (true) {
                    try {
                        String inputData = reader.readLine();
                        System.out.println("서버 -> 클라이언트 : " + inputData);
                        // 응답 받으면 break, stateless
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            while (true) {
                // 순차적이라서 스레드 필요 없음
                getMsg = scan.nextLine();
                // 마지막 메세지 끝에는 \n이 꼭 필요함
                writer.write("Msg : " + getMsg + " \n"); // 버퍼에 담김
                writer.flush(); // 버퍼가 가득 차지 않았기 때문에 비워줘야 함
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
