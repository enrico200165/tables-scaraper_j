package com.enrico200165.cookies;
/**
 * COPIATO DA https://www.html.it/pag/393703/java-cookie-implementare-un-semplice-cookiestore/
 */

import java.io.*;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.*;

public class CookieStoreEV implements CookieStore, Runnable  {

        private HashMap<URI, String> cookies;
        private final static String storePath = ".\\cookies.ser";
        @SuppressWarnings("unchecked")
        public CookieStoreEV() throws FileNotFoundException, IOException, ClassNotFoundException {
            File file = new File(storePath);
            if(!file.exists())
                file.createNewFile();
            if(file.length()>0){
                try (FileInputStream fileIn = new FileInputStream(storePath);
                     ObjectInputStream in = new ObjectInputStream(fileIn);) {
                    cookies = (HashMap<URI, String>) in.readObject();
                    for (Map.Entry<URI, String> entry : cookies.entrySet()) {
                        List<HttpCookie> cookieList = HttpCookie.parse(entry.getValue());
                        cookies.put(entry.getKey(),cookieList.get(0).toString());
                    }
                }
            } else {
                cookies = new HashMap<URI,String>();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(this));
        }
        @Override
        public void add(URI uri, HttpCookie cookie) {
            cookies.put(uri, cookie.toString());
        }

        @Override
        public void run() {
            try {
                clearFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (FileOutputStream fileOut = new FileOutputStream(storePath);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut);) {
                out.writeObject(cookies);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        private void clearFile() throws IOException{
            File file = new File(storePath);
            file.delete();
            file.createNewFile();
        }

        @Override
        public List<HttpCookie> get(URI uri) {
            ArrayList<HttpCookie> cookieList = new ArrayList<HttpCookie>();
            if(cookies.containsKey(uri)) {
                HttpCookie ck = HttpCookie.parse(cookies.get(uri)).get(0);
                if(!ck.hasExpired())
                    cookieList.add(ck);
                for (String v : cookies.values()) {
                    HttpCookie c = HttpCookie.parse(v).get(0);
                    String domain = c.getDomain();
                    if (domain!=null && uri.toString().contains(domain)) {
                        if (!c.hasExpired())
                            cookieList.add(c);
                    }
                }
            }
            return Collections.unmodifiableList(cookieList);
        }
        @Override
        public List<HttpCookie> getCookies() {
            ArrayList<HttpCookie> cookiesList = new ArrayList<HttpCookie>();
            for (String c : cookies.values()) {
                HttpCookie c1 = HttpCookie.parse(c).get(0);
                if (!c1.hasExpired())
                    cookiesList.add(c1);
            }
            return Collections.unmodifiableList(cookiesList);
        }

        @Override
        public List<URI> getURIs() {
            ArrayList<URI> uriList = new ArrayList<URI>();
            for (URI uri : cookies.keySet())
                uriList.add(uri);
            return Collections.unmodifiableList(uriList);
        }

        @Override
        public boolean remove(URI uri, HttpCookie cookie) {
            return cookies.remove(uri, cookie);
        }
        @Override
        public boolean removeAll() {
            cookies.clear();
            return true;
        }

        public String dump() {
            String ret = "da implementare";
            return ret;
        }

        public int size() {
            return cookies.size();
        }
}