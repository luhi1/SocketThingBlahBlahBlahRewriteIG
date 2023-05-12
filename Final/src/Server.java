import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
    private final String[] randWords = {
		"abandoned","able","absolute","adorable","adventurous","academic","acceptable","acclaimed","accomplished",
		"accurate","aching","acidic","acrobatic","active","actual","adept","admirable","admired","adolescent",
		"adorable","adored","advanced","afraid","affectionate","aged","aggravating","aggressive","agile","agitated",
		"agonizing","agreeable","ajar","alarmed","alarming","alert","alienated","alive","all","altruistic","amazing",
		"ambitious","ample","amused","amusing","anchored","ancient","angelic","angry","anguished","animated","annual",
		"another","antique","anxious","any","apprehensive","appropriate","apt","arctic","arid","aromatic","artistic",
		"ashamed","assured","astonishing","athletic","attached","attentive","attractive","austere","authentic",
		"authorized","automatic","avaricious","average","aware","awesome","awful","awkward","babyish","bad","back",
		"baggy","bare","barren","basic","beautiful","belated","beloved","beneficial","better","best","bewitched","big",
		"bighearted","biodegradable","bitesized","bitter","black", "men"
        };
    private ServerSocket serverSocket;
    private ArrayList<String> clientIPs;
    private ArrayList<String> hostIPs;
    //hashmap
    public static final int serverPort = 6666;
    
    public Server(int port) {
        this.clientIPs = new ArrayList<String>();
        this.hostIPs = new ArrayList<String>();
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }

    }

    public void initServer(){
        while (true) {
            Socket newSocketConnection;
            try {
                System.out.println("Waiting for client connections.");
                newSocketConnection = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
                close();
                break;
            }
            // new thread for a client
            ServerClientHandler newServerClientHandler = new ServerClientHandler(newSocketConnection);
            new Thread(newServerClientHandler).start();
        }
    }

    private void close(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    private Game createGame() throws Exception{
        Game newGame;
        String secretWord = randWords[(int) (Math.random()*randWords.length)];
        newGame = new Game(secretWord);
        return newGame;
    }
     */

    private class ServerClientHandler extends Handler{
        private String clientIP;

        public ServerClientHandler(Socket socket){
                super(socket);
                this.clientIP = super.clientServerSocket.getInetAddress().getHostAddress();
                clientIPs.add(clientIP);
        }

        protected void closeConnectionsAndRemoveIP(){
            super.closeConnections();
            clientIPs.remove(clientIP);
            hostIPs.remove(clientIP);
        }

        public void readRequestAndRespond(){
            try {
                messagesOut.println("Welcome to the rodeo! \n Type \"list\" to view the list of games, type \"quit\" to exit, or type \"host\" to host a game!");
                String clientServerRequests;
                do {
                    clientServerRequests = messagesIn.readLine(); 
            
                    if (clientServerRequests == null){
                        continue;
                    }

                    messagesOut.flush();
                    if (clientServerRequests.contains("Join: ")){
                        break;
                    }

                    switch (clientServerRequests){
                        case "list":
                            for (String currentIP : clientIPs){
                                messagesOut.println(currentIP);
                            }
                            messagesOut.println("List of hosts you could join!");
                            break;
                            
                        case "host":
                            hostIPs.add(clientIPs.remove(clientIPs.indexOf(clientIP)));
                            //ServerHostHandler newServerHostHandler = new ServerHostHandler(super.clientServerSocket);
                            //new Thread(newServerHostHandler).start();

                            Host uHost = new Host(super.clientServerSocket);
                            uHost.initHostServer();
                            break;
                    }

                    messagesOut.flush();
                                    
                } while (clientServerRequests == null || (!clientServerRequests.contains("Join: ") && (!clientServerRequests.equals("host")) && (!clientServerRequests.equals("quit"))));      
                    
                closeConnectionsAndRemoveIP();
            } catch (Exception e) {
                    e.printStackTrace();
                    closeConnectionsAndRemoveIP();
                    return;
            }
        }
    }
    
    private class ServerHostHandler extends Handler{
        private String hostIP;
        private ObjectOutputStream gameOut;
        private ObjectInputStream gameIn;

        public ServerHostHandler(Socket socket){
                super(socket);
                try {
                    this.hostIP = super.clientServerSocket.getInetAddress().getHostAddress();
                    gameOut = new ObjectOutputStream(super.clientServerSocket.getOutputStream());
                    gameIn = new ObjectInputStream(super.clientServerSocket.getInputStream());

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

        protected void closeConnectionsAndGameStreamsAndRemoveIP(){
            super.closeConnections();
            clientIPs.remove(hostIP);
            hostIPs.remove(hostIP);
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
                        messagesOut.println("Type \"start\" to start the game.");
                        String hostServerRequests;
                        do {
                            hostServerRequests = messagesIn.readLine(); 
                            
                            if (hostServerRequests == null){
                                hostServerRequests = messagesIn.readLine(); 
                                continue;
                            }

                            messagesOut.flush();

                            if (hostServerRequests.equalsIgnoreCase("start")){
                                //Give host a game.
                                //start the game and shit.
                            } 
                        } while (!hostServerRequests.equals("start"));      
                        
                        closeConnectionsAndGameStreamsAndRemoveIP();

                } catch (Exception e) {
                        e.printStackTrace();
                        closeConnectionsAndGameStreamsAndRemoveIP();
                        return;
                }
        }
    }
}