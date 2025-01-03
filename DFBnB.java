import java.util.*;

public class DFBnB {
    private static int visitedNodes = 0; // משתנה למעקב אחר צמתים שנוצרו

    public static State search(State initialState, Board boardHandler, boolean recordOpenList) {
        visitedNodes = 0; // איפוס הספירה בתחילת האלגוריתם
        Stack<State> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        int upperBound = Integer.MAX_VALUE; // חסם עליון ראשוני
        State result = null;

        stack.push(initialState);

        while (!stack.isEmpty()) {
            State current = stack.pop();
            String currentBoardString = getBoardString(current.getBoard());

            if (recordOpenList) {
                printOpenList(stack);
            }

            if (visited.contains(currentBoardString)) {
                visited.remove(currentBoardString);
                continue;
            }

            visited.add(currentBoardString);

            if (current.getFValue() >= upperBound) {
                visited.remove(currentBoardString);
                continue;
            }

            if (boardHandler.isGoalState(current)) {
                upperBound = current.getFValue();
                result = current;
                continue;
            }

            List<State> successors = boardHandler.getNextStates(current);

            // עדכון ספירת הצמתים
            visitedNodes += successors.size();

            for (State successor : successors) {
                successor.calculateHeuristic(boardHandler);
            }

            // מיון הצמתים לפי ערך F בסדר עולה
            successors.sort(Comparator.comparingInt(State::getFValue));

            // עיבוד כל הצמתים ברשימת הצאצאים
            for (int i = 0; i < successors.size(); i++) {
                State successor = successors.get(i);

                // אם הערך F של הצומת גדול מהחסם העליון
                if (successor.getFValue() >= upperBound) {
                    while (i < successors.size()) {
                        successors.remove(i);
                    }
                    break;
                }

                // בדיקת מניעת חזרות
                String successorBoardString = getBoardString(successor.getBoard());
                if (visited.contains(successorBoardString)) {
                    successors.remove(i);
                    i--;
                    continue;
                }

                // אם מצאנו מצב יעד עם F קטן מהחסם העליון
                if (boardHandler.isGoalState(successor)) {
                    upperBound = successor.getFValue();
                    result = successor;
                    while (i < successors.size()) {
                        successors.remove(i);
                    }
                    break;
                }
            }

            // הכנסה למחסנית בסדר הפוך
            for (int i = successors.size() - 1; i >= 0; i--) {
                stack.push(successors.get(i));
            }
        }

        return result;
    }

    public static int getVisitedNodes() {
        return visitedNodes; // החזרת מספר הצמתים שנוצרו
    }

    private static String getBoardString(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            for (char c : row) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void printOpenList(Stack<State> stack) {
        System.out.println("------- Open List -------");
        for (State state : stack) {
            printBoard(state.getBoard());
            System.out.println("F Value: " + state.getFValue());
        }
        System.out.println("-------------------------\n");
    }

    private static void printBoard(char[][] board) {
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
}
