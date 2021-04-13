package client;

public class MineSweeper {
    private int [][] board;
    private int numOfMines;

    public MineSweeper(int demenitons) {
        this.board = new int[demenitons][demenitons];
        this.numOfMines = demenitons;
    }
    public double[][] toField(String numbers, int dementions){
        double[][] field= new double[dementions][dementions];
        for(int i=0; i< field.length; i++){
            for(int j=0; j< field[0].length; j++) {
           // field[i][j] =(double) numbers.split(",");
            }
        }
        return field;
    }
}
