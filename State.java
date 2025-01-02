import java.util.Arrays;

public class State {
    private char[][] board;
    private int cost;
    private String path;
    private int lastDirection;
    private int hValue;
    private int fValue;
    private int generationTime;

    public State(char[][] board, int cost, String path, int lastDirection) {
        this.board = board != null ? copyBoard(board) : null;
        this.cost = cost;
        this.path = path;
        this.lastDirection = lastDirection;
    }

    public char[][] copyBoard(char[][] board) {
        char[][] copyB = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            copyB[i] = Arrays.copyOf(board[i], board[0].length);
        }
        return copyB;
    }

    public void calculateHeuristic(Board boardHandler) {
        this.hValue = boardHandler.heuristic(this.board, boardHandler.getGoalBoard());
        this.fValue = this.cost + this.hValue;
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

    public int getHValue() {
        return hValue;
    }

    public int getFValue() {
        return fValue;
    }

    public int getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(int generationTime) {
        this.generationTime = generationTime;
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