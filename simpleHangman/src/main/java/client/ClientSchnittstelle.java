package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


public class ClientSchnittstelle{
    private String name = "Client";
    private String host = "localhost";
    private Integer port = 5050;

    private InetSocketAddress socketAddress;
    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;

    private boolean listening = false;

    Scanner scannerIn = new Scanner(System.in);

    public ClientSchnittstelle(String[] args) {
        if (args.length >=1) this.host = args[0];
        if (args.length >=2) this.port = Integer.parseInt(args[1]);
    }

    public static void main(String[] args) {
        ClientSchnittstelle cs = new ClientSchnittstelle(args);
        cs.run();
    }

    public void run() {
        try {
            socket = new Socket(this.host, this.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket.setKeepAlive(true);
            listening = true;
            System.out.println("Game started!");
            while(listening){
                try{
                    String message = in.readLine();
                    if(message != null && !message.equals("")) {
                        if (message.startsWith("!")){
                            if (message.startsWith("!EXIT"))
                                shutdown();
                            else if (message.startsWith("!ACCEPT")) {
                                System.out.println("Input:");
                                send(scannerIn.next());
                            }
                        } else {
                            System.out.println(message);
                        }
                    }
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public void send(String message) {
        out.println(message);
    }

    public void shutdown() {
        listening = false;
        try {
            if (socket != null && !socket.isClosed()){
                socket.close();
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
     * @return True if still listening and online
     */
    public boolean isListening() {
        return listening;
    }
}
