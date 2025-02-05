package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;


public class NetworkCL {
    protected ServerSocket server;
    protected Socket clientHandler;
    protected PrintWriter out;
    protected BufferedReader in;
    protected LocalDateTime date = LocalDateTime.now();
    protected ArrayList<PostMessage> postMessages = new ArrayList<>();


    public void run() throws IOException {
        try {
            postMessages = HtmlIO.readFile();
            server = new ServerSocket(12345);
            clientHandler = server.accept();
            out = new PrintWriter(clientHandler.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientHandler.getInputStream()));
            String inputLine;
            out.println("Connection established to server " + server.getLocalPort());
            out.println("Write \"Stop\" to stop the server");
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                if (inputLine.equalsIgnoreCase("Stop")) {
                    break;
                }

                if (inputLine.startsWith("GET")) {
                    String promtInput = inputLine.replace("GET ", "").trim();
                    /*switch (promtInput) {
                        case "/hello" -> {
                            String message = "Hello, World!";
                            getResponse(message);
                        }
                        case "/index.html" -> {
                            String message = "!doctype html";
                            getResponse(message);
                        }
                    }*/
                    boolean exists = false;
                    for (PostMessage post : postMessages){
                        if (post.getPromtInput().equals(promtInput)) {
                            String message = post.getMessage();
                            getResponse(message);
                            exists = true;
                        }
                    }
                    if (!exists) {
                        System.out.println("Ingen message findes for" + promtInput);
                        out.println("\r\nHTTP/1.1 400 Bad Request");
                    }
                } else if (inputLine.startsWith("POST")) {
                    String promtInput = inputLine.split(" ")[1].trim();
                    String message = inputLine.replace("POST " + promtInput, "").trim();
                    for (PostMessage post : postMessages){
                        if (post.getPromtInput().equals(promtInput)) {
                            postMessages.remove(post);
                        }
                    }
                    postMessages.add(new PostMessage(promtInput, message));
                    getResponse(message);
                    HtmlIO.writeFile(postMessages);

                } else {
                    out.println("\r\nHTTP/1.1 400 Bad Request");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }


    public void stop() throws IOException {
        server.close();
        clientHandler.close();
        out.close();
        in.close();
    }

    private String dateToString(LocalDateTime date) {
        return "Date: " + date.getDayOfWeek() + ". " + date.getDayOfMonth() + " " + date.getMonth() + " " + date.getYear() + " " + date.getHour() + ":" + date.getMinute() + ":" + date.getSecond() + " " + ZoneId.systemDefault();
    }

    private void getResponse(String message) {
        out.println("\r\nHTTP/1.1 200 OK \r\n"
                + dateToString(date) + "\r\n"
                + "Server: FCK_FanShop \r\n"
                + "Content-Type: text/html; charset=UTF-8 \r\n"
                + "Content-Length: " + message.length() + "\r\n \r\n"
                + message);
    }

}
