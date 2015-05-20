package cs601.proxy;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import com.sun.corba.se.spi.orb.ParserImplTableBase;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ivan on 2014/9/7.
 */
public class ClientHandler implements Runnable
{
    Socket clientSocket;

    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket=clientSocket;
    }

    public void run()
    {
        try {

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in1 = null;
            DataOutputStream out1 = null;
            byte[] a = new byte[10000];
            byte[] b = new byte[10000];
            int readA = 0;
            int readB = 0;

            String line = in.readLine();
            System.out.println(line);

            if (line != null) {
                String cmd = line.substring(0, line.indexOf(" "));
                String url = line.substring(line.indexOf("http://")+7, line.indexOf("HTTP/") - 1);
                String uri = url.substring(0, url.indexOf("/"));
                String fileName = url.substring(url.indexOf("/"));
                String version = line.substring(line.indexOf("HTTP/"));
                String host = " HTTP/1.0" + "\r\n";

                if (cmd.equalsIgnoreCase("GET")) {
                    Map<String, String> map = new HashMap<String, String>();

                    String me[];
                    line = in.readLine().toLowerCase();
                    while (!line.equals("") && line != null) {

                        me = line.split(":");
                        String name = me[0];
                        String value = me[1];
                        if (!name.equals("connection") && !name.equals("user-agent") && !value.equals(" keep-alive")) {
                            map.put(name, value);
                        }

                        line = in.readLine().toLowerCase();
                    }

                    Socket socket = new Socket(uri, 80);
                    out1 = new DataOutputStream(socket.getOutputStream());
                    in1 = new DataInputStream(socket.getInputStream());
                    String outData = cmd + " " + fileName + host;
                    Iterator iterator = map.entrySet().iterator();


                    while (iterator.hasNext()) {
                        Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                        String headName = entry.getKey();
                        String headers = headName + ":" + entry.getValue() + "\r\n";
                        outData = outData + headers;
                    }

                    outData+="\r\n";

                    //System.out.println(outData);
                    byte[] aa = outData.getBytes();
                    out1.write(aa);

                    out1.flush();


                    while (readA != -1) {
                        try {
                            //if(readA==-1)break;
                            readA = in1.read(a, 0, 10000);
                            if (readA > 0) {
                                out.write(a, 0, readA);
                                out.flush();

                            }

                        } catch (Exception e) {
                            break;
                        }
                    }

                    out1.close();
                    in1.close();
                    socket.close();
                    in.close();
                    out.close();
                    clientSocket.close();
                }
                if (cmd.equalsIgnoreCase("POST"))
                {
                    Map<String, String> map = new HashMap<String, String>();
                    String me[];
                    line = in.readLine().toLowerCase();
                    while (!line.equals("") && line != null) {

                        me = line.split(":");
                        String name = me[0];
                        String value = me[1];
                        if (!name.equals("connection") && !name.equals("user-agent") && !value.equals(" keep-alive")) {
                            map.put(name, value);
                        }

                        line = in.readLine().toLowerCase();
                    }

                    Socket socket = new Socket(uri, 80);
                    out1 = new DataOutputStream(socket.getOutputStream());
                    in1 = new DataInputStream(socket.getInputStream());
                    String outData = cmd + " " + fileName + host;
                    Iterator iterator = map.entrySet().iterator();


                    while (iterator.hasNext()) {
                        Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                        String headName = entry.getKey();
                        String headers = headName + ":" + entry.getValue() + "\r\n";
                        outData = outData + headers;
                    }

                    outData+="\r\n";

                    //System.out.println(outData);
                    byte[] aa = outData.getBytes();
                    out1.write(aa);
                    out1.flush();

                    if(map.containsKey("content-length"))
                    {
                        readB=Integer.valueOf(map.get("content-length").substring(1));
                        byte bb[]=new byte[readB];
                        in.read(bb,0,readB);
                        out1.write(bb);
                    }


                    while (readA != -1) {
                        try {
                            //if(readA==-1)break;
                            readA = in1.read(a, 0, 10000);
                            if (readA > 0) {
                                out.write(a, 0, readA);
                                out.flush();

                            }

                        } catch (Exception e) {
                            break;
                        }
                    }

                    out1.close();
                    in1.close();
                    socket.close();
                    in.close();
                    out.close();
                    clientSocket.close();
                }


            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
