package cs601.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ivan on 2014/9/7.
 */
public class ProxyServer
{
    public static void main(String args[])throws IOException
    {
        try{
            ServerSocket serverSocket=new ServerSocket(8080);
            while(true)
            {
                Socket channel=serverSocket.accept();
                ClientHandler clientHandler=new ClientHandler(channel);
                Thread thread=new Thread(clientHandler);
                thread.start();
                thread.join();

            }
        }catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
