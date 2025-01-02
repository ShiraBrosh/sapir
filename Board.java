import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private static final int SIZE = 3;
    private static final int CostRED = 10;
    private static final int CostBLUE = 1;
    private static final int CostGREEN = 3;

    private char[][] goalBoard;

    public Board(char[][] goalBoard) {
        this.goalBoard = goalBoard;
    }

    public char[][] copyBoard(char[][] board) {
        char[][] copyB = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            copyB[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return copyB;
    }

    public int getMoveCost(char value) {
        switch (value) {
            case 'B': return CostBLUE;
            case 'R': return CostRED;
            case 'G': return CostGREEN;
            default: return 0;
        }
    }

    public List<State> getNextStates(State curr) {
        List<State> nextStates = new ArrayList<>();
        char[][] board = curr.getBoard();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != '_' && board[i][j] != 'X') {
                    movePoint(nextStates, board, i, j, curr);
                }
            }
        }
        return nextStates;
    }

    private void movePoint(List<State> states, char[][] board, int i, int j, State curr) {
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // למעלה, ימינה, למטה, שמאלה

        for (int[] direction : directions) {
            int newI = (i + direction[0] + SIZE) % SIZE;
            int newJ = (j + direction[1] + SIZE) % SIZE;

            if (board[newI][newJ] == '_') {
                char[][] newBoard = copyBoard(board);
                newBoard[newI][newJ] = board[i][j];
                newBoard[i][j] = '_';

                int costOfMove = getMoveCost(board[i][j]);
                String newPath = curr.getPath() +
                        String.format("(%d,%d):%c:(%d,%d)--",
                                i + 1, j + 1, board[i][j], newI + 1, newJ + 1);

                State newState = new State(newBoard, curr.getCost() + costOfMove, newPath, -1);
                states.add(newState);
            }
        }
    }

    public boolean isGoalState(State curr) {
        char[][] currentBoard = curr.getBoard();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (currentBoard[i][j] != goalBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getGoalBoard() {
        return copyBoard(goalBoard);
    }
}

