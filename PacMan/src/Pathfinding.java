import java.awt.*;
import java.util.*;
import java.util.List;

public class Pathfinding {
    private Tile[][] grid;
    private int numRows;
    private int numCols;
    private Point[] directions = {new Point(-1, 0), new Point(0, -1), new Point(1, 0), new Point(0, 1)};

    public Pathfinding(Tile[][] grid) {
        this.grid = grid;
        numRows = grid.length;
        numCols = grid[0].length;
    }

    public List<Point> findPath(Point start, Point goal) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fScore));
        Map<Point, Node> nodeMap = new HashMap<>();
        Set<Point> closedSet = new HashSet<>();

        Node startNode = new Node(start, 0, calculateHeuristic(start, goal), null);
        openSet.add(startNode);
        nodeMap.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.position.equals(goal)) {
                return reconstructPath(current);
            }

            closedSet.add(current.position);

            for (Point direction : directions) {
                Point neighbor = new Point(current.position.x + direction.x, current.position.y + direction.y);

                if (!isValidPosition(neighbor) || closedSet.contains(neighbor) || grid[neighbor.y][neighbor.x].type != 0) {
                    continue;
                }

                int tentativeGScore = current.gScore + 1;
                Node neighborNode = nodeMap.get(neighbor);

                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, tentativeGScore, calculateHeuristic(neighbor, goal), current);
                    nodeMap.put(neighbor, neighborNode);
                    openSet.add(neighborNode);
                } else if (tentativeGScore < neighborNode.gScore) {
                    neighborNode.gScore = tentativeGScore;
                    neighborNode.fScore = tentativeGScore + calculateHeuristic(neighbor, goal);
                    neighborNode.parent = current;
                    openSet.remove(neighborNode);
                    openSet.add(neighborNode);
                }
            }
        }

        return null;
    }

    private boolean isValidPosition(Point position) {
        int x = position.x;
        int y = position.y;
        return x >= 0 && x < numCols && y >= 0 && y < numRows;
    }

    private int calculateHeuristic(Point start, Point goal) {
        return Math.abs(start.x - goal.x) + Math.abs(start.y - goal.y);
    }

    private List<Point> reconstructPath(Node goalNode) {
        List<Point> path = new ArrayList<>();
        Node current = goalNode;

        while (current != null) {
            path.add(0, current.position);
            current = current.parent;
        }

        return path;
    }

    private static class Node {
        private Point position;
        private int gScore;
        private int fScore;
        private Node parent;

        public Node(Point position, int gScore, int hScore, Node parent) {
            this.position = position;
            this.gScore = gScore;
            this.fScore = gScore + hScore;
            this.parent = parent;
        }
    }
}
