package NP.MidTerm1.zad2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}

class Square {
    private int side;
    private int perimeter;

    public Square(String side) {
        this.side = Integer.parseInt(side);
        this.perimeter = 4 * this.side;
    }

    public int getPerimeter() {
        return perimeter;
    }
}

class Canvas {
    private List<Square> squares;
    private String canvasID;


    public Canvas(String line) {
        this.squares = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(line, " ");
        this.canvasID = st.nextToken();
        while (st.hasMoreTokens()) {
            squares.add(new Square(st.nextToken()));
        }
    }

    public int getNumberOfSquares() {
        return squares.size();
    }

    public int getTotalPerimeter() {
        int totalPerimeter = 0;
        for (Square square : squares) {
            totalPerimeter += square.getPerimeter();
        }
        return totalPerimeter;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", canvasID, squares.size(), getTotalPerimeter());
    }
}

class ShapesApplication{
    List<Canvas> canvases;

    public ShapesApplication() {
    }

    public int readCanvases(InputStream in) {
        BufferedReader bread = new BufferedReader (new InputStreamReader(in));
        canvases = new LinkedList<>();
        String line;
        try {
            while ((line = bread.readLine()) != null) {
                canvases.add(new Canvas(line));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }int sum=0;
        for (Canvas canvas : canvases) {
            sum+=canvas.getNumberOfSquares();
        }
        return sum;
    }

    public void printLargestCanvasTo(PrintStream out) {
        Canvas largestCanvas = null;
        for (Canvas canvas : canvases) {
            if (largestCanvas == null || canvas.getTotalPerimeter() > largestCanvas.getTotalPerimeter()) {
                largestCanvas = canvas;
            }
        }
        out.println(largestCanvas);
    }
}
