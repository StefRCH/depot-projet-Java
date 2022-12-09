package org.example;

import java.util.Scanner;

public class Input {

    private static Input INSTANCE = null;


    public static Input getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Input();
        }
        return INSTANCE;
    }


    private Scanner scanner = new Scanner(System.in);


    private Input() {}


    public synchronized String getNextLine() {
        return scanner.nextLine();
    }
}
