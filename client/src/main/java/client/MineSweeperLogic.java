package client;

/**
 * The minesweeper class keeps instances of an induvidual minesweeper game
 * this includes the board, location of bombs, and creating them
 */
public class MineSweeperLogic {
    int sizeRow;
    int sizeColumn;
    int numBombs;
    public int field[][];
    public int bombCoor[][];

    /**
     * This is the constructor, creates a new instance
     * @param dem is the demention of the game board
     * @param numBombs is the number of bombs that should be loaded
     */
    public MineSweeperLogic(int dem, int numBombs){
        this.sizeColumn = dem;
        this.sizeRow = dem;
        this.numBombs = numBombs;
        field = new int[(sizeRow+2)][(sizeColumn +2)];
        bombCoor = new int [numBombs][2];
        loadBombs();
    }

    /**
     * Uses an algorithm to spawn random coordinated for the location of the bombs on the board
     * There is checks to make sure the bomb fits on the board and will not over lap a previous bomb
     */
    public void loadBombs() {

        int numBombsPlaced = 0;

        while (numBombsPlaced < numBombs) {
           int x = 0;
           int y = 0;

           while ((x < 1) || (x > sizeRow))
               x = (int) (Math.random() * sizeRow);
           while ((y < 1) || (y > sizeColumn))
               y = (int) (Math.random() * sizeRow);

           if (field[x][y] != -1 && x > 0 && y > 0 && x < sizeRow && y < sizeColumn) {
               field[x][y] = -1;
               bombCoor[numBombsPlaced][0] = x;
               bombCoor[numBombsPlaced][1] = y;
               numBombsPlaced ++;
           }
       }
       setNumbers();
    }

    /**
     * This is a test function used to print out all values of the board
     */
    public void printMap(){
        for(int i = 0; i<field.length; i++){
            for(int j = 0; j< field[0].length; j++) {
                System.out.print(field[i][j]+ " , ");
            }
            System.out.println();
        }
    }

    /**
     * SetNumbers iterate through each spot on the board to see if it is a bomb
     * If it is not a bomb it goes to center check
     */
    public void setNumbers(){
        for(int i=0; i< field.length; i++){
             for(int j=0; j< field[0].length; j++) {
                 if(i> 0 && i< field.length-1 && j> 0 && j<field[0].length-1) {
                     if(field[i][j]!= -1){
                        field[i][j] = centerCheck(i,j);
                    }
                }
            }
        }
    }

    /**
     * centerCheck takes a space that is not a bomb and check to see if there are any bombs around it
     * It adds the number of bombs and puts that value on the board
      * @param X the x coordinate
     * @param Y the y coordinate
     * @return the number of bombs
     */
    public int centerCheck(int X , int Y) {      ///Note x, y are actually revered in this function
        int results = 0;
        for(int i = X-1; i<X+2; i++) {
            for(int j = Y-1; j<Y+2; j++) {
                if(field[i][j]==-1){
                    results++;
                }
            }
        }
        return results;
    }

    /**
     * getter function for the given coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the value at given coordinates
     */
    public int getNum (int x, int y) {
        return field[x+1][y+1];
    }

    /**
     * gets the array of bomb coordinates
     * @return the 2D array of bomb coordinates
     */
    public int[][] getBombCoor() {
        return bombCoor;
    }
}