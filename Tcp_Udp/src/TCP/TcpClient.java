package TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) throws Exception {
        // 1. Get the address of server and its port
        InetAddress serverIP = InetAddress.getByName("127.0.0.1");
        int port = 9999;

        // 2. build a socket connection
        Socket socket = new Socket(serverIP,port);
        System.out.println("Client is working...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String info = reader.readLine();

        // 3. create a OutputStream
        OutputStream os = socket.getOutputStream();
        os.write(info.getBytes());

        // 4. tell the service: transfer completed
        socket.shutdownOutput();

        // 5. create a InputStream to receive the data feedback from server
        InputStream is = socket.getInputStream();

        // 6. output the messages received from server
        ByteArrayOutputStream baso = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer)) != -1){
            baso.write(buffer,0,len);
        }
        System.out.println(baso.toString());

        // 7. close the resources
        os.close();
        is.close();
        baso.close();
        socket.close();

    }
}
