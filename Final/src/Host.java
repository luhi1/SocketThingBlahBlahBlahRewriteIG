import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Host extends Thread{
    private String guess;
    private PrintWriter messagesOut;
    private ArrayList<String> playerIPs;
    private Socket HostServerSocket;
    private ServerSocket HostingSocket; 
    private Boolean gameStarted;
    private Game game;
    
    public Host(Socket socket){
        this.playerIPs = new ArrayList<String>();
        this.HostServerSocket = socket;
        playerIPs.add(HostServerSocket.getInetAddress().getHostAddress());
        this.gameStarted = false;

        try {
            this.guess = "";
            messagesOut = new PrintWriter(HostServerSocket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace(System.out);
            close();
        }
    }

    public void run(){
        try {
            HostingSocket = new ServerSocket(Server.serverPort+1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        messagesOut.println("Waiting for player connections.");
        while (!gameStarted) {
            Socket newSocketConnection = null;
            try {
                newSocketConnection = HostingSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                close();

            }

            //One player can join after the game starts, that's what this if statement avoids.
            if (!gameStarted){
                // new thread for a client
                HostClientHandler newHostClientHandler = new HostClientHandler(newSocketConnection);
                new Thread(newHostClientHandler).start();
            }
            
        }
        close();
    }

    public void currentGame(Game g, int playerIndex) throws Exception{
        this.game = g;
        guess = "";
        if (playerIndex == playerIPs.size()-1){
            playerIndex = 0;
        }

        if (this.guess.equals("home")){
            Game.clearScreen();
            return;
        }

        this.game.setCurrentPlayer(playerIPs.get(playerIndex));
        while (guess.equals("")){
            wait(10000);
            if (guess.equals("")){
                guess = "noInput";
            }
        }

        if (guess.equals("noInput")){
            currentGame(this.game, playerIndex+1);        
            return;
        }

        this.game.checkGuess(guess);
        currentGame(this.game, playerIndex+1);
    }

    private void close(){
        messagesOut.close();
        try {
            HostingSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class HostClientHandler extends Handler{
        private String clientIP;

        public HostClientHandler(Socket socket){
                super(socket);
                this.clientIP = super.clientServerSocket.getInetAddress().getHostAddress();
                playerIPs.add(clientIP);
            
        }

        protected void closeConnectionsAndGameStreamsAndRemoveIP(){
            super.closeConnections();
            playerIPs.remove(clientIP);
        }

        public void readRequestAndRespond(){
                try {                        
                        while (!gameStarted){
                            messagesOut.println("Waiting for game to start");
                            messagesOut.flush();
                        }

                        String hostServerRequests = "";

                        do {
                            if (clientIP.equals(game.getCurrentPlayer())){
                                messagesOut.println("Type your guess here:");
                                while (hostServerRequests == null){
                                    if (!clientIP.equals(game.getCurrentPlayer())){
                                        break;
                                    }
                                    hostServerRequests = messagesIn.readLine();
                                }
                            } else {
                                game.displayScreen(messagesOut);
                            }

                        } while (!guess.equals("home"));
                        closeConnectionsAndGameStreamsAndRemoveIP();

                } catch (Exception e) {
                        e.printStackTrace();
                        closeConnectionsAndGameStreamsAndRemoveIP();
                        return;
                }
        }
    }
}
