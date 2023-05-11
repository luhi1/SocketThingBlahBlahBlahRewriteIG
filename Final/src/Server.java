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
        this.serverSocket = null;
    }

    public void initServer(){
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for client connections.");
        while (true) {
            Socket newSocketConnection = null;
            try {
                newSocketConnection = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            new ServerClientHandler(newSocketConnection).start();
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

    public class ServerClientHandler extends Thread{
        private Socket clientServerSocket;
        private PrintWriter messagesOut;
        private BufferedReader messagesIn;
        private String clientIP;

        public ServerClientHandler(Socket socket){
                this.clientServerSocket = socket;
                this.clientIP = clientServerSocket.getInetAddress().getHostAddress();
        }

        public void readServerClientRequestsAndRespond(){
                try {
                        messagesOut = new PrintWriter(clientServerSocket.getOutputStream(), true);
                        messagesIn = new BufferedReader(new InputStreamReader(clientServerSocket.getInputStream()));

                        messagesOut.println("Welcome to the rodeo! \n Type \"list\" to view the list of games, type \"quit\" to exit, or type \"host\" to host a game!");
                        String hostClientRequests;
                        do {
                            hostClientRequests = messagesIn.readLine(); 
                            
                            if (hostClientRequests == null){
                                hostClientRequests = messagesIn.readLine(); 
                                continue;
                            }

                            switch (hostClientRequests){
                                    case ("list"):
                                            clientIPs.add(clientIP);
                                            messagesOut.println("List of hosts you could join: WIP");
                                            for (String currentIP : clientIPs){
                                                messagesOut.println(currentIP);
                                            }
                                            //Print out list of hosts.
                                    case ("host"):
                                            hostIPs.add(clientIPs.remove(clientIPs.indexOf(clientIP)));
                                            // @TODO new Host(hostClientRequests, Server.serverPort, createGame());
                                    case ("quit"):
                                            // @TODO implement
                            }
                            if (hostClientRequests.contains("Join: ")){
                                // @TODO add this guy to the host's ip list and go crazy uhhh.
                            }
                            messagesOut.flush();
                        } while (!hostClientRequests.contains("Join: ") || (!hostClientRequests.equals("host")) || !hostClientRequests.equals("quit"));      
                        closeConnections();
                } catch (Exception e) {
                        e.printStackTrace();
                        closeConnections();
                        return;
                }
        }

        private void closeConnections(){
            try {
                messagesOut.close();
                messagesIn.close();
                clientServerSocket.close();
                clientIPs.remove(clientIP);
                hostIPs.remove(clientIP)
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
}