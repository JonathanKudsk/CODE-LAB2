package org.example;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        NetworkCL networkCL = new NetworkCL();

        try {
            networkCL.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}