package BackEnd;

import java.io.IOException;
import java.net.Socket;

public class Client {
    Socket socket;

    public Client() throws IOException {
        socket = new Socket("localhost",5128);
        System.out.println("Connection established");
    }

    public void signUp() {

    }

    public void login() {

    }
}
