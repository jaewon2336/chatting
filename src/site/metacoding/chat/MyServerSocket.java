package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    // OS의 socket 라이브러리 기반 System call
    ServerSocket serverSocket; // 연결하는지 지켜보는 리스너(연결 => 세션?)
    Socket socket; // 메시지 통신
    BufferedReader reader;

    public MyServerSocket() {
        // 통신은 무조건 예외가 발생할 수 있음!
        try {
            // 1. 서버소켓 생성(리스너)
            // 잘 알려진 포트 : 0 ~ 1023
            serverSocket = new ServerSocket(1077); // 내부적으로 while이 돈다.
            System.out.println("서버 소켓 생성됨");

            // 종료되지 않는 프로그램 -> 데몬(main 스레드)
            // 내부적으로 accept가 새로운 소켓을 만들어서 return하여 연결해줌
            // 이때 소켓의 포트번호는 사용하지 않는 포트 랜덤지정(OS에게 맡김)
            socket = serverSocket.accept(); // while을 돌면서 대기, 실제로 while 도는 메서드

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()) // socket 선 = conn
            );

            String inputData = reader.readLine();
            System.out.println("받은 메시지 : " + inputData);
            System.out.println("클라이언트 연결됨");
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
