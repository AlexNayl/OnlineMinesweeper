package client;

public class MineSweeperLogic {
    int sizeRow;
    int sizeColunm;
    int numBombs;
    int numBombsPlaced = 0;
    public double field[][];
    public int bombCoor[][];

    public MineSweeperLogic(int dem, int numBombs){
        this.sizeColunm = dem;
        this.sizeRow = dem;
        this.numBombs = numBombs;
        field = new double[(sizeRow+2)][(sizeColunm+2)];
        bombCoor = new int [dem][2];
        loadBombs();
    };

    public void loadBombs() {

       for (int i = 0; i < numBombs; i++) {
           int x = 0;
           int y = 0;

           while ((x < 1) || (x > 10))
               x = (int) (Math.random() * sizeRow);
           while ((y < 1) || (y > 10))
               y = (int) (Math.random() * sizeRow);

           for (int j = 0; j < i; j++){
               if ((x == bombCoor[j][0]) && (y == bombCoor[i][1])) {
                   while ((x < 1) || (x > 10))
                       x = (int) (Math.random() * sizeRow);
                   while ((y < 1) || (y > 10))
                       y = (int) (Math.random() * sizeRow);
               }
           }

           bombCoor[i][0] = x;
           bombCoor[i][1] = y;



               if (field[x][y] != -1 && x > 0 && y > 0 && x < sizeRow && y < sizeColunm) {
                   field[x][y] = -1;
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
            System.out.println(field);
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
    public String toString(){
        String result ="";
        for(int i=0; i< field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                result += String.valueOf(field[i][j]) + ",";
                //maybe add something here to make it easier to know when to do a new line
            }
            result+="\n";
        }
        return result;
    }
    public double getNum (int x, int y) {
        return field[x+1][y+1];
    }

    public int[][] getBombCoor() {
        return bombCoor;
    }
}