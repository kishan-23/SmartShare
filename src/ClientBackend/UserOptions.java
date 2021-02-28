package ClientBackend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UserOptions {
    Socket socket;
    DataInputStream din;
    DataOutputStream dout;

    public UserOptions(Socket s) throws IOException {
        this.socket = s;
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }
}
