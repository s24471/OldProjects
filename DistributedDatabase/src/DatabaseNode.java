import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatabaseNode {

    public int tcpport;
    public int Key;
    public int Value;
    public ArrayList<Integer> taskID;
    public ExecutorService executor;
    public ArrayList<ConnectionHandler> connectionHandlers;
    public boolean isAlive;
    public boolean busy;
    public boolean waiting;
    public ServerSocket server;
    //konstruktor
    public DatabaseNode(int tcpport, int key, int value) {
        busy = false;
        taskID = new ArrayList<>();
        taskID.add(1);
        isAlive = true;
        this.tcpport = tcpport;
        Key = key;
        Value = value;
        this.executor = Executors.newCachedThreadPool();
        this.connectionHandlers = new ArrayList<>();
        System.out.println("Stworzono nowy wezel na porcie " + tcpport + " z kluczem i wartoscia: " + key +":" + value);
    }
    //łączenie z nodem podanym w args
    public void connect(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            String connection = address + ":" + port;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("HELLO");
            if(!in.readLine().equals("HELLO")){
                throw new IOException("did not respond to HELLO");
            }
            System.out.println("Nawiazano polaczenie z wezlem " + connection);
            connectionHandlers.add(new ConnectionHandler(this, socket, in, out));
            executor.execute(connectionHandlers.get(connectionHandlers.size()-1));
        } catch (IOException e) {
            System.err.println("Error connecting to node: " + e.getMessage());
        }
    }
    //nasluchiwanie na nowen polaczenia od node albo klientow
    public void start() {
        try {
            server = new ServerSocket(tcpport);
            while (isAlive) {
                System.out.println("oczekiwanie na polaczenie");
                Socket socket = server.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String tmp = in.readLine();
                if(tmp.equals("HELLO")){
                    out.println("HELLO");
                    System.out.println("Nawiazano polaczenie z nowym wezlem");
                    connectionHandlers.add(new ConnectionHandler(this, socket, in, out));
                    executor.execute(connectionHandlers.get(connectionHandlers.size()-1));
                }else executor.execute(new ClientHandler(this, socket, in, out, tmp));
            }
        } catch (IOException e) {
            if(isAlive)System.err.println("Error starting node: " + e.getMessage());
        }
    }

    public int getKey() {
        return Key;
    }

    public int getValue() {
        return Value;
    }

    public static void main(String[] args) {
        int tcpport = 0;
        int key = 0;
        int value = 0;
        ArrayList<String> connections = new ArrayList<>();
        ArrayList<Integer> ports = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-tcpport":
                    tcpport = Integer.parseInt(args[++i]);
                    break;
                case "-record":
                    String[] record = args[++i].split(":");
                    key = Integer.parseInt(record[0]);
                    value = Integer.parseInt(record[1]);
                    break;
                case "-connect":
                    String[] connect = args[++i].split(":");
                    String address = connect[0];
                    int port = Integer.parseInt(connect[1]);
                    connections.add(address);
                    ports.add(port);
                    break;
                default:
                    System.err.println("Invalid argument: " + args[i]);
                    System.exit(1);
            }
        }
        DatabaseNode node = new DatabaseNode(tcpport, key, value);
        for (int i =0; i<connections.size(); i++) {
            node.connect(connections.get(i), ports.get(i));
        }
        node.start();
    }
    //Wyslanie task do sasiednich nodes
    public void sendAll(ConnectionHandler connectionHandler, String task){
        for (ConnectionHandler tmp: connectionHandlers){
            if(tmp != connectionHandler) tmp.send(connectionHandler, task);
        }
        for (ConnectionHandler tmp: connectionHandlers){
            while(tmp.isWaiting) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //Wyslanie task do sasiednich nodes jako oryginalny nadawca
    public void og(ClientHandler clientHandler, String task){
        if(connectionHandlers.size()>0) {
            for (ConnectionHandler tmp : connectionHandlers) {
                tmp.og(clientHandler, task);
            }
            for (ConnectionHandler tmp : connectionHandlers) {
                while (tmp.isOG) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            System.out.println("========solo==========");
            switch (task.split(":")[1]) {
                case "set-value":
                    set_value(task.split(":"), clientHandler);
                    break;
                case "get-value":
                    get_value(task.split(":"), clientHandler);
                    break;
                case "find-key":
                    find_key(task.split(":"), clientHandler);
                    break;
                case "get-max":
                    get_max(clientHandler);
                    break;
                case "get-min":
                    get_min(clientHandler);
                    break;
            }
        }
    }

//operacje
    public void set_value(String[] messageParts, ClientHandler clientHandler) {
        int key = Integer.parseInt(messageParts[2]);
        int value = Integer.parseInt(messageParts[3]);
        if (Key == key) {
            Value = value;
            clientHandler.results.add("RETURN:OK");
        } else {
            clientHandler.results.add("RETURN:ERROR");
        }
    }

    public void get_value(String[] messageParts, ClientHandler clientHandler){
        System.out.println("rozpoczecie operacji get-value");
        int key = Integer.parseInt(messageParts[2]);
        if(key == Key){
            clientHandler.results.add("RETURN:"+key+":"+Value);
        }else{
            clientHandler.results.add("RETURN:ERROR");
        }
    }

    public void find_key(String[] messageParts, ClientHandler clientHandler) {
        int key = Integer.parseInt(messageParts[2]);
        if(Key == key){
            try {
               clientHandler.results.add("RETURN:" + server.getInetAddress().getLocalHost().getHostAddress()+":"+tcpport);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }else{
            clientHandler.results.add("RETURN:ERROR");
        }
    }

    public void get_max(ClientHandler clientHandler) {
        int max = Value;
        int key = Key;
        clientHandler.results.add("RETURN:"+key+":"+max);
    }

    public void get_min(ClientHandler clientHandler) {
        int min = Value;
        int key = Key;
        clientHandler.results.add("RETURN:"+key+":"+min);
    }

    public void terminate(){
        isAlive = false;
        for (ConnectionHandler tmp: connectionHandlers){
            tmp.terminated = true;
            tmp.out("TERMINATED");
            try {
                tmp.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            server.close();
        } catch (IOException e) {
            System.out.println("whdgakj");
        }
        executor.shutdown();
    }
}
