import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {
    private DatabaseNode node;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public ArrayList<String> results;
    private String first;
    //konstruktor
    public ClientHandler(DatabaseNode node, Socket socket, BufferedReader in, PrintWriter out, String first) {
        this.node = node;
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.first = first;
        results = new ArrayList<>();
        System.out.println("nowy klient podlaczony");
    }

    @Override
    public void run() {
        try {
            boolean check = true;
            String message = first;
            System.out.println("K odebrano " + message);
            String[] messageParts = message.split(" ");
            String task = "";
            for (int i = 0; i < messageParts.length; i++) {
                task += messageParts[i];
                if (i != messageParts.length - 1) task += ":";
            }
            String command = messageParts[0];
            node.taskID.add(node.taskID.get(node.taskID.size()-1)+rand()+1);
            task = node.taskID.get(node.taskID.size()-1) + ":" + task;

            switch (command) {
                case "set-value":
                    System.out.println("wywolano set-value");
                    node.og(this, task);
                    System.out.println("wyniki dla klienta gotowe");
                    for (String result : results) {
                        System.out.println(result);
                        if (!result.equals("RETURN:ERROR")) {
                            out.println(result.split(":")[1]);
                            check = false;
                            break;
                        }
                    }
                    if(check)out.println("ERROR");
                    break;
                case "get-value":
                    System.out.println("wywolano get-value");
                    node.og(this, task);
                    System.out.println("wyniki dla klienta gotowe");
                    for (String result : results) {
                        System.out.println(result);
                        if (!result.equals("RETURN:ERROR")) {
                            System.out.println("result " + result);
                            out.println(result.split(":")[1] + ":" + result.split(":")[2]);
                            check = false;
                            break;
                        }
                    }
                    if(check)out.println("ERROR");
                    break;
                case "find-key":
                    System.out.println("wywolano find-key");
                    node.og(this, task);
                    System.out.println("wyniki dla klienta gotowe");
                    for (int i = 0; i < results.size(); i++) {
                        System.out.println(results.get(i));
                        if (!results.get(i).equals("RETURN:ERROR")) {
                            out.println(results.get(i).split(":")[1] + ":" + results.get(i).split(":")[2]);
                            check=false;
                            break;
                        }
                    }
                    if(check)out.println("ERROR");
                    break;
                case "get-max":
                    System.out.println("wywolano get-max");
                    node.og(this, task);
                    System.out.println("wyniki dla klienta gotowe");
                    int max=-1;
                    for (int i = 0; i < results.size(); i++) {
                        System.out.println(results.get(i));
                        if (!results.get(i).equals("RETURN:ERROR")) {
                            if(max==-1)max=i;
                            else {
                                int tmp = Integer.parseInt(results.get(i).split(":")[2]);
                                if (tmp > Integer.parseInt(results.get(max).split(":")[2])) max = i;
                            }
                        }
                    }
                    if(max!=-1){
                        out.println(results.get(max).split(":")[1] + ":" + results.get(max).split(":")[2]);
                    }
                    else out.println("ERROR");
                    break;
                case "get-min":
                    System.out.println("wywolano get-min");
                    node.og(this, task);
                    System.out.println("wyniki dla klienta gotowe");
                    int min =-1;
                    for (int i = 0; i < results.size(); i++) {
                        System.out.println(results.get(i));
                        if (!results.get(i).equals("RETURN:ERROR")) {
                            if(min==-1)min=i;
                            else {
                                int tmp = Integer.parseInt(results.get(i).split(":")[2]);
                                if (tmp < Integer.parseInt(results.get(min).split(":")[2])) min = i;
                            }
                        }
                    }
                    if(min!=-1){
                        out.println(results.get(min).split(":")[1] + ":" + results.get(min).split(":")[2]);
                    }
                    else out.println("ERROR");
                    break;
                case "new-record":
                    node.Key = Integer.parseInt(messageParts[1].split(":")[0]);
                    node.Value = Integer.parseInt(messageParts[1].split(":")[1]);
                    out.println("OK");
                    break;
                case "terminate":
                    out.println("OK");
                    node.terminate();
                    try {
                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                default:
                    System.out.println("????");
                    System.err.println("????");
                    break;
            }
            node.waiting = false;
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public int rand(){
        return (int)(Math.random()*100%50);
    }
}
