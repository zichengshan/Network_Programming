package UDP;

import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpServer {
    public static void main(String[] args) throws Exception{
        System.out.println("Waiting for signal....");
        // waiting for UDP packets on the specified port
        DatagramSocket socket = new DatagramSocket(6666);
        int package_count = 0;

        while (true) {

            // receive the package
            byte[] container = new byte[1024];
            DatagramPacket packet = new DatagramPacket(container,0,container.length);
            socket.receive(packet);
            String str = new String(packet.getData(),0,packet.getLength());

            // send the package
            if (str.equals("index")){
                System.out.println("keyword received.");
                File folder = new File("file");
                if (folder.isDirectory()){
                    // send a package back
                    packet.setData("ok".getBytes());
                    socket.send(packet);
                    package_count ++;
                    File[] fs = folder.listFiles();
                    for(File file : fs){
                        packet.setData(("The content of " + file + ":").getBytes());
                        socket.send(packet);
                        package_count++;
                        FileInputStream f = new FileInputStream(file);
                        byte[] buffer = new byte[1024];
                        int len;
                        while((len = f.read(buffer)) != -1){
                            packet = new DatagramPacket(buffer,0, len, new InetSocketAddress("localhost",8888));
                            socket.send(packet);
                            package_count++;
                        }
                    }
                }
                // send a finish package back
                packet.setData("Completed".getBytes());
                socket.send(packet);
                package_count++;
                System.out.println("The content of files is sent.");
                break;
            }else if(str.contains("get")){
                String[] files = str.split(" ");
                for (int i = 1; i <files.length; i++){
                    File f = new File(files[i]);
                    if(f.exists()){
                        // send a package back
                        packet.setData("ok, we have this file\n --------------------".getBytes());
                        socket.send(packet);
                        package_count++;
                        FileInputStream fi = new FileInputStream(f);
                        byte[] buffer = new byte[1024];
                        int len;
                        while((len = fi.read(buffer)) != -1){
                            packet = new DatagramPacket(buffer,0, len, new InetSocketAddress("localhost",8888));
                            socket.send(packet);
                            package_count++;
                        }
                    }else{
                        packet.setData("error".getBytes());
                        socket.send(packet);
                        package_count++;
                    }
                }
                // send a finish package back
                packet.setData("Completed".getBytes());
                socket.send(packet);
                package_count++;
                System.out.println("The content of files is sent.");
                break;
            }else{
                packet.setData("error".getBytes());
                socket.send(packet);
                package_count++;
                packet.setData("Completed".getBytes());
                socket.send(packet);
                package_count++;
                break;
            }
        }
        System.out.println("----------------\n Number of packages sent: " + package_count);
    }
}
