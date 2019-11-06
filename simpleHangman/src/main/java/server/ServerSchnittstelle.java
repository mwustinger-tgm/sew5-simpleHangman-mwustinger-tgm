package server;


import javax.print.DocFlavor;

import static java.util.logging.Level.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * SimpleChatServer listens to incoming SimpleChatClients with the choosen communication protocol and initiates a UI.
 * <br>
 * Default settings for the main attributes will be: host="localhost" port=5050 and backlog=5
 */
public class ServerSchnittstelle{
    private Integer port = 5050;
    private String host = "localhost";
    private final Integer backlog = 5;
    private ServerSocket serverSocket = null;

    private boolean listening = false;

    private List<ClientWorker> workerList = new ArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public ServerSchnittstelle(String[] args) {
        if (args.length >=1) this.host = args[0];
        if (args.length >=2) this.port = Integer.parseInt(args[1]);
        HangmanModel.initBasicWords();
    }

    public static void main(String[] args) {
        ServerSchnittstelle ss = new ServerSchnittstelle(args);
        ss.run();
    }

    /**
     * Initiating the ServerSocket with already defined Parameters and starts accepting incoming
     * requests. If client connects to the ServerSocket a new ClientWorker will be created and passed
     * to the ExecutorService for immediate concurrent action.
     */
    public void run() {
        try{
            serverSocket = new ServerSocket(this.port, this.backlog);
            listening = true;
            while(listening){
                Socket socket = serverSocket.accept();
                ClientWorker worker = new ClientWorker(socket, this);
                workerList.add(worker);
                executorService.execute(worker);
            }
        }catch(IOException ioex){
            System.err.print(ioex.getMessage());
        }

    }

    /**
     * Getter for the workerList for testcases
     *
     * @return workerList the list of the workers
     */
    public List<ClientWorker> getWorkerList() {
        return workerList;
    }

    /**
     * Clean shutdown of all connected Clients.<br>
     * ExecutorService will stop accepting new Thread inits.
     * After notifying all clients, ServerSocket will be closed and ExecutorService will try to shutdown all
     * active ClientWorker Threads.
     */
    public void shutdown() {
        for(ClientWorker w: workerList){
            w.shutdown();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}

/**
 * Thread for client socket connection.<br>
 * Every client has to be handled by an own Thread.
 */
class ClientWorker implements Runnable {
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    private ServerSchnittstelle callback;
    private HangmanModel hangman;
    private boolean listening = true;

    ClientWorker(Socket client, ServerSchnittstelle callback) throws IOException {
        this.client = client;
        this.callback = callback;
        this.hangman = new HangmanModel(10, null);
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    @Override
    public void run() {
        send(hangman.getTries()+" remaining tries. "+hangman.getCurrentWord());
        send("!ACCEPT");
        while(listening){
            try{
                String message = in.readLine();
                if(message != null && !message.equals("")) {
                    message = message.toUpperCase();
                    if (message.startsWith("!")){
                        if (message.startsWith("!EXIT"))
                            shutdown();
                        else if (message.startsWith("!SHUTDOWN"))
                            callback.shutdown();
                    } else {
                        if (message.length() == 1) {
                            hangman.replaceCharInWord(message.charAt(0));
                        } else {
                            hangman.checkIfRightWord(message);
                        }
                        if (hangman.isAlreadyWon()) {
                            send("You Won!");
                            shutdown();
                        } else if (hangman.isAlreadyLost()){
                            send("You lose. The word was: "+hangman.getWord());
                            shutdown();
                        } else {
                            send(hangman.getTries()+" remaining tries. "+hangman.getCurrentWord());
                            send("!ACCEPT");
                        }
                    }

                }
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }

    void shutdown() {
        listening = false;
        send("!EXIT");
        try {
            if (client != null && !client.isClosed()){
                client.close();
            }
            if (in != null){
                in.close();
            }
            if (out != null){
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sending message through Socket OutputStream {@link #out}
     *
     * @param message MessageText for Client
     */
    void send(String message) {
        out.println(message);
    }

}
