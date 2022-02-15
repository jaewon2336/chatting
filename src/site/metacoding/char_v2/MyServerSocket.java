package site.metacoding.char_v2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServerSocket {

    // 리스너(연결받기) -> 메인 스레드
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;

    // 서버는 메시지 여러명에게 받아서 보내기(순차적)
    // 새로운 스레드, 클라이언트 수마다
    // 채팅서버는 메시지(요청)를 받을 때만 동작! -> pull 서버

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            고객리스트 = new ArrayList<>();

            // while 돌리기
            // 여러사람이 요청할 때마다 소켓이 새로 생성되어야하기 때문에 전역변수로 생성 X
            while (true) {
                Socket socket = serverSocket.accept(); // 대기 -> main 스레드가 하는 일
                System.out.println("클라이언트 연결됨");

                // while의 stack이 종료되면 t의 값이 가비지컬렉션 되기 때문에
                // 고객 socket을 기억하기 위해 전역 ArrayList에 보관하기
                고객전담스레드 t = new 고객전담스레드(socket);
                고객리스트.add(t); // 클라이언트 각각의 socket을 가지고있음
                System.out.println("고객리스트 크기 : " + 고객리스트.size());

                new Thread(t).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 내부 클래스
    class 고객전담스레드 implements Runnable {

        // 소켓 보관 컬렉션
        Socket socket;

        public 고객전담스레드(Socket socket) {
            this.socket = socket;
        }

        // 버퍼
        @Override
        public void run() {

        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
    }

}
