package org.example;

import java.io.*;
import java.util.ArrayList;

public class HtmlIO {


    protected static String message1inArray;

    public static ArrayList<PostMessage> readFile() {
        String filePath = "Data/index.htlm.html";
        File file = new File(filePath);
        ArrayList<PostMessage> postMessages = new ArrayList<>();
        if (!file.exists()) {
            System.out.println("Brugerdatafil ikke fundet: " + filePath);
            return null;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("</div>")) {
                    line = line.replace("<div id=", "");
                    line = line.replace("</div>", "");
                    line = line.replace("\"", "");
                    String[] data = line.split(">");
                    String promtInput = data[0].replace(">", "");
                    String message = data[1];
                    postMessages.add(new PostMessage(promtInput, message));
                }
            }
            return postMessages;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(ArrayList<PostMessage> postMessages) {
        String filePath = "Data/index.htlm.html";

        File file = new File(filePath);
        boolean insideBody = false;
        if (!file.exists()) {
            System.out.println("Brugerdatafil ikke fundet: " + filePath);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line2;
            ArrayList<String> lines = new ArrayList<>();
            while ((line2 = br.readLine()) != null) {
                lines.add(line2);
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            boolean first = true;
            for (String line : lines) {
                if (line.contains("<body>")) {
                    insideBody = true;
                    bw.write(line + "\n");
                }
                if (line.contains("</body>")) {
                    insideBody = false;
                }
                if (!insideBody) {
                    bw.write(line + "\n");
                } else {
                    if (first) {
                        for (PostMessage postMessage : postMessages) {
                            bw.write(postMessage.toHtmlString() + "\n");
                        }
                        first = false;
                    }
                }
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
