# SE181
The Chess Game is designed to be played on two separate computers from remote locations. 


## Creating Server 
Before you play you have to setup your server environment. 

1. Open up the file HttpUtil.kt 

2. Both players change the Host variable to be the IP Address they want to host the game from. 

    Ex: HOST = "localhost"

3. Set the port to be a port both players agree on. 

    Ex: PORT = 7000

4. Set the GAMES_URL equal to "http://IP_Address:Port/games"

    Ex: GAMES_URL = "http://localhost:7000/games"
    
5. Set the WEBSOCKET_URL equal to "ws://IP_Address:Port/games"

    Ex: WEBSOCkET_URL = "ws://localhost:7000/games"
    
6. Run \chessServer\main\kotlin\Main.kt to run server. The server may take some time to load up.
    
## Running Chess Game 

1. Run the Chess program through \chessGame\src\main\java\edu\se181\MainApp

2. Create a Room that both players can join. You may set a password if you choose. 

3. Wait for both players to join game room. 

4. Whoever hosts the game will be the White Player. 

5. Play till checkmate, draw, or surrender. 

6. To play again, open up a new room or join an existing room. 
 
