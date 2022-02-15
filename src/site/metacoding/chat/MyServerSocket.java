package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServerSocket {

    // OS의 socket 라이브러리 기반 System call
    ServerSocket serverSocket; // 연결하는지 지켜보는 리스너(연결 => 세션이 만들어짐 => 인증됨!)
    Socket socket; // 메시지 통신
    BufferedReader reader;

    // 추가(클라이언트에게 메시지 보내기)
    BufferedWriter writer;
    Scanner scan;

    public MyServerSocket() {
        // 통신은 무조건 예외가 발생할 수 있음!
        try {
            // 1. 서버소켓 생성(리스너)
            // 잘 알려진 포트 : 0 ~ 1023
            serverSocket = new ServerSocket(1077); // 내부적으로 while이 돈다.
            System.out.println("서버 소켓 생성됨");
            System.out.println("aaa를 입력하면 종료됩니다.");

            // 종료되지 않는 프로그램 -> 데몬(main 스레드)
            // 내부적으로 accept가 새로운 소켓을 만들어서 return하여 연결해줌
            // 이때 소켓의 포트번호는 사용하지 않는 포트 랜덤지정(OS에게 맡김)
            // 포트번호 0 ~ 65536
            socket = serverSocket.accept(); // while을 돌면서 대기, 실제로 while 도는 메서드

            // 새로운 소켓을 알아야 버퍼를 달고 통신, 순서 중요
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()) // socket 선 = conn
            );

            // 메시지 보내기
            scan = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(() -> {
                // 익명 클래스 : 다른 공간이라서 try-catch 따로 걸어줘야함
                while (true) {
                    try {
                        String inputData = scan.nextLine();
                        writer.write(inputData + "\n");
                        writer.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 메시지 반복해서 받는 서버 소켓 - main 스레드
            while (true) {
                // System.out.println("클라이언트 연결됨");
                String inputData = reader.readLine();
                System.out.println("클라이언트 -> 서버 : " + inputData);
            }

        } catch (Exception e) {
            System.out.println("통신 오류 발생 : " + e.getMessage());
            // e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("main 종료");
    }

}
