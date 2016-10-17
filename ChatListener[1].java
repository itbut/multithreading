/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kate
 */
//обработчик событий от окна и от кнопок
public class ChatListener extends WindowAdapter implements ActionListener {

    private ChatClient client;
    
    public ChatListener(ChatClient client) {
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Send":
                System.out.println("Send");
                client.sendMessage();
                System.out.println("Packet send");
                break;
            case "Clear":
                client.clearLog();
                try {
                    client.setTurn();
                } catch (IOException ex) {
                    System.err.println("Error #3: " + ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    System.err.println("Error #4: " + ex.getMessage());
                }
        }
        //System.out.println("Command: " + e.getActionCommand());
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            client.setTurn(new Turn (-1, -1, null));
            client.close();
        } catch (IOException ex) {
             System.out.println("Error #5: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
             System.out.println("Error #6: " + ex.getMessage());
        }
    }
}
