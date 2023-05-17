import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Host extends Thread{
    private String guess;
    private PrintWriter messagesOut;
    private ObjectOutputStream gameOut;
    private ObjectInputStream gameIn;
    private ArrayList<String> playerIPs;
    private HashMap<String, PrintWriter> playersAndOutputs;
    private Socket HostServerSocket;
    private ServerSocket HostingSocket; 
    private Boolean gameStarted;
    
    public Host(Socket socket){
        this.playerIPs = new ArrayList<String>();
        this.HostServerSocket = socket;
        this.playersAndOutputs = new HashMap<>();
        playerIPs.add(HostServerSocket.getInetAddress().getHostAddress());
        this.gameStarted = false;

        try {
            this.guess = "";
            messagesOut = new PrintWriter(HostServerSocket.getOutputStream(), true);
            playersAndOutputs.put(playerIPs.get(0), messagesOut);

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
            // new thread for a client
            HostClientHandler newHostClientHandler = new HostClientHandler(newSocketConnection);
            new Thread(newHostClientHandler).start();
        }
        close();
    }

    public void currentGame(Game g, int playerIndex) throws Exception{
        if (playerIndex == playerIPs.size()){
            playerIndex = 0;
        }

        if (this.guess.equals("home")){
            Game.clearScreen();
            return;
        }
        g.setCurrentPlayer(playerIPs.get(playerIndex));
        for (String player: playerIPs){
            g.displayScreen(playersAndOutputs.get(player));
            if (player == g.getCurrentPlayer()){
                playersAndOutputs.get(player).println("Type your guess here:");
                int counter = 0;
                while (!guess.contains("Guess:")){
                    counter++;
                    if (counter == 10000000){
                        guess = "";
                        break;
                    }
                }
            }
        }
        g.checkGuess(guess);
        currentGame(g, playerIndex+1);
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
                playersAndOutputs.put(clientIP, super.messagesOut);
        }

        protected void closeConnectionsAndGameStreamsAndRemoveIP(){
            super.closeConnections();
            playerIPs.remove(clientIP);
            playersAndOutputs.remove(clientIP);
        }

        public void readRequestAndRespond(){
                try {
                        String hostServerRequests = "";
                        messagesOut.println("Waiting for game to start");
                        while (!gameStarted){
                            hostServerRequests = messagesIn.readLine();
                            if (messagesIn.readLine().equals("check")){
                                messagesOut.println("");
                            }
                        }
                        messagesOut.flush();
                        do {
                            if (!hostServerRequests.contains("Guess:")){
                                hostServerRequests = messagesIn.readLine();
                                continue;
                            } 

                        } while (!(hostServerRequests.contains("Guess:")));
                        //Get them into the game and start it up
                        closeConnectionsAndGameStreamsAndRemoveIP();

                } catch (Exception e) {
                        e.printStackTrace();
                        closeConnectionsAndGameStreamsAndRemoveIP();
                        return;
                }
        }
    }
}
