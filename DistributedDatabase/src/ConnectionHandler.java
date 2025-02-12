import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable {
    private static int NR = 0;
    private static String SPLIT = ":";
    private static String RETURN = "RETURN:";
    private static String RETURN_OK = "RETURN:OK";
    private static String RETURN_ERROR = "RETURN:ERROR";

    private int nr;
    private DatabaseNode node;
    public Socket socket;
    private BufferedReader  in;
    private PrintWriter out;
    public boolean isWaiting;
    private ArrayList<String> results;
    private ConnectionHandler connectionHandler;
    private ClientHandler clientHandler;
    public boolean isOG;
    public boolean terminated;

    //Konstruktor
    public ConnectionHandler(DatabaseNode node, Socket socket, BufferedReader in, PrintWriter out) {
        nr = NR;
        NR++;
        this.node = node;
        this.socket = socket;
        this.out= out;
        this.in = in;
        isWaiting = false;
        isOG = false;
        results = new ArrayList<>();
        terminated = false;
    }

    //Funkcja wypisująca do połączonego node
    public void out(String str){
        System.out.println(nr + " out: " + str);
        out.println(str);
    }

    @Override
    public void run() {
        try {
            System.out.println(nr + " started");
            while (true) {
                String message = null;
                //Nasluchiwanie na polecenie
                while (message == null && !terminated) {
                    message = in.readLine();
                }
                if (terminated) {
                    out("TERMINATED");
                    return;
                }
                System.out.println(nr + " odebrano: " + message);

                if (message.equals("TERMINATED")) {
                    in.close();
                    out.close();
                    socket.close();
                    node.connectionHandlers.remove(this);
                    return;
                }

                String[] messageParts = message.split(SPLIT);
                //Jezeli RETURN to dodanie do wynikow
                if (messageParts[0].equals("RETURN")) {
                    if (isWaiting) {
                        if (isOG) {
                            clientHandler.results.add(message);
                            System.out.println(nr + " dodano do results clienthandler " + message);
                            isOG = false;
                            clientHandler = null;
                        } else {
                            System.out.println(nr + " dodano do results connectionhandler " + message);
                            connectionHandler.results.add(message);
                            connectionHandler = null;
                        }
                        isWaiting = false;
                    } else {
                        System.err.println(nr + " RETURN bez isWaiting " + message);
                        System.out.println(message);
                    }
                } else {
                    String command = messageParts[1];
                    if (!node.taskID.contains(Integer.parseInt(messageParts[0]))) {
                        node.taskID.add(Integer.parseInt(messageParts[0]));
                        String res = null;
                        System.out.println(nr + " started " + command);
                        if (!isOG) node.sendAll(this, message);
                        switch (command) {
                            case "set-value":
                                res = set_value(messageParts);
                                break;
                            case "get-value":
                                res = get_value(messageParts);
                                break;
                            case "find-key":
                                res = find_key(messageParts);
                                break;
                            case "get-max":
                                res = get_max();
                                break;
                            case "get-min":
                                res = get_min();
                                break;
                            default:
                                System.out.println("???????????????????????");
                                System.err.println("???????????????????????");
                        }
                        results = new ArrayList<>();
                        if (res != null) {
                            System.out.println(nr + " wynik: " + res);
                            if (isOG) clientHandler.results.add(res);
                            else out(res);
                        } else {
                            if (isOG) clientHandler.results.add(RETURN_ERROR);
                            else out(RETURN_ERROR);
                        }
                    } else {
                        System.out.println(nr + " bylo");
                        out(RETURN_ERROR);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
                node.connectionHandlers.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Operacje:
    public String set_value(String[] messageParts){
        int key = Integer.parseInt(messageParts[2]);
        int value = Integer.parseInt(messageParts[3]);
        if(node.Key == key){
            node.Value = value;
            return RETURN_OK;
        }else {
            for (String result : results) {
                if (result.equals(RETURN_OK)) return RETURN_OK;
            }
            return RETURN_ERROR;
        }
    }

    public String get_value(String[] messageParts){
        int key = Integer.parseInt(messageParts[2]);
        if(key == node.Key){
            return RETURN+key+SPLIT+node.Value;
        }else{
            for (String result : results) {
                if (!result.equals(RETURN_ERROR)) {
                    return result;
                }
            }
        }
        return RETURN_ERROR;
    }

    public String find_key(String[] messageParts) {
        int key = Integer.parseInt(messageParts[2]);
        if(node.Key == key){
            try {
                return RETURN + socket.getInetAddress().getLocalHost().getHostAddress()+SPLIT+node.tcpport;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }else{
            for (String result : results) {
                if (!result.equals(RETURN_ERROR)) {
                    return result;
                }
            }
        }
        return RETURN_ERROR;
    }

    public String get_max() {
        int max = node.Value;
        int key = node.Key;
        for (String result : results) {
            if (!result.equals(RETURN_ERROR)) {
                int tmp = Integer.parseInt(result.split(SPLIT)[2]);
                if (tmp > max) {
                    max = tmp;
                    key = Integer.parseInt(result.split(SPLIT)[1]);
                }
            }
        }
        return RETURN+key+SPLIT+max;
    }

    public String get_min() {
        int min = node.Value;
        int key = node.Key;
        for (String result : results) {
            if (!result.equals(RETURN_ERROR)) {
                int tmp = Integer.parseInt(result.split(SPLIT)[2]);
                if (tmp < min) {
                    min = tmp;
                    key = Integer.parseInt(result.split(SPLIT)[1]);
                }
            }
        }
        return RETURN+key+SPLIT+min;
    }
    //Wyslanie operacji
    public void send(ConnectionHandler connectionHandler1, String task){
        connectionHandler = connectionHandler1;
        isWaiting = true;
        out(task);
    }
    //Wyslanie operacji jako oryginalny node

    public void og(ClientHandler tmp, String task){
        clientHandler = tmp;
        isOG = true;
        isWaiting = true;
        System.out.println("rozpoczecie og task " + task);
        String res = null;
        switch (task.split(SPLIT)[1]) {
            case "set-value":
                res = set_value(task.split(SPLIT));
                break;
            case "get-value":
                res = get_value(task.split(SPLIT));
                break;
            case "find-key":
                res = find_key(task.split(SPLIT));
                break;
            case "get-max":
                res = get_max();
                break;
            case "get-min":
                res = get_min();
                break;
        }
        if(res!=null){
            clientHandler.results.add(res);
            System.out.println(nr + " dodano do results clienthandler " + res);
        }
        out(task);
    }
}