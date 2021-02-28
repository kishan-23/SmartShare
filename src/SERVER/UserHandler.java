//This is the Threaded User Handler Class
package SERVER;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class UserHandler implements Runnable {
    Socket s;
    Connection con;
    DataInputStream din;
    DataOutputStream dout;
    String user_id;
    int user_no;
    String user_name;
    String mail;

    public UserHandler (Socket s, Connection con) throws IOException, SQLException {
        this.con = con;
        this.s = s;
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());

    }

    public void upload() throws IOException, SQLException {
        String name, path, comment, query;
        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy#HH:mm:ss");
        String key = ""+user_no+"@"+formatter.format(dt);
        java.sql.Date sqldate = new java.sql.Date(dt.getTime());
        Time sqlTime = new Time(dt.getTime());
        LocalDateTime now = LocalDateTime.now();
        name = din.readUTF();
        int i=0;
        path = "C:\\Users\\Kishan Verma\\MyServer\\@"+i+name+"\\";
        File f = new File(path);
        while(f.exists())
        {
            i++;
            path = "C:\\Users\\Kishan Verma\\MyServer\\@"+i+name+"\\";
            f = new File(path);
        }
        FileOutputStream fout = new FileOutputStream(f);
        int max_download = din.readInt();
        long time = din.readLong();
        comment = din.readUTF();
        byte[] buffer = new byte[10240];
        int length;
        while ((length = din.read(buffer)) > 0) {
            fout.write(buffer, 0, length);
            //System.out.println(""+length);
            if(length<10240) break;
        }
        fout.close();
        System.out.println("i'm here out of loop");

        query = "insert into fileinfo (fileName, filePath, uploadedOn, downloadsLeft, uploadedBy, availableUpto, fileKey, comments) "+"values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        ps = con.prepareStatement(query);
        ps.setString(1,name);
        ps.setString(2,path);
        ps.setDate(3,sqldate);
        ps.setTime(4,sqlTime);
        ps.setInt(5,max_download);
        ps.setInt(6,user_no);
        ps.setLong(7,time);
        ps.setString(8,key);
        ps.setString(9,comment);
        ps.execute();
        System.out.println("File added");
        dout.writeUTF(key);
    }

    public void download () throws IOException, SQLException {
        String key = din.readUTF();
        String name;
        java.util.Date jDate;
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
            timL = res1.getLong(5);
            name = res1.getString(6);
            long diff = (new Date().getTime()-jDate.getTime())/60000;
            if(diff>timL)
            {
                dout.writeUTF("Time Limit for the download is exceeded.");
                dout.flush();
            }
            else{
                maxD--;
                if(maxD<0)
                {
                    dout.writeUTF("Maximum number of downloads for this file is achieved.");
                    dout.flush();
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
        String qu = "select fileName, uploadedOn, downloadsLeft, fileKey, availableUpto, comments from fileinfo where "+"uploadedBy = "+"(?)";
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
            java.sql.Date dt = rs.getDate(2);
            Time tm = rs.getTime(3);
            dout.writeUTF(dt.toString());
            dout.flush();
            dout.writeUTF(tm.toString());
            dout.flush();
            dout.writeInt(rs.getInt(4));
            dout.flush();
            dout.writeUTF(rs.getString(5));
            dout.flush();
            dout.writeLong(rs.getLong(6));
            dout.flush();
            dout.writeUTF(rs.getString(7));
            dout.flush();
        }
        dout.writeBoolean(false);
        dout.flush();
        int delch = din.readInt();
        if(delch==1) this.delFile();
    }

    public void delFile() throws IOException, SQLException {
        String key = din.readUTF();
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
                }
                PreparedStatement ps2 = con.prepareStatement(qu2);
                ps2.setString(1,key);
                ps2.executeUpdate();
                dout.writeUTF("Successfully deleted.");
                dout.flush();
            }
            else {
                dout.writeUTF("This file doesn't belong to you.");
                dout.flush();
            }
        }
        else{
            dout.writeUTF("no such file was found");
            dout.flush();
        }
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

    @Override
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

                    ch = din.readUTF();
                }


            } catch (IOException | SQLException e) {
                System.out.println("exception 2: " + e.getMessage());
                e.printStackTrace();
                ;
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