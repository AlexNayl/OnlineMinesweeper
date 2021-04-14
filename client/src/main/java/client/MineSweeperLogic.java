package client;

public class MineSweeperLogic {
    int sizeRow;
    int sizeColumn;
    int numBombs;
    public int field[][];
    public int bombCoor[][];

    public MineSweeperLogic(int dem, int numBombs){
        this.sizeColumn = dem;
        this.sizeRow = dem;
        this.numBombs = numBombs;
        field = new int[(sizeRow+2)][(sizeColumn +2)];
        bombCoor = new int [numBombs][2];
        loadBombs();
    };

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

    public void printMap(){

        for(int i = 0; i<field.length; i++){
            for(int j = 0; j< field[0].length; j++) {
                System.out.print(field[i][j]+ " , ");
            }
            System.out.println();
        }
    }

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

    public int getNum (int x, int y) {
        return field[x+1][y+1];
    }

    public int[][] getBombCoor() {
        return bombCoor;
    }
}