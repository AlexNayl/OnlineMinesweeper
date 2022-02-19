# csci2020u-assignmnet
Mine Sweepers
by Alex Nayl, Jessica Wall, and Alexie Linardatos

Game Description 
This project is based on the popular game mine sweepers. Players connect to a server  by entering an IP address, a port number, and a username.

![image](https://user-images.githubusercontent.com/61524049/114794548-b0936800-9d5a-11eb-9c72-b1d7ee55269e.png)

Then they are able to pick a easy, medium, or hard level.

![image](https://user-images.githubusercontent.com/61524049/114794560-b5581c00-9d5a-11eb-8446-7582cd82a2be.png)


After they pick a grid will appear the user then can pick squares, each square will either reveal a number ranging from 1-9, a bomb, or a blank square. The number represents the number of bombs within a one square radius of the number clicked.  The blank square means that there are no bombs within a one square radius, and the bomb represents the player finding a bomb and that its game over. When a player click all the square around a bomb a flag will appear on the square containing the bomb. 

![image](https://user-images.githubusercontent.com/61524049/114794577-bd17c080-9d5a-11eb-876e-c586ef90df91.png)


After the game is over a top ten board appears from the top 10 past user scores. The user can then deiced to play another round or exit the
application.


![image](https://user-images.githubusercontent.com/61524049/114794587-c012b100-9d5a-11eb-9847-7cebd0949bb0.png)


Set up/ Running 
To set up the program all you need to do is clone the repository from “https://github.com/AlexNayl/OnlineMinesweeper” open the project and build/ run it through gradle. If you have gradle already installed ally you have to do it configure the package to your java file and run it. 
