package chatGame;

import java.io.Serializable;

/**
 *
 * @author java
 */
public class Turn implements Serializable {

    private int number;
    private int turn;
    private Result result;

    public static enum Result {
        EMPTY, HIT, DEAD;
    }

    public Turn(int number, int turn, Result result) {
        this.number = number;
        this.turn = turn;
        this.result = result;
    }

    public int getNumber() {

        return number;
    }

    public int getTurn() {

        return turn;
    }

    public Result getResult() {

        return result;
    }

    @Override
    public String toString() {
        return number + ": " + turn + " - " +(result!=null ? result.name(): "null");
    }
}
