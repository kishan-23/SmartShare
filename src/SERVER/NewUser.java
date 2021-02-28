package SERVER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewUser {
    private String userID , userName, email, pass;
    Connection con;
    public NewUser( Connection con ) {
        this.con = con;
    }

    public boolean registerUser(String name, String id, String mail, String pass) throws SQLException {

        this.userName = name;
        this.userID = id;
        this.email = mail;
        this.pass = pass;
        Password p = new Password(pass);
        System.out.println("Password created.."+p.getHash());
        String str = " insert into userinfo (userId, userName, email, password)" + " values (?, ?, ?, ?) ";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(str);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ps.setString(1,id);
        ps.setString(2,name);
        ps.setString(3,mail);
        ps.setString(4,p.getHash());
        ps.execute();
        System.out.println("registered");

        return true;
    }
}
