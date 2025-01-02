import java.io.*;

public class Ex1 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        String algorithm = reader.readLine().trim();
        String timeOption = reader.readLine().trim();
        String openListOption = reader.readLine().trim();

        char[][] initialBoard = new char[3][3];
        for (int i = 0; i < 3; i++) {
            String line = reader.readLine().replace(",", "").trim();
            initialBoard[i] = line.toCharArray();
        }

        reader.readLine(); // קריאת "Goal state:"

        char[][] goalBoard = new char[3][3];
        for (int i = 0; i < 3; i++) {
            String line = reader.readLine().replace(",", "").trim();
            goalBoard[i] = line.toCharArray();
        }
        reader.close();

        Board boardHandler = new Board(goalBoard);
        State initialState = new State(initialBoard, 0, "", -1);
        State result = null;
        long startTime = System.nanoTime();
        boolean recordOpenList = openListOption.equalsIgnoreCase("with open");

        // משתנה לשמירת מספר הצמתים שנוצרו
        int visitedNodes = 0;

        switch(algorithm.toUpperCase()) {
            case "BFS":
                result = BFS.search(initialState, boardHandler, recordOpenList);
                visitedNodes = BFS.getVisitedNodes();
                break;
            case "DFID":
                result = DFID.search(initialState, boardHandler, recordOpenList);
                visitedNodes = DFID.getVisitedNodes();
                break;
            // נוסיף כאן את שאר האלגוריתמים בהמשך
        }

        long endTime = System.nanoTime();

        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        if (result != null) {
            String path = result.getPath();
            if (path.length() > 2) {
                path = path.substring(0, path.length() - 2);
            }
            writer.write(path + "\n");
            writer.write("Num: " + visitedNodes + "\n");
            writer.write("Cost: " + result.getCost() + "\n");

            if (timeOption.equalsIgnoreCase("with time")) {
                writer.write(String.format("%.3f seconds\n", (endTime - startTime) / 1e9));
            }
        } else {
            writer.write("no path\n");
            writer.write("Num: " + visitedNodes + "\n");
            writer.write("Cost: inf\n");
            if (timeOption.equalsIgnoreCase("with time")) {
                writer.write(String.format("%.3f seconds\n", (endTime - startTime) / 1e9));
            }
        }
        writer.close();
    }
}