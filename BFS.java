import java.util.*;

public class BFS {
    private static int visitedNodes = 0;

    /**
     * חיפוש BFS עם אפשרות לרשום את ה-Open List בכל איטרציה.
     *
     * @param initialState   המצב ההתחלתי
     * @param boardHandler   המחלקה שמטפלת בלוח
     * @param recordOpenList האם לרשום את ה-Open List
     * @return המצב היעד או null אם לא נמצא
     */
    // בקובץ BFS.java
    public static State search(State initialState, Board boardHandler, boolean recordOpenList) {
        visitedNodes = 0;

        Queue<State> openList = new LinkedList<>();
        Set<String> openSet = new HashSet<>();
        Set<String> closedList = new HashSet<>();

        openList.add(initialState);
        openSet.add(getBoardString(initialState.getBoard()));

        while (!openList.isEmpty()) {
            if (recordOpenList) {
                printOpenList(openList);
            }

            State current = openList.poll();
            String currentBoardStr = getBoardString(current.getBoard());
            openSet.remove(currentBoardStr);

            closedList.add(currentBoardStr);

            List<State> nextStates = boardHandler.getNextStates(current);
            visitedNodes += nextStates.size();

            for (State next : nextStates) {
                // בודקים אם הגענו למצב היעד כשמייצרים מצב חדש
                if (boardHandler.isGoalState(next)) {
                    return next;
                }

                String nextBoardStr = getBoardString(next.getBoard());
                if (!closedList.contains(nextBoardStr) && !openSet.contains(nextBoardStr)) {
                    openList.add(next);
                    openSet.add(nextBoardStr);
                }
            }
        }

        return null;
    }
    /**
     * הדפסת ה-Open List כמרשימת מטריצות לטרמינל.
     *
     * @param openList התור שמייצג את ה-Open List
     */
    private static void printOpenList(Queue<State> openList) {
        System.out.println("------- Open List -------");
        int count = 1;
        for (State state : openList) {
            System.out.println("State " + count + ":");
            printBoard(state.getBoard());
            count++;
        }
        System.out.println("-------------------------\n");
    }

    /**
     * הדפסת לוח המשחק בצורה של מטריצה.
     *
     * @param board הלוח להדפסה
     */
    private static void printBoard(char[][] board) {
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    /**
     * המרת לוח למחרוזת.
     *
     * @param board הלוח להמרה
     * @return מחרוזת המייצגת את הלוח
     */
    private static String getBoardString(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            for (char c : row) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * השגת מספר הצמתים שנבדקו.
     *
     * @return מספר הצמתים שנבדקו
     */
    public static int getVisitedNodes() {
        return visitedNodes;
    }
}