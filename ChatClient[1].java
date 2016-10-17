package chatGame;

import java.awt.Container;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFrame;

/**
 *
 * @author java
 */
public class ChatClient extends JFrame {
//working processes

    public static final int BUFFER_SIZE = 255;
    public static final int LISTENER = 23456;
    private static final String BROADCAST_ADDRESS = "192.168.19.255";
    private static final String HELLO_MESSAGE = "Hello";
    private static final String BYE_MESSAGE = "Bye, bye";
    private DatagramPacket packet;
    private DatagramSocket sender;
    private ChatReader receiver;
    private Thread thread;
//interface
    private JButton send;
    private JButton clear;
    private Container cp;
    private JTextField host;
    private JTextField port;
    private JTextArea log;
    private JTextField message;
    private ChatListener cl;
    private GameClient game;
    private byte[] buffer;
    private boolean active;

    public ChatClient() throws SocketException, IOException {
        setTitle("Chat Client, v.1.0");
        setSize(640, 480);
        setLocation(150, 80);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        cl = new ChatListener(this);
        addWindowListener(cl);
        cp = getContentPane();
        cp.setLayout(null);
        initGUI();
        initGame();
        setVisible(true);

    }
//in init() just interface settings

    private void initGUI() {
        JLabel h = new JLabel("Host: ");
        h.setBounds(10, 8, 48, 28);
        cp.add(h);
        host = new JTextField();
        host.setBounds(42, 8, 560, 28);
        cp.add(host);
        JLabel p = new JLabel("Port: ");
        p.setBounds(10, 40, 48, 28);
        cp.add(p);
        port = new JTextField();
        port.setBounds(42, 40, 48, 28);
        cp.add(port);
        log = new JTextArea();
        log.setEditable(false);
        JScrollPane sp = new JScrollPane(log);
        sp.setBounds(10, 76, 580, 320);
        cp.add(sp);
        message = new JTextField();
        message.setBounds(10, 398, 280, 28);
        cp.add(message);
        send = new JButton("Send");
        send.setBounds(426, 398, 80, 28);
        send.addActionListener(cl);
        cp.add(send);
        clear = new JButton("Clear");
        clear.setBounds(514, 398, 80, 28);
        clear.addActionListener(cl);
        cp.add(clear);
    }

    private void initGame() throws IOException {
        game = new GameClient();
    }

    public static void main(String[] args) {
        try {
            ChatClient c = new ChatClient();
            c.run();
            c.close();
        } catch (SocketException ex) {
            System.out.println("Error #1: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error #2: " + ex.getMessage());

        }
    }
// run() - responsible  for interaction of the program with system/clients
//основной циклб запускает прослушивание портаб вызывает блокирующие метод ресиве и блокирует порт

    private void run() throws SocketException {
        sender = new DatagramSocket();
        buffer = new byte[BUFFER_SIZE];
        packet = new DatagramPacket(buffer, buffer.length);
        log.append("Start...");
        sendMessage(BROADCAST_ADDRESS, HELLO_MESSAGE);
        receiver = new ChatReader(this);
        thread = new Thread(receiver, "Reader");
        thread.start();
        try {
            thread.join(1000000);
        } catch (InterruptedException ex) {
            log.append("Error #12: " + ex.getMessage());
        }
    }

    void sendMessage() {
        String h = host.getText().trim();
        //преобразуем имя взятой строки в адрес для получения адреса получателя
        if (h.isEmpty()) {
            log.append("\nError: Empty address");
            return;
        }
        sendMessage(h, message.getText());
    }

    private void sendMessage(String host, String msg) {
        try {
            InetAddress a = InetAddress.getByName(host);
            packet.setAddress(a);
            packet.setPort(LISTENER);
            //сериализация из байтов в строку
            byte[] b = msg.trim().getBytes();
            packet.setLength(b.length);
            packet.setData(b);
            sender.send(packet);
            packet.setData(buffer);
        } catch (UnknownHostException ex) {
            //восстанавливаем в пакете исходный код
        } catch (IOException ex) {
            log.append("Error: " + ex.getMessage());
        }

    }

    void clearLog() {
        log.setText("");
    }

    void setTurn() throws IOException, ClassNotFoundException {
        game.run();
    }

    void setTurn(Turn turn) throws IOException, ClassNotFoundException {
        game.run();
    }

    void addLogMessage(String message) {
        log.append(message);
    }

    //allows us to close sockets
    public void close() {
        sendMessage(BROADCAST_ADDRESS, BYE_MESSAGE);

        if (!sender.isClosed());
        {
            sender.close();
        }
        if (thread.isAlive()) {
            thread.interrupt();
        }
        System.exit(0);
    }

}
