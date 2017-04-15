package com.example.jersey;

/**
 * Created by enrico on 12/02/2016.
 */
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

public class SSLContextKeyExample {
    public static void main(String args[]) throws Exception {
        SSLContext context;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] storepass = "newpass".toCharArray();
        char[] keypass = "wshr.ut".toCharArray();
        String storename = "newstore";

        context = SSLContext.getInstance("TLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        FileInputStream fin = new FileInputStream(storename);
        ks = KeyStore.getInstance("JKS");
        ks.load(fin, storepass);

        kmf.init(ks, keypass);
        context.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory ssf = context.getServerSocketFactory();

        ServerSocket ss = ssf.createServerSocket(5432);
        while (true) {
            Socket s = ss.accept();
            PrintStream out = new PrintStream(s.getOutputStream());
            out.println("Hi");
            out.close();
            s.close();
        }

    }
}