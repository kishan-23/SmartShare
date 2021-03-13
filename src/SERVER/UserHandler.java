//This is the Threaded User Handler Class
package SERVER;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserHandler {
    Socket s;
    Connection con;
    DataInputStream din;
    DataOutputStream dout;
    String user_id;
    int user_no;
    String user_name;
    String mail;

    public UserHandler (Socket s, Connection con, int user_no) throws IOException, SQLException {
        this.con = con;
        this.s = s;
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());
        this.user_no=user_no;
    }

    public void upload() throws IOException, SQLException, ClassNotFoundException {
        String name, path, comment, query;
        name = din.readUTF();
        int maxd = din.readInt();
        comment=din.readUTF();
        String dt = din.readUTF();
        System.out.println(dt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dt,formatter);
        System.out.println(date);
        int i=0;
        path = "C:\\Users\\kishan\\MyServer\\@"+i+name+"\\";
        File f = new File(path);
        while(f.exists())
        {
            i++;
            path = "C:\\Users\\kishan\\MyServer\\@"+i+name+"\\";
            f = new File(path);
        }
        FileOutputStream fout = new FileOutputStream(f);

        byte[] buffer = new byte[10240];
        int length;
        while ((length = din.read(buffer)) > 0) {
            fout.write(buffer, 0, length);
            if(length<10240) break;
        }
        fout.close();
        System.out.println("i'm here out of loop");
        System.out.println("out");

        System.out.println("i'm here out of loop again");
        java.sql.Date sqldate1 = java.sql.Date.valueOf(date);
        java.sql.Date sqldate2 = java.sql.Date.valueOf(LocalDate.now());
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yy#HH:mm:ss");
        String key = ""+user_no+"@"+formatter2.format(new Date());

        query = "insert into fileinfo (fileName, filePath, uploadedOn, downloadsLeft, uploadedBy, availableUpto, fileKey, comments) "+"values (?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        ps = con.prepareStatement(query);
        ps.setString(1,name);
        ps.setString(2,path);
        ps.setDate(3,sqldate2);
        ps.setInt(4,maxd);
        ps.setInt(5,user_no);
        ps.setDate(6,sqldate1);
        ps.setString(7,key);
        ps.setString(8,comment);
        ps.execute();
        System.out.println("File added");
        dout.writeUTF(key);
        dout.flush();
    }

    public void download () throws IOException, SQLException {
        String key = din.readUTF();
        String name;
        java.util.Date jDate,aDate;
        String fPath;
        int maxD;
        long timL;
        String qur1 = "select filePath, downloadsLeft, uploadedOn, availableUpto, fileName from fileinfo where fileKey = "+"(?)";
        PreparedStatement prs1 = con.prepareStatement(qur1);
        prs1.setString(1,key);
        ResultSet res1 = prs1.executeQuery();
        if(res1.next())
        {
            fPath = res1.getString(1);
            maxD = res1.getInt(2);
            jDate = res1.getDate(3);
            aDate = res1.getDate(4);
            //timL = res1.getLong(5);
            name = res1.getString(5);
            //long diff = (new Date().getTime()-jDate.getTime())/60000;
            if(aDate.compareTo(new Date())<0)
            {
                dout.writeUTF("Time Limit for the download is exceeded.");
                dout.flush();
                this.delf(key);
            }
            else{
                maxD--;
                if(maxD<0)
                {
                    dout.writeUTF("Maximum number of downloads for this file is achieved.");
                    dout.flush();
                    this.delf(key);
                }
                else{
                    dout.writeUTF("OK");
                    dout.flush();
                    dout.writeUTF(name);
                    dout.flush();
                    File fil = new File(fPath);
                    FileInputStream fin = new FileInputStream(fil);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fin.read(buffer)) > 0) {
                        dout.write(buffer, 0, length);
                    }
                    dout.flush();
                    fin.close();
                    System.out.println("File sent");
                    String qur2 = "update fileinfo set downloadsLeft ="+"(?)"+"where fileKey = "+"(?)";
                    PreparedStatement prs2 = con.prepareStatement(qur2);
                    prs2.setInt(1,maxD);
                    prs2.setString(2,key);
                    prs2.executeUpdate();
                }
            }
        }
        else{
            dout.writeUTF("File not found.");
            dout.flush();
        }
    }

    public void history() throws SQLException, IOException {
        String qu = "select fileName, fileKey, downloadsLeft, availableUpto from fileinfo where "+"uploadedBy = "+"(?)";
        PreparedStatement ps = null;
        ResultSet rs =null;
        try{
            ps = con.prepareStatement(qu);
            ps.setInt(1,user_no);
            rs = ps.executeQuery();
        }
        catch(Exception e)
        {
            System.out.println("Exception in ps,rs: "+e.getMessage());
        }

        while (rs.next())
        {
            dout.writeBoolean(true);
            dout.flush();
            String fname = rs.getString(1);
            dout.writeUTF(fname);
            dout.flush();
            dout.writeUTF(rs.getString(2));
            dout.flush();
            dout.writeInt(rs.getInt(3));
            dout.flush();
            dout.writeUTF(rs.getDate(4).toString());
            dout.flush();
        }
        dout.writeBoolean(false);
        dout.flush();
    }

    public String delf(String key) throws SQLException {
        String qu1 = "select uploadedBy, filePath from fileinfo where fileKey = "+"(?)";
        PreparedStatement ps1 = con.prepareStatement(qu1);
        ps1.setString(1,key);
        ResultSet rs1 = ps1.executeQuery();
        if(rs1.next())
        {
            int uploader = rs1.getInt(1);
            String fpath = rs1.getString(2);
            if(uploader==user_no)
            {
                String qu2 = "delete from fileinfo where fileKey = "+"(?)";
                try{
                    File fl = new File(fpath);
                    if(fl.exists()){
                        System.out.println(""+fl.delete());
                    }
                }
                catch (Exception i)
                {
                    System.out.println("file not on server folder."+i.getMessage());
                    return "file not on server folder.";
                }
                PreparedStatement ps2 = con.prepareStatement(qu2);
                ps2.setString(1,key);
                ps2.executeUpdate();
                return "Successfully deleted.";
            }
            else {
                return "This file doesn't belong to you.";
            }
        }
        else{
            return  "no such file was found";
        }
    }

    public void delFile() throws IOException, SQLException {
        String key = din.readUTF();
        dout.writeUTF(delf(key));
        dout.flush();
    }

    public void logout()
    {
        try{
            s.close();
        }
        catch(Exception e)
        {
            System.out.println("exception: "+e.getMessage());
        }
    }

    public void run() {

            try {
                String ch = din.readUTF();
                while (!ch.equals(new String("Q")))
                {
                    if (ch.equals(new String("U"))) {
                        this.upload();
                    }

                    else if (ch.equals(new String("D"))) {
                        this.download();
                    }

                    else if (ch.equals(new String("H"))) {
                        this.history();
                    }

                    else if(ch.equals(new String("DF"))) {
                        this.delFile();
                    }
                    ch = din.readUTF();
                }


            } catch (IOException | SQLException | ClassNotFoundException e) {
                System.out.println("exception in UserHandler.run() : " + e.getMessage());
                e.printStackTrace();
                return;
            }

            try {
                din.close();
                dout.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}