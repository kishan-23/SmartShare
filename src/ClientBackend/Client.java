package ClientBackend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    Socket socket;
    DataInputStream din;
    DataOutputStream dout;

    public Client() throws IOException {
        socket = new Socket("localhost",5128);
        System.out.println("Connection established");
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public Socket getSocket() {
        return this.socket;
    }
    public boolean signUp(String name, String id ,String email, String pass) throws IOException {
        dout.writeUTF("SIGNUP");
        dout.writeUTF(name);
        dout.flush();
        dout.writeUTF(id);
        dout.flush();
        dout.writeUTF(email);
        dout.flush();
        dout.writeUTF(pass);
        dout.flush();
        boolean b = din.readBoolean();
        return b;
    }

    public boolean login(String userId, String pass) throws IOException {
        dout.writeUTF("LOGIN");
        dout.flush();
        dout.writeUTF(userId);
        dout.flush();
        dout.writeUTF(pass);
        dout.flush();
        return (din.readBoolean());
    }

    public void exit() throws IOException {
        socket.close();
    }
}
