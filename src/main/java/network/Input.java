package network;

import java.util.Scanner;

/**
 * Cette classe nous sert à avoir des scanners bien indépendants les uns des autres afin qu'ils ne se "s'entremêlent" pas
 */
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

    public synchronized void close(){
        scanner.close();
    }
}
