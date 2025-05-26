
package main;

import com.formdev.flatlaf.FlatDarkLaf;
import core.utils.persistence.JsonFileLoader;
import core.views.AirportFrame;
import javax.swing.UIManager;

public class Main {

    public static void main(String args[]) {
        System.setProperty("flatlaf.useNativeLibrary", "false");

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        String locationsFilePath = "json/locations.json"; 
        String planesFilePath = "json/planes.json";       
        String passengersFilePath = "json/passengers.json"; 
        String flightsFilePath = "json/flights.json"; 
        
        JsonFileLoader.loadAll(
                locationsFilePath,  
                planesFilePath,     
                passengersFilePath, 
                flightsFilePath     
        );

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AirportFrame().setVisible(true);
            }
        });
    }
}
