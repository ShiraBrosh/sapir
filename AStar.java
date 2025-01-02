import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class AStar {
    private static int visitedNodes = 0;

    public static State search(State initialState, Board boardHandler, boolean recordOpenList) {
        visitedNodes = 0;
        initialState.calculateHeuristic(boardHandler);

        PriorityQueue<State> openList = new PriorityQueue<>((s1, s2) -> {
            int f1 = s1.getFValue();
            int f2 = s2.getFValue();
            return f1 != f2 ? f1 - f2 : s1.getGenerationTime() - s2.getGenerationTime();
        });

        Set<String> closedSet = new HashSet<>();
        Set<String> openSet = new HashSet<>();

        openList.add(initialState);
        openSet.add(getBoardString(initialState.getBoard()));

        while (!openList.isEmpty()) {
            if (recordOpenList) {
                printOpenList(openList);
            }

            State current = openList.poll();
            String currentBoard = getBoardString(current.getBoard());
            openSet.remove(currentBoard);

            if (boardHandler.isGoalState(current)) {
                return current;
            }

            closedSet.add(currentBoard);

            for (State next : boardHandler.getNextStates(current)) {
                next.calculateHeuristic(boardHandler);
                String nextBoard = getBoardString(next.getBoard());

                if (closedSet.contains(nextBoard)) {
                    continue;
                }

                next.setGenerationTime(visitedNodes);

                if (!openSet.contains(nextBoard)) {
                    openList.add(next);
                    openSet.add(nextBoard);
                }
            }

            visitedNodes += boardHandler.getNextStates(current).size();
        }

        return null;
    }

    private static void printOpenList(PriorityQueue<State> openList) {
        System.out.println("------- Open List -------");
        int count = 1;
        for (State state : openList) {
            System.out.println("State " + count + ":");
            printBoard(state.getBoard());
            System.out.println("F Value: " + state.getFValue());
            count++;
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

    private static String getBoardString(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            for (char c : row) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static int getVisitedNodes() {
        return visitedNodes;
    }
}