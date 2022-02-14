package site.metacoding.chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;

    public MyClientSocket() {
        try {
            // IP주소, 포트번호
            // 서버소켓과 연결
            socket = new Socket("192.168.0.132", 1077);

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            // 마지막 메세지 끝에는 \n이 꼭 필요함
            writer.write("정재원 \n"); // 버퍼에 담김
            writer.flush(); // 버퍼가 가득 차지 않았기 때문에 비워줘야 함
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
