package client;

public class MineSweeper {
    private int [][] board;
    private int numOfMines;

    public MineSweeper(int demenitons) {
        this.board = new int[demenitons][demenitons];
        this.numOfMines = demenitons;
    }

}
