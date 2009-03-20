///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.wendal.java.xrace.toolkit.bmpconvert;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Rec {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException,
            InterruptedException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            Socket socket = serverSocket.accept();
            Thread.sleep(1000);
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            Object object = ois.readObject();
            if (object instanceof BmpMessage) {
                BmpMessage message = (BmpMessage) object;
                FileOutputStream fis = new FileOutputStream(message
                        .getFilename()
                        + ".rbg");
                fis.write(message.getData());
                fis.flush();
                fis.close();
                System.out
                        .println("Finiah : " + message.getFilename() + ".rbg");
            }
            if (object instanceof BmpSizeMessage) {
                BmpSizeMessage message = (BmpSizeMessage) object;
                FileOutputStream fis = new FileOutputStream(message.getName()
                        + ".size");
                DataOutputStream dos = new DataOutputStream(fis);
                int[] size = message.getSize();
                dos.writeInt(size[0]);
                dos.writeInt(size[1]);
                dos.flush();
                dos.close();
                // fis.write(message.getSize());
                // fis.flush();
                // fis.close();
                System.out.println("Finiah : " + message.getName() + ".size");
            }
            socket.close();
        }
    }

}
