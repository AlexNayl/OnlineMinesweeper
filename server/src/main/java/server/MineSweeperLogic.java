package server;

import java.util.Random;

public class MineSweeperLogic{
    int sizeRow;
    int sizeColunm;
    int numBombs;
    int numBombsPlaced = 0;
    public double field[][];
    public int bombCoor[][];

    public MineSweeperLogic(int dem){
        this.sizeColunm = dem;
        this.sizeRow = dem;
        this.numBombs = dem;
        field = new double[(sizeRow+2)][(sizeColunm+2)];
        bombCoor = new int [dem][2];
        loadBombs(numBombs);
    };

    public void loadBombs(int bombs) {

       for (int i = 0; i < numBombs; i++) {
           int x = 0;
           int y = 0;
           x = (int) (Math.random() * sizeRow);
           y = (int) (Math.random() * sizeColunm);
           bombCoor[i][0] = x;
           bombCoor[i][1] = y;

               if (field[x][y] != -1 && x > 0 && y > 0 && x < sizeRow && y < sizeColunm) {
                   field[x][y] = -1;
                   numBombsPlaced ++;
               }
       }

       for (int i = 0; i < numBombs; i++) {
           for (int j = 0; j < 2; j++){
               System.out.print(" " + bombCoor[i][j]);
           }
           System.out.println(" ");
       }
       printMap();
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
        printMap();
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

    public double getNum (int x, int y) {
        return field[x+1][y+1];
    }

    public int[][] getBombCoor() {
        return bombCoor;
    }
}