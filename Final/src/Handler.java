import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Handler extends Thread{
    protected Socket clientServerSocket;
    protected PrintWriter messagesOut;
    protected BufferedReader messagesIn;

    public Handler(Socket socket){
            try {
                this.clientServerSocket = socket;

                messagesOut = new PrintWriter(clientServerSocket.getOutputStream(), true);
                messagesIn = new BufferedReader(new InputStreamReader(clientServerSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
                closeConnections();
            }
    }

    protected void closeConnections(){
        try {
            messagesOut.close();
            messagesIn.close();
            clientServerSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    abstract void readRequestAndRespond();
    
    //To get the method above to run when the thread is created. 
    //Could I have done "abstract void run();" maybe, but I think the naming makes more sense to a reader this way.
    public void run(){
        readRequestAndRespond();
    }
}
