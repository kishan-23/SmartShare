package ClientBackend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserOptions {
    Socket socket;
    DataInputStream din;
    DataOutputStream dout;

    public UserOptions(Socket s) throws IOException {
        this.socket = s;
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public String upload(File file, int maxd, LocalDate date, String com) throws IOException {
        dout.writeUTF("U");
        dout.flush();
        String key;
        dout.writeUTF(file.getName());
        dout.flush();
        dout.writeInt(maxd);
        dout.flush();
        dout.writeUTF(com);
        dout.flush();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dt = date.format(formatter);
        dout.writeUTF(dt);
        dout.flush();
        System.out.println(dt);
        try {
            FileInputStream fin = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            int length;
            while ((length = fin.read(buffer)) > 0) {
                dout.write(buffer, 0, length);
            }
            dout.flush();
            fin.close();
        }
        catch (Exception e)
        {
            System.out.println("exception in file input stream: "+e.getMessage());
        }
        System.out.println("out");
        key = din.readUTF();
        return key;
    }

    public String download(String key, String path) throws IOException {
        dout.writeUTF("D");
        dout.flush();
        dout.writeUTF(key);
        dout.flush();
        String msg= din.readUTF();
        if(!msg.equals(new String("OK"))){
            return msg;
        }
        String name = din.readUTF();
        int i=0;
        String pt=path+"/"+i+name;
        File file = new File(pt);
        while (file.exists()) {
            pt=path+"/"+i+name;
            file = new File(pt);
            i++;
        }
        try{
            FileOutputStream fout = new FileOutputStream(file);
            byte[] buffer=new byte[1024];
            int length;
            while ((length = din.read(buffer)) > 0) {
                fout.write(buffer, 0, length);
                if(length<1024) break;
            }
            fout.close();
        }
        catch (Exception e) {
            System.out.println("Exception in UserOptions.download()"+e.getMessage());
        }

        return "Downloaded Successfully";
    }

    public ObservableList<FileModal> history() throws IOException {
        dout.writeUTF("H");
        dout.flush();
        ObservableList<FileModal> fileList = FXCollections.observableArrayList();
        while(din.readBoolean())
        {
            String name = din.readUTF();
            String key = din.readUTF();
            int downLeft = din.readInt();
            String avlUpto = din.readUTF();
            Integer i = Integer.valueOf(downLeft);
            fileList.add(new FileModal(name,key,downLeft,avlUpto));
        }
        return fileList;
    }

    public String delFile(String key) throws IOException {
        dout.writeUTF("DF");
        dout.flush();
        dout.writeUTF(key);
        dout.flush();
        String msg=din.readUTF();
        return msg;
    }
}
