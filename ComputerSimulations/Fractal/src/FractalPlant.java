import java.awt.*;
import javax.swing.*;
import java.util.Stack;

public class FractalPlant {
    private static final double ANGLE = 25.0; // kąt obrotu
    private static final int ITERATIONS = 8; // liczba iteracji generowania słowa
    private static final String AXIOM = "X"; // słowo początkowe

    private static final String RULE_X = "F+[[X]-X]-F[-FX]+X"; // reguła P1
    private static final String RULE_F = "FF"; // reguła P2

    private static final String SYMBOL_FORWARD = "F";
    private static final String SYMBOL_TURN_LEFT = "+";
    private static final String SYMBOL_TURN_RIGHT = "-";
    private static final String SYMBOL_PUSH = "[";
    private static final String SYMBOL_POP = "]";
    public static String word;
    static TurtleGraphics turtle;

    public static void main(String[] args) {
        turtle = new TurtleGraphics();

        turtle.setAngleIncrement(ANGLE);
        word = generateWord(AXIOM, ITERATIONS);
        System.out.println(word);

        //turtle.setAngleIncrement(ANGLE);
        turtle.draw(word);
    }

    private static String generateWord(String axiom, int iterations) {
        String word = axiom;

        for (int i = 0; i < iterations; i++) {
            StringBuilder nextWord = new StringBuilder();

            for (int j = 0; j < word.length(); j++) {
                char currentSymbol = word.charAt(j);
                if (currentSymbol == 'X') {
                    nextWord.append(RULE_X);
                } else if (currentSymbol == 'F') {
                    nextWord.append(RULE_F);
                } else {
                    nextWord.append(currentSymbol);
                }
            }

            word = nextWord.toString();
            FractalPlant.word = word;
            //turtle.draw(word);
        }

        return word;
    }

    private static class TurtleGraphics extends JPanel {
        private double x;
        private double y;
        private double angle;
        private Stack<Double> xStack;
        private Stack<Double> yStack;
        private Stack<Double> angleStack;

        public TurtleGraphics() {
            x = 0.0;
            y = 0.0;
            angle = 90.0;
            xStack = new Stack<>();
            yStack = new Stack<>();
            angleStack = new Stack<>();

            JFrame frame = new JFrame("Fractal Plant");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.getContentPane().add(this);
            frame.setVisible(true);
        }

        public void setAngleIncrement(double angleIncrement) {
            this.angle = 90.0 - angleIncrement;
        }

        public void draw(String word) {
            Graphics2D g2d = (Graphics2D) getGraphics();
            g2d.setColor(Color.BLACK);

            for (int i = 0; i < word.length(); i++) {
                char currentSymbol = word.charAt(i);

                switch (currentSymbol) {
                    case 'F':
                        double newX = x + Math.cos(Math.toRadians(angle));
                        double newY = y + Math.sin(Math.toRadians(angle));
                        g2d.drawLine((int) x, (int) y, (int) newX, (int) newY);
                        x = newX;
                        y = newY;
                        break;
                    case '+':
                        angle += ANGLE;
                        break;
                    case '-':
                        angle -= ANGLE;
                        break;
                    case '[':
                        xStack.push(x);
                        yStack.push(y);
                        angleStack.push(angle);
                        break;
                    case ']':
                        x = xStack.pop();
                        y = yStack.pop();
                        angle = angleStack.pop();
                        break;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //draw(word);
        }
    }
}
