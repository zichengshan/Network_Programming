package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args) throws Exception {

        // 1. build the server. give it a port
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("Server is working...");
        // 2. waiting for client connection
        Socket socket = serverSocket.accept();
        // 3. get the input stream and read the client message
        InputStream is = socket.getInputStream();

        // 4. get the message from client
        ByteArrayOutputStream baso = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer)) != -1){
            baso.write(buffer,0,len);
        }
        // 5. stop receiving message from client
        socket.shutdownInput();

        // 6. create OutputStream to send messages to client (multi-thread)
        OutputStream os = socket.getOutputStream();
        if (baso.toString().compareTo("index") == 0) {
            System.out.println("Received the keyword!");
            File f = new File("file");
            if (f.isDirectory()) {
                os.write("ok\n".getBytes());
                File[] fs = f.listFiles();
                for (File file : fs) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                read_file(file,os);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }else{
                System.out.println("error");
            }
        }else if (baso.toString().contains("get")){
            String[] files = baso.toString().split(" ");
            for (int i = 1; i< files.length; i++){
                File f = new File(files[i]);
                if (f.exists()) {
                    os.write("ok\n".getBytes());
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                read_file(f,os);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    os.write("error\n".getBytes());
                }
            }
        }else{
            os.write("error\n".getBytes());
        }

        Thread.sleep(100);
        // 7. close the resources
        baso.close();
        is.close();
        os.close();
        socket.close();
        serverSocket.close();
    }

    // read_file is used to read the file and send the messages to client
    synchronized public static void read_file(File file, OutputStream os) throws Exception {
        os.write(("The info in " + file + ":\n").getBytes());
        FileInputStream file_info = new FileInputStream(file);
        byte[] buffer1 = new byte[1024];
        int len1;
        while((len1 = file_info.read(buffer1)) != -1){
            os.write(buffer1,0,len1);
        }
        os.write("\n".getBytes());
        file_info.close();
    }
}