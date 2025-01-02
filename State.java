import java.util.Arrays;

// State.java
public class State {
    private char[][] board;
    private int cost;
    private String path;
    private int lastDirection;

    public State(char[][] board, int cost, String path, int lastDirection) {
        this.board = board != null ? copyBoard(board) : null;
        this.cost = cost;
        this.path = path;
        this.lastDirection = lastDirection;
    }

    public char[][] copyBoard(char[][] board) {
        char[][] copyB = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            copyB[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return copyB;
    }

    public char[][] getBoard() {
        return board;
    }

    public int getCost() {
        return cost;
    }

    public String getPath() {
        return path;
    }

    public int getLastDirection() {
        return lastDirection;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        State other = (State) obj;
        return Arrays.deepEquals(this.board, other.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }
}