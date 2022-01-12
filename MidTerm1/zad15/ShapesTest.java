package NP.MidTerm1.zad15;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

enum Color {
    RED, GREEN, BLUE
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

abstract class Form implements Scalable, Stackable {
    protected String id;
    protected Color color;

    public Form(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    @Override
    public void scale(float scaleFactor) {
    }

    @Override
    public float weight() {
        return 0;
    }

    public String getId() {
        return id;
    }
}

class Circle extends Form {
    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public float weight() {
        return radius * radius * (float) Math.PI;
    }

    @Override
    public void scale(float scaleFactor) {
        radius = radius * scaleFactor;
    }

    @Override
    public String toString() {
        return String.format("%c: %-5s%-10s%10.2f\n", 'C',id,color,weight());
    }

}

class Rectangle extends Form {
    protected float width;
    protected float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public void scale(float scaleFactor) {
        width = width * scaleFactor;
        height = height * scaleFactor;
    }

    @Override
    public String toString() {
        return String.format("%c: %-5s%-10s%10.2f\n", 'R',id,color,weight());
    }
}

class Canvas {
    List<Form> canvas;

    public Canvas()
    {
        canvas=new ArrayList<>();
    }

    private int indexToAdd(float weight)
    {
        if(canvas.size()==0)
            return 0;
        for(int i=0;i<canvas.size();i++){
            if(weight>canvas.get(i).weight()){
                return i;
            }
        }
        return canvas.size();
    }

    public void add(String id , Color color , float radius)
    {
        Form circle=new Circle(id,color,radius);
        int index=indexToAdd(circle.weight());
        canvas.add(index,circle);
    }

    public void add(String id, Color color , float width , float height)
    {
        Form rectangle=new Rectangle(id,color,width,height);
        int index=indexToAdd(rectangle.weight());
        canvas.add(index,rectangle);
    }
    public void scale(String id , float scaleFactor)
    {
        Form tempForm=null;

        for(Form form:canvas)
        {
            if(form.getId().equals(id)){
                tempForm=form;
                break;
            }
        }
        canvas.remove(tempForm);
        tempForm.scale(scaleFactor);
        int index=indexToAdd(tempForm.weight());
        canvas.add(index,tempForm);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Form form : canvas) {
            sb.append(form.toString());
        }
        return sb.toString();
    }
}
