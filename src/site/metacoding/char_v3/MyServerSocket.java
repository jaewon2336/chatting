package site.metacoding.char_v3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyServerSocket {

    // 리스너(연결받기) -> 메인 스레드
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;
    boolean isLogin = true;

    // 서버는 메시지 여러명에게 받아서 보내기(순차적)
    // 새로운 스레드, 클라이언트 수마다
    // 채팅서버는 메시지(요청)를 받을 때만 동작! -> pull 서버

    public MyServerSocket() {
        while (isLogin) {
            try {
                serverSocket = new ServerSocket(2000);
                고객리스트 = new Vector<>(); // 동기화가 처리된 ArrayList

                // while 돌리기
                // 여러사람이 요청할 때마다 소켓이 새로 생성되어야하기 때문에 전역변수로 생성 X

                Socket socket = serverSocket.accept(); // 대기 -> main 스레드가 하는 일
                System.out.println("클라이언트 연결됨");

                // while의 stack이 종료되면 t의 값이 가비지컬렉션 되기 때문에
                // 고객 socket을 기억하기 위해 전역 ArrayList에 보관하기
                고객전담스레드 t = new 고객전담스레드(socket);
                고객리스트.add(t); // 클라이언트 각각의 socket을 가지고있음
                System.out.println("고객리스트 크기 : " + 고객리스트.size());

                new Thread(t).start();
            } catch (Exception e) {
                System.out.println("오류내용 : " + e.getMessage());
            }
        }
    }

    // 내부 클래스
    class 고객전담스레드 implements Runnable {

        // 소켓 보관 컬렉션
        Socket socket;
        BufferedReader reader;
        BufferedWriter writer;

        public 고객전담스레드(Socket socket) {
            this.socket = socket;
            // new될 때 양끝단에 버퍼달림
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                String inputData;
                try {
                    inputData = reader.readLine();
                    System.out.println("From 클라이언트 : " + inputData);

                    // 메시지 받았으니까 List<고객전담스레드> 고객리스트 <== 여기에 담긴
                    // 모든 클라이언트에게 메시지 전송(컬렉션 크기만큼 for문 돌려서!!)
                    // for(int i = 0; i<고객리스트.size(); i++) {
                    // 고객리스트.get(i).writer.write(inputData + "\n");
                    // 고객리스트.get(i).writer.flush();
                    // }

                    // 컬렉션의 1번째 데이터를 t에 넣는것
                    for (고객전담스레드 t : 고객리스트) { // 컬렉션타입 : 컬렉션
                        t.writer.write(inputData + "\n");
                        t.writer.flush();
                    }

                } catch (Exception e) {
                    // 클라이언트로부터 메세지를 읽는데, 클라이언트가 연결을 해지하면
                    // readline에서 대기하다가 Stream이 끊겨서 catch로 넘어오고
                    // while은 catch만 계속 반복해서 출력된다.
                    System.out.println("오류내용 : " + e.getMessage());
                    isLogin = false;
                    // 리스트에서 참조중이라서 사라지지 않으니까 heap에 떠있는 자신을 날림
                    고객리스트.remove(this);

                    try {
                        reader.close();
                        writer.close();
                        socket.close();
                    } catch (Exception e1) {

                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
    }

}
