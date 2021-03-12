package SERVER;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSignUp implements Runnable{

    Socket s;
    Connection con;
    DataInputStream din;
    DataOutputStream dout;
    String user_id;
    int user_no;
    String user_name;
    String mail;

    public LoginSignUp (Socket s, Connection con) throws IOException, SQLException {
        this.con = con;
        this.s = s;
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());

    }

    private boolean validateLogin(Connection con, String userId, String pass) throws IOException {
        //String userId = din.readUTF();
        //String pass = din.readUTF();
        Password p = new Password(pass);
        String str1 = p.getHash();
        String query = "select password from userinfo where userId ="+"(?)";
        try{
            PreparedStatement ps1 = con.prepareStatement(query);
            ps1.setString(1,userId);
            ResultSet rs1 = ps1.executeQuery();
            if(rs1.next()) {
                String str2 = rs1.getString(1);
                if (str2.equals(str1))
                {
                    String qu = "select userNo, userName, email from userinfo where userId = "+"(?)";
                    PreparedStatement ps = null;
                    try {
                        ps = con.prepareStatement(qu);
                    } catch (SQLException throwables) {
                        System.out.println("exception in ps:"+throwables.getMessage());
                    }
                    ps.setString(1,userId);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    this.user_no = rs.getInt(1);
                    this.user_name = rs.getString(2);
                    this.mail = rs.getString(3);
                    return true;
                }
            }
            return false;
        }

        catch (SQLException throwables) {
            System.out.println("exception in validate login:"+throwables.getMessage());
        }
        return false;
    }

    public static boolean signUpUser(Connection con, DataInputStream din, DataOutputStream dout) throws IOException, SQLException {
        String name = din.readUTF();
        String userID = din.readUTF();
        String mail = din.readUTF();
        String pass = din.readUTF();
        boolean temp = false;
        //userID = din.readUTF();
        String query = "select password from userinfo where userId ="+"(?)";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,userID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) temp = false;
            else temp=true;
        }

        catch (SQLException throwables) {
            System.out.println("exception:"+throwables.getMessage());
        }

        finally {
            if(temp){
                NewUser u = new NewUser(con);
                return u.registerUser(name,userID,mail,pass);
            }

            else{
                return false;
            }
            /*dout.writeBoolean(temp);
            dout.flush();
            if(!temp) return false;*/
        }
        //}
        //while(!temp);*/




        /*String st = "Name :"+name+"\nUserID :"+userID+"\ne-mail :"+mail;
        dout.writeUTF(st);
        dout.flush();*/
        //return true;
    }

    @Override
    public void run() {
        String choice = null;
        do {
            try {
                choice = din.readUTF();

                //Validation of Login
                if (choice.equals(new String("LOGIN"))) {
                    String userId = din.readUTF();
                    String pass = din.readUTF();
                    boolean result = validateLogin(con,userId,pass);
                    dout.writeBoolean(result);
                    dout.flush();
                    UserHandler usr = new UserHandler(s,con,user_no);
                    usr.run();
                    break;
                }

                //For new user Registration
                else if (choice.equals(new String("SIGNUP"))) {
                    boolean b = signUpUser(con, din, dout);
                    dout.writeBoolean(b);
                    dout.flush();
                }

            } catch (IOException | SQLException e) {
                try {
                    s.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
                //System.out.println("exception 1" + e.getMessage());
            }
        }

        while (!choice.equals(new String("Quit")));

    }
}
