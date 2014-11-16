/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphingcalculator;

import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Octalus
 */
public class Main {
    static PrintWriter writer;
    public static void main(String[] args) throws IOException {
        writer = new PrintWriter("C:\\Users\\Octalus\\Desktop\\Log.txt", "UTF-8");
        writer.write("Started");
        MainUI mainUI = new MainUI();
        mainUI.setVisible(true);
        
    }
}
