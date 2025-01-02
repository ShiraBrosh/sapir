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
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        for (int[] direction : directions) {
            int newI = (i + direction[0] + SIZE) % SIZE;
            int newJ = (j + direction[1] + SIZE) % SIZE;

            if (isOppositeMove(curr.getLastMove(), newI, newJ, i, j)) {
                continue; // מניעת מהלך הפוך
            }

            if (board[newI][newJ] == '_') {
                char[][] newBoard = copyBoard(board);
                newBoard[newI][newJ] = board[i][j];
                newBoard[i][j] = '_';
                int costOfMove = getMoveCost(board[i][j]);
                String newPath = curr.getPath() +
                        String.format("(%d,%d):%c:(%d,%d)--",
                                i + 1, j + 1, board[i][j], newI + 1, newJ + 1);

                State newState = new State(newBoard, curr.getCost() + costOfMove, newPath, new int[]{i, j, newI, newJ});
                states.add(newState);
            }
        }
    }

    private boolean isOppositeMove(int[] lastMove, int fromI, int fromJ, int toI, int toJ) {
        if (lastMove == null) return false;
        return lastMove[0] == toI && lastMove[1] == toJ && lastMove[2] == fromI && lastMove[3] == fromJ;
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
    public int heuristic(char[][] currentBoard, char[][] goalBoard) {
        int totalPenalty = 0; // עונש על אריחים שלא במקום
        int totalManhattanDistance = 0; // סכום מרחקי מנהטן

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char currentCell = currentBoard[i][j];

                // אם התא ריק או שחור, דלג עליו
                if (currentCell == '_' || currentCell == 'X') continue;

                // מציאת מיקום האריח בלוח היעד
                int[] goalPosition = findGoalPosition(currentCell, goalBoard);

                // אם האריח לא קיים בלוח היעד, דלג עליו (שגיאה לוגית אפשרית)
                if (goalPosition[0] == -1 || goalPosition[1] == -1) continue;

                // חישוב מרחק מנהטן
                int manhattanDistance = Math.abs(i - goalPosition[0]) + Math.abs(j - goalPosition[1]);
                totalManhattanDistance += manhattanDistance;

                // הוספת העונש על אריח שאינו במקום
                if (currentCell != goalBoard[i][j]) {
                    totalPenalty += getPenalty(currentCell);
                }
            }
        }

        // החזרת הסכום הכולל של מרחקי מנהטן ועונשים
        return totalPenalty + totalManhattanDistance;
    }
//    public int heuristic(char[][] currentBoard, char[][] goalBoard) {
//        int totalDistance = 0;
//
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZE; j++) {
//                char currentCell = currentBoard[i][j];
//
//                if (currentCell == '_' || currentCell == 'X') continue;
//
//                int[] goalPosition = findGoalPosition(currentCell, goalBoard);
//
//                int manhattanDistance = Math.abs(i - goalPosition[0]) + Math.abs(j - goalPosition[1]);
//
//                int penalty = getPenalty(currentCell);
//
//                totalDistance += (manhattanDistance * penalty);
//            }
//        }
//
//        return totalDistance;
//    }

    private int[] findGoalPosition(char cell, char[][] goalBoard) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (goalBoard[i][j] == cell) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    private int getPenalty(char cell) {
        switch (cell) {
            case 'B': return 1;   // כחול
            case 'G': return 3;   // ירוק
            case 'R': return 10;  // אדום
            default: return 0;
        }
    }
}
