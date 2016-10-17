/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatGame;

import static chatGame.ChatClient.BUFFER_SIZE;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author java
 */
public class ChatReader implements Runnable{

    private ChatClient client;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buffer;
    private boolean active;
    
    
    public ChatReader (ChatClient client){
    this.client =client;
    active = true;
    }
    
    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(ChatClient.LISTENER)){
            
            buffer = new byte[ChatClient.BUFFER_SIZE];
            packet = new DatagramPacket(buffer, buffer.length);
            
            StringBuilder b = new StringBuilder();
            active = true;
            String msg;
           
            ////
            //multi- loop, works as listner of a port- receiver
           while (active) {
            try {
               socket.receive(packet);
                socket.receive(packet);
            } catch (IOException e) {
               client.addLogMessage("Error: " + e.getMessage());
                break;
            }
            //getting an IP addres of a client
            b.append(packet.getAddress()).append("\n\t");
            //ceate new string and get information starting from  as much as there
            //десериализация из строки в байты. пример встроенной сериалитизации
            msg = new String(packet.getData(), 0, packet.getLength());
            
            //adding the string to the builder
            b.append(msg).append("\n");
            //выводит сообщение в протокол(можно было и без этого выводить напрямкю)
          client.addLogMessage(b.toString());
            //clean builder after getting packet
            b.setLength(0);
            
            }
  
        } catch (SocketException ex) {
           client.addLogMessage("Error in ChatReader: "+ ex.getMessage());
        }
    }

    }
    

