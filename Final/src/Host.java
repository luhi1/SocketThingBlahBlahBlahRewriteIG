import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Host{
    private String guess;
    private PrintWriter messagesOut;
    private ObjectOutputStream gameOut;
    private ObjectInputStream gameIn;
    private ArrayList<String> playerIPs;
    private Socket HostServerSocket;
    private ServerSocket HostingSocket; 
    private Boolean gameStarted;
    
    public Host(Socket socket){
        this.playerIPs = new ArrayList<String>();
        this.HostServerSocket = socket;

        try {
            this.guess = "";
            messagesOut = new PrintWriter(HostServerSocket.getOutputStream(), true);
            gameIn = new ObjectInputStream(HostServerSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void initHostServer(){
        try {
            HostingSocket = new ServerSocket(Server.serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for player connections.");
        while (!gameStarted) {
            Socket newSocketConnection = null;
            try {
                newSocketConnection = HostingSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
                close();

            }
            // new thread for a client
            new HostClientHandler(newSocketConnection).start();
        }
        close();
    }

    public void startGame(){
            messagesOut.println("start");
            //Println = send the message, scanner reading is just like user input!
            Game resp;
            try {
                    resp = (Game) gameIn.readObject();
                    this.currentGame(resp);
            } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    close();

            }
            close();
    }

    public void currentGame(Game g) throws Exception{
        if (this.guess.equals("home")){
            Game.clearScreen();
            return;
        }
        g.displayScreen();

        Scanner guessReader = new Scanner(System.in);
        System.out.println("Type here: ");
        this.guess = guessReader.nextLine();

        g.checkGuess(guess);
        currentGame(g);
        
        guessReader.close();
    }

    private void close(){
        messagesOut.close();
        try {
            gameIn.close();
            gameOut.close();
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
            
        }

        protected void closeConnectionsAndGameStreamsAndRemoveIP(){
            super.closeConnections();
            playerIPs.remove(clientIP);
            try {
                gameIn.close();
                gameOut.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void readRequestAndRespond(){
                try {
                        String hostServerRequests = null;
                        
                        while (!hostServerRequests.equals("goTime")){
                            hostServerRequests = messagesIn.readLine();
                            messagesOut.println("Waiting for game to start");
                        }

                        startGame();
                        messagesOut.flush();
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
