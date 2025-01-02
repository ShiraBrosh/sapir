import java.util.HashSet;
import java.util.Stack;

public class IDAStar {
    private static int visitedNodes = 0;

    public static State search(State initialState, Board boardHandler, boolean recordOpenList) {
        int threshold = boardHandler.heuristic(initialState.getBoard(), boardHandler.getGoalBoard());

        while (true) {
            int minF = Integer.MAX_VALUE;
            Stack<State> stack = new Stack<>();
            HashSet<String> visited = new HashSet<>();

            stack.push(initialState);

            while (!stack.isEmpty()) {
                State current = stack.pop();

                if (recordOpenList) {
                    printOpenList(stack);
                }

                int fValue = current.getCost() + boardHandler.heuristic(current.getBoard(), boardHandler.getGoalBoard());

                if (fValue > threshold) {
                    minF = Math.min(minF, fValue);
                    continue;
                }

                if (boardHandler.isGoalState(current)) {
                    return current;
                }

                visited.add(getBoardString(current.getBoard()));

                for (State next : boardHandler.getNextStates(current)) {
                    String nextBoardString = getBoardString(next.getBoard());
                    if (!visited.contains(nextBoardString)) {
                        visitedNodes++;
                        stack.push(next);
                    }
                }
            }

            if (minF == Integer.MAX_VALUE) {
                return null;
            }

            threshold = minF;
        }
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
            System.out.println("Cost: " + state.getCost());
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

    public static int getVisitedNodes() {
        return visitedNodes;
    }
}
