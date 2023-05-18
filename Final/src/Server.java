import java.io.IOException;
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
    public static final int serverPort = 6666;
    
    public Server(int port) {
        this.clientIPs = new ArrayList<String>();
        this.hostIPs = new ArrayList<String>();
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initServer(){
        //I know you hate this Mr.Mayo, but I think it's verbose to add a variable to check whether or not the Server should be up and then ending the while loop then.
        while (true) {
            Socket newSocketConnection;
            try {
                System.out.println("Waiting for client connections.");
                newSocketConnection = serverSocket.accept();
                // new thread for a client
                ServerClientHandler newServerClientHandler = new ServerClientHandler(newSocketConnection);
                new Thread(newServerClientHandler).start();

            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
        }
    }
    
    private Game createGame() throws Exception{
        Game newGame;
        String secretWord = randWords[(int) (Math.random()*randWords.length)];
        newGame = new Game(secretWord);
        return newGame;
    }
    

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
                                    
                } while (clientServerRequests == null || (!clientServerRequests.contains("Join: ") && (!clientServerRequests.equals("host")) && (!clientServerRequests.equals("quit"))));      
                //Short circuit operators baby!
                
                if (clientServerRequests.contains("Join:") && hostIPs.contains(clientServerRequests.replace("Join:", ""))){
                    //Looks wierd but basically is just saying "this handler's work is done here. Make another socket if you want to rejoin."
                    closeConnectionsAndRemoveIP();
                    return;
                }

                switch (clientServerRequests){
                    case "list":
                        for (String currentIP : hostIPs){
                            messagesOut.println(currentIP);
                        }
                        messagesOut.println("List of hosts you could join!");
                        break;
                        
                    case "host":
                        hostIPs.add(clientIPs.remove(clientIPs.indexOf(clientIP)));
                        ServerHostHandler newServerHostHandler = new ServerHostHandler(super.clientServerSocket);
                        new Thread(newServerHostHandler).start();
                        break;

                    case "quit":
                        closeConnectionsAndRemoveIP();
                        break;
                }
                return;
                //Unnecasry but ensures garbage collection of handler.
            } catch (Exception e) {
                    e.printStackTrace();
                    closeConnectionsAndRemoveIP();
                    return;
            }
        }
    }
    
    private class ServerHostHandler extends Handler{
        private String hostIP;

        public ServerHostHandler(Socket socket){
                super(socket);
                this.hostIP = super.clientServerSocket.getInetAddress().getHostAddress();
        }

        protected void closeConnectionsAndRemoveIP(){
            super.closeConnections();
            clientIPs.remove(hostIP);
            hostIPs.remove(hostIP);
        }

        public void readRequestAndRespond(){
                Host myHost = new Host(super.clientServerSocket);
                new Thread(myHost).start();

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

                        } while (!hostServerRequests.equals("start"));     
                        
                        hostIPs.remove(this.hostIP);  
                        Game newGame = createGame();
                        myHost.currentGame(newGame,0);  
                        
                        closeConnectionsAndRemoveIP();

                } catch (Exception e) {
                        e.printStackTrace();
                        closeConnectionsAndRemoveIP();
                        return;
                }
        }
    }
}