package server;

import java.util.Random;

public class logic{
    int sizeRow = 10;
    int sizeColum = 11;
    int numBombs = 10;
    int numBombsPlaced = 0;
    public double field[][] = new double[(sizeRow+1)][(sizeColum+1)];
    public logic(){
        loadBombs(numBombs);
    };

    public void loadBombs(int bombs) {
        boolean AllBombsNotPlaced = true;

       while(AllBombsNotPlaced) {
           if(numBombsPlaced==bombs){
               AllBombsNotPlaced = false;
           }
           else{
            int x = 0;
            int y = 0;
            x = (int) (Math.random() * sizeRow);
            y = (int) (Math.random() * sizeColum);
            System.out.println("y ,x: " + x + " , " + y);
                if (field[x][y] != -1 && x > 0 && y > 0 && x < sizeRow && y < sizeColum) {
                    field[x][y] = -1;
                    numBombsPlaced ++;
                }
           }
       }
       printMap();
       setNumbers();
    }
public void printMap(){
    int counter = 0;

    for(int i = 0; i<field.length; i++){
        for(int j = 0; j< field[0].length; j++) {
            System.out.print(field[i][j]+ " , ");
        }
        System.out.println();
    }
}
public void setNumbers(){
    System.out.println("got here");
     for(int i=0; i< field.length; i++){
         for(int j=0; j< field[0].length; j++){ //should i do a +1 on length?
             if(i> 0 && i< field.length-1 && j> 0 && j<field[0].length-1){
                 if(field[i][j]!= -1){
                     System.out.println("Set Numbers: i, j : "+ i + " , " + j);
                    // printSquare(i,j);
                     field[i][j] = centerCheck(i,j);
                     //System.out.println(field[i][j]);
                 }else{
                    System.out.println("bombs: " + i +" , " + j);
                 }
             }
         }
     }
      //  field[cenSizeX][cenSizeY]=bombNearBy;
    printMap();
}


public int centerCheck(int X , int Y){      ///Note x, y are actually revered in this function
        int results = 0;
    for(int i = X-1; i<X+2; i++){
        for(int j = Y-1; j<Y+2; j++){

            //System.out.println("i, j : "+ i + " , " + j);
            if(field[i][j]==-1){
                results++;
            }
        }
    }
        return results;
}
public void printSquare(int i, int j){
    for(int z = i-1; z<i+2; z++) {
        for (int a = j - 1; a < j + 2; a++) {
        System.out.print(field[z][a]+" , ");
        }
        System.out.println();
    }
    }
public static void main(String[] args){
        logic smallBoard = new logic();

}


}