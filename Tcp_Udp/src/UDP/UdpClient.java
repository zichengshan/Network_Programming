package UDP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
public class UdpClient {
    public static void main(String[] args) throws Exception{
        // waiting for UDP packets on the specified port
        DatagramSocket socket = new DatagramSocket(8888);
        DatagramPacket packet = null;
        int package_received = 0;
        
        // send the packet
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        byte[] data = reader.readLine().getBytes();
        packet = new DatagramPacket(data,0,data.length, new InetSocketAddress("localhost",6666));
        socket.send(packet);
        System.out.println("package sent successfully.");

        while (true) {
            // receive the packet
            byte[] buffer = new byte[1024];
            packet = new DatagramPacket(buffer,buffer.length);
            socket.receive(packet);
            package_received++;
            String str = new String(packet.getData(),0,packet.getLength());
            if (str.equals("Completed")){
                break;
            }
            System.out.println(str);
        }
        System.out.println("-----------------------\n Number of packages received: " + package_received);
        socket.close();
    }
}
