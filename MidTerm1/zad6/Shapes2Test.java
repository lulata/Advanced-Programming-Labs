package NP.MidTerm1.zad6;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum typeShape {
    CIRCLE,
    SQUARE
}

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String ID, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", ID, maxArea));
    }
}

abstract class Shape {
    protected int length;

    public Shape() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = Integer.parseInt(length);
    }

    public abstract typeShape getType();

    public abstract double getL();

    public abstract double getA();
}

class Square extends Shape {

    public Square() {
    }

    @Override
    public typeShape getType() {
        return typeShape.SQUARE;
    }

    @Override
    public double getL() {
        return length * 4;
    }

    @Override
    public double getA() {
        return Math.pow(length, 2);
    }
}

class Circle extends Shape {

    public Circle() {
    }

    @Override
    public typeShape getType() {
        return typeShape.CIRCLE;
    }

    @Override
    public double getL() {
        return 2 * Math.PI * length;
    }

    @Override
    public double getA() {
        return Math.PI * Math.pow(length, 2);
    }
}

class Canvas implements Comparable<Canvas> {
    private List<Shape> shapeList;
    private String ID;

    public String getID() {
        return ID;
    }

    public Canvas() {
        this.shapeList = new LinkedList<>();
        this.ID = "";
    }

    public Canvas(List<Shape> shapeList, String ID) {
        this.shapeList = shapeList;
        this.ID = ID;
    }

    public static Canvas createCollectionOfShapes(String s, double maxArea) throws IrregularCanvasException {
        String[] arrayStrings = s.split("\\s+");
        String id = arrayStrings[0];
        List<Shape> shapeList = new LinkedList<>();

        Arrays.stream(arrayStrings).skip(1)
                .forEach(x -> {
                            if (Character.isAlphabetic(x.charAt(0))) {
                                if (x.charAt(0) == 'S') {
                                    shapeList.add(new Square());
                                } else if (x.charAt(0) == 'C') {
                                    shapeList.add(new Circle());
                                }
                            } else {
                                shapeList.get(shapeList.size() - 1).setLength(x);
                            }


                        }
                );


        if (shapeList.stream().anyMatch(x -> x.getA() > maxArea)) {
            throw new IrregularCanvasException(id, maxArea);
        }

        return new Canvas(shapeList, id);
    }

    public int getSquareCount() {
        return shapeList.size();
    }

    public double getSumPerimeter() {
        return shapeList.stream().mapToDouble(Shape::getL).sum();
    }

    public double getSumArea() {
        return shapeList.stream().mapToDouble(Shape::getA).sum();
    }

    public static double getSumArea(List<Shape> shapeList) {
        return shapeList.stream().mapToDouble(Shape::getA).sum();
    }

    public int totalCircles() {
        return (int) shapeList.stream().filter(x -> x.getType().equals(typeShape.CIRCLE)).count();
    }

    public int totalSquares() {
        return (int) shapeList.stream().filter(x -> x.getType().equals(typeShape.SQUARE)).count();
    }

    @Override
    public int compareTo(Canvas o) {
        return Double.compare(getSumArea(), o.getSumArea());
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics dss =
                shapeList.stream()
                        .mapToDouble(Shape::getA)
                        .summaryStatistics();


        return String.format("%s %d %d %d %.2f %.2f %.2f", ID, dss.getCount(), totalCircles(), totalSquares(), dss.getMin(), dss.getMax(), dss.getAverage());
    }
}

class ShapesApplication {

    private List<Canvas> canvasList;
    private double maxArea;

    ShapesApplication(double maxArea) {
        canvasList = new LinkedList<>();
        this.maxArea = maxArea;
    }

    void readCanvases(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        canvasList = bufferedReader.lines()
                .map(s -> {
                    try {
                        return Canvas.createCollectionOfShapes(s, maxArea);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }

                })
                .collect(Collectors.toList());

        canvasList = canvasList.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

    public void printCanvases(OutputStream os) {
        PrintWriter printWriter = new PrintWriter(os);
        canvasList.stream().sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }
}

public class Shapes2Test {


    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
