import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj zasady:");
        Rules rules = new Rules();
        rules.setup(scanner.nextLine());
        System.out.println("Jaka szerokość planszy?");
        int szer = scanner.nextInt();
        System.out.println("Jaka wysokość planszy?");
        int dl = scanner.nextInt();
        BoardFrame boardFrame = new BoardFrame(szer, dl, rules);
        boardFrame.setSize(800, 800);
    }
}