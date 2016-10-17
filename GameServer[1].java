package chatGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author java
 */
public class GameServer implements AutoCloseable {

    public static final int GAME_PORT = 34567;
    public static final int FIELD_SIZE = 100;
    private int[] field;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    //компилятор должен всегда проверять что значение свежее
    private volatile boolean active;

    public GameServer() throws IOException {
        serverSocket = new ServerSocket(GAME_PORT);
        field = new int[FIELD_SIZE];
        for (int i = 0; i < field.length; i++) {
            field[i] = (int) Math.round(Math.random());
            System.out.print(field[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try (GameServer server = new GameServer()) {
            server.run();
        } catch (IOException e) {
            System.out.println("Error #1:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error #2:" + e.getMessage());
        }

    }

    private void run() throws IOException, ClassNotFoundException {
        Turn turn;
        active = true;
////        while (active) {
//        }
        socket = serverSocket.accept();
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        //ход
        while (active) {
            turn = (Turn) in.readObject();
            System.out.println("Input turn: " + turn);
            //num номер хода
            int num = turn.getNumber() + 1;
           // int x = turn.getNumber();
            if (num < 0) {
                out.writeObject(turn);
                return;
            }
            //новый ход сгенерирован рандомно
            int t = (int) Math.round(Math.random() * 99);
            //проверка хода и результат
            turn = new Turn(num, t, field[turn.getTurn()] == 1 ? Turn.Result.DEAD : Turn.Result.EMPTY);
            out.writeObject(turn);
        }
    }

    void setActive() {
        active = !active;
    }

    @Override
    public void close() throws Exception {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
