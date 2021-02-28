package SERVER;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Server {
    public static void main(String[] args) throws IOException, SQLException {

        Connection con = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            //Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/smartshare", "root", "");
            System.out.println("..Database connection established..");
        } catch (Exception e) {
            System.out.println("Exception in database connection: " + e.getMessage());
            e.printStackTrace();
        }

        ServerSocket ss = new ServerSocket(5128);
        System.out.println("Server Created");
        while (true) {
            System.out.println("Waiting for client..");
            Socket s = ss.accept();
            System.out.println("User.Client Arrived :)");
            LoginSignUp handle = new LoginSignUp(s,con);
            Thread thread = new Thread(handle);
            thread.start();
        }
    }
}