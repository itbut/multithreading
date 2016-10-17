package chatGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author java
 */
public class GameClient implements AutoCloseable {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int[] field;
    private volatile boolean active;

    public GameClient() throws IOException {
        socket = new Socket("localhost", GameServer.GAME_PORT);
        //при создании потока сначала создается входной поток потом выходной, но открывать нужно по очереди
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        field = new int[GameServer.FIELD_SIZE];
        active = true;
        for (int i = 0; i < field.length; i++) {
            field[i] = (int) Math.round(Math.random());
        }

    }

    public void run() throws IOException, ClassNotFoundException {
        Turn turn = new Turn(1, (int) Math.round(Math.random() * 99), null);
        run(turn);
    }

    private void run(Turn turn) throws IOException, ClassNotFoundException {
        out.writeObject(turn);
        System.out.println("Turn sent");
        turn = (Turn) in.readObject();
        System.out.println("Reply: " + turn);

    }
    
    

    @Override
    public void close() throws Exception {
        if (socket != null && socket.isClosed()) {
            socket.close();
        }
    }

}
