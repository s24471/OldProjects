import java.util.ArrayList;

public class Rules {
    ArrayList<Integer> alive;
    ArrayList<Integer> dead;

    public Rules() {
        alive = new ArrayList<>();
        dead = new ArrayList<>();
    }

    public void setup(String s){
        for(String i: s.split("/")[0].split("")){
            alive.add(Integer.parseInt(i));
        }
        for(String i: s.split("/")[1].split("")){
            dead.add(Integer.parseInt(i));
        }
    }
    public boolean isAlive(int n, boolean a){
        return a?alive.contains(n):dead.contains(n);
    }
}
