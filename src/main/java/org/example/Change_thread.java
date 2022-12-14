package org.example;

public class Change_thread extends Thread {
    public Changing_Pseudo pseudochangeframe;

    public Change_thread(Changing_Pseudo changeframe){
        this.pseudochangeframe=changeframe;
        changeframe.setVisible(true);
    }

    public void run(){
    }

}

