package client;

public class MineSweeper {
    private int[][] board;
    private int numOfMines;

    public MineSweeper(int demenitons) {
        this.board = new int[demenitons][demenitons];
        this.numOfMines = demenitons;
    }

    public static String[][] toField(String numbers, int dementions) {
        String[] rows = numbers.split("\n");
        String[][] field = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(",");
            field[i] = new String[cols.length];
            for (int iCol = 0; iCol < cols.length; iCol++) {
                field[i][iCol] = cols[iCol];
            }
        }
        return field;
    }
}
