import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

class Path {
    ArrayList<Node> nodes;

    Path() {
        this.nodes = new ArrayList<>();
    }

    Path(ArrayList<Node> newNodes) {
        this.nodes = new ArrayList<>(newNodes);
    }

    @Override
    public String toString() {
        return nodes.toString();
    }

    int caluculateLength() {
        int ret = 0;
        for (Node node : nodes) {
            if (node.value > 0) {
                ret += node.value;
            }
        }
        return ret;
    }

    int calculateMoves() {
        int ret = 0;
        for (int i = 0; i < nodes.size(); i++) {
            ret++;
        }
        return ret;
    }
}

class Maze {
    Node[][] field;
    int numOfTresures;

    private int tresures() {
        int temp = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].value == -3) {
                    temp++;
                }
            }
        }
        return temp;
    }

    Maze(String fileName) throws FileNotFoundException {
        this.field = readLabyrinthFile(fileName);
        this.numOfTresures = tresures();
    }

    private void resetVisited() {
        Node node;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                node = field[i][j];
                node.visited = false;
                ColorNode(node, Color.WHITE);
                switch(node.value) {
                    case -1:
                        ColorNode(node, Color.BLACK);                        
                        break;
                    case -2:
                        ColorNode(node, Color.RED);
                        break;
                    case -3:
                        ColorNode(node, Color.YELLOW);
                        break;
                    case -4:
                        ColorNode(node, Color.GREEN);
                        break;
                    default:
                        ColorNode(node, Color.WHITE);
                        break;
                }
            }
        }
    }


    public Node[] getAdjacentNodes(Node center) {
        Node[] nodes = new Node[4];
        int x = center.x;
        int y = center.y;

        nodes[0] = field[x][y + 1];
        nodes[1] = field[x + 1][y];
        nodes[2] = field[x][y - 1];
        nodes[3] = field[x - 1][y];

        return nodes;        
    }

    private Node[][] readLabyrinthFile(String fileName) throws FileNotFoundException {
    
        File file = new File(fileName);
        Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));

        int rows = 0;
        int cols = -1;       

        while(sc.hasNextLine()) {
            if (cols == -1) {
                String[] line = sc.nextLine().trim().split(",");
                cols = line.length;
            } else {
                sc.nextLine();
            }
            rows++;
        }

        sc = new Scanner(new BufferedReader(new FileReader(file)));

        Node[][] arr = new Node[rows][cols];

        while (sc.hasNextLine()) {
            for (int i = 0; i < arr.length; i++) {
                String[] line = sc.nextLine().trim().split(",");

                for (int j = 0; j < line.length; j++) {
                    arr[i][j] = new Node(i, j, Integer.valueOf(line[j]));
                }
            }
        }
        return arr;
    }

    public void draw() {
        StdDraw.setCanvasSize(700, 700);
        StdDraw.setFont(new Font(null, Font.PLAIN, (int) (16 - (double) (field.length / 8))));

        int fieldLength = field.length;

        for (int i = 0; i < fieldLength; i++) {
            for (int j = 0; j < field[0].length; j++) {
                switch (field[i][j].value) {
                    case -1:
                        StdDraw.setPenColor(Color.BLACK);
                        break;
                    case -2:
                        StdDraw.setPenColor(Color.RED);
                        break;
                    case -3:
                        StdDraw.setPenColor(Color.YELLOW);
                        break;
                    case -4:
                        StdDraw.setPenColor(Color.GREEN);
                        break;
                    default:
                        StdDraw.setPenColor(Color.WHITE);
                        break;
                }
                StdDraw.filledSquare((double) j / (fieldLength - 1), 1- (double) i / (fieldLength -1),
                        (double) 1 / (fieldLength - 1) * 0.5);
                if (field[i][j].value >= 0) {
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.text((double) j / (fieldLength - 1), 1 - (double) i / (fieldLength - 1),
                            String.valueOf(field[i][j].value));
                }
            }
        }
    }

    private void ColorNode(Node node, Color color) {
        int fieldLength = field.length;

        StdDraw.setPenColor(color);
        StdDraw.filledSquare((double) node.y / (fieldLength - 1), 1- (double) node.x / (fieldLength -1),
                        (double) 1 / (fieldLength - 1) * 0.5);
    }

    Node getStart() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].value == -2) {
                    return field[i][j];
                }
            }
        }

        return null;
    }

    Node getEnd() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].value == -4) {
                    return field[i][j];
                }
            }
        }
        return null;
    }


    public Path DFS(Node current, Path path, int target) throws InterruptedException {
        current.visited = true;

        if (current.value == target) {                  
            return path;
        }
        
        Node[] nodes = getAdjacentNodes(current);

        for (Node node : nodes) {
            if (node.value != -1 && !node.visited) {
                ColorNode(current, Color.BLUE);
                path.nodes.add(node);
                Path retPath = DFS(node, path, target);
                if (!retPath.nodes.isEmpty()) {
                    return retPath;
                }
            }
        }

        ColorNode(current, Color.WHITE);
        System.out.println("backtracking");
        return new Path();
    }

    public ArrayList<Path> getPath_DFS() throws InterruptedException {
        Node start = getStart();
        ArrayList<Path> allPaths = new ArrayList<>();

        while (numOfTresures != 0) {
            Path path = new Path();
           
            path = DFS(start, path, -3);
            allPaths.add(path);
            start = path.nodes.get(path.nodes.size() - 1);
            resetVisited();
            start.value = 0;
            numOfTresures--;
        }
        Path path = new Path();
        allPaths.add(DFS(start, path, -4));
        return allPaths;
    }

    public Path BFS(Node root, int target) throws InterruptedException {
        ArrayDeque<Path> q = new ArrayDeque<>();
        Path path = new Path();

        root.visited = true;
        path.nodes.add(root);
        q.add(path);

        while (!q.isEmpty()) {
            path = q.poll();
            Node currentNode = path.nodes.get(path.nodes.size() - 1);
            
            if (currentNode.value == target) {
                for (Node node : path.nodes) {
                    ColorNode(node, Color.PINK);
                }
                return path;
            }
            for (Node node : getAdjacentNodes(currentNode)) {
                if (node.value != -1 && !node.visited) {
                    node.visited = true;
                    ColorNode(node, Color.BLUE);

                    Path new_path = new Path(path.nodes);
                    new_path.nodes.add(node);
                    q.add(new_path);
                }
            }
            
        }
        return null;
    }

    public ArrayList<Path> getPaths_BFS() throws InterruptedException {
        Node start = getStart();
        ArrayList<Path> allPaths = new ArrayList<>();

        while (numOfTresures != 0) {
            Path path = new Path();
           
            path = BFS(start, -3);
            allPaths.add(path);
            start = path.nodes.get(path.nodes.size() - 1);
            resetVisited();
            start.value = 0;
            numOfTresures--;
        }
        allPaths.add(BFS(start, -4));
        return allPaths;
    }

    private int euclideanDistance(Node start, Node end) {
        return (int)Math.sqrt(Math.pow((double)start.x - (double)end.x, 2) + Math.pow((double)start.y - (double)end.y, 2));
    }

    public int aStar() {
        Node start = getStart();
        Node end = getEnd();
        return euclideanDistance(start, end);
    }
}

class Node {
    int x, y, value;
    boolean visited = false;

    Node(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("x: ");
        sb.append(x);
        sb.append(" y: ");
        sb.append(y);
        sb.append(" value: ");
        sb.append(value);
        sb.append("\n");
        return sb.toString();
    }
}


class main {

        
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        Maze maze = new Maze("../labirinti/labyrinth_2.txt");
        maze.draw();

        // ArrayList<Path> paths = maze.getPaths_BFS();

        // int skupnaCena = 0;
        // int skupnoPremikov = 0;

        // for (Path path : paths) {
        //     skupnaCena += path.caluculateLength();
        //     skupnoPremikov += path.calculateMoves();
        // }
        // System.out.printf("cena: %d, premiki: %d", skupnaCena, skupnoPremikov);

        System.out.println(maze.aStar());
        
    }    
}
