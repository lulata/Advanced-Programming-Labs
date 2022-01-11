package NP.lab2.ex3;

import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {
    ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotFittableException extends Exception {
    Movable movable;

    public MovableObjectNotFittableException(Movable movable) {
        this.movable = movable;
    }

    @Override
    public String getMessage() {
        StringBuilder s = new StringBuilder();
        if (movable instanceof MovingPoint) {
            MovingPoint temp = (MovingPoint) movable;
            s.append("Movable point with coordinates (" + temp.getCurrentXPosition() + "," + temp.getCurrentYPosition() + ")");
        } else if (movable instanceof MovingCircle) {
            MovingCircle temp = (MovingCircle) movable;
            s.append("Movable circle with center (" + temp.getCurrentXPosition() + "," + temp.getCurrentYPosition() + ") and radius "+ temp.getRadius() + " can not be fitted into the collection");
        }
        return s.toString();
    }
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();

    TYPE getType();
}

class MovingPoint implements Movable {
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private final TYPE type = TYPE.POINT;

    public MovingPoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y + ySpeed > MovablesCollection.maxY) {
            throw new ObjectCanNotBeMovedException(x, y+ySpeed);
        }
        y += ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < MovablesCollection.minY) {
            throw new ObjectCanNotBeMovedException(x, y-ySpeed);
        }
        y -= ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x + xSpeed > MovablesCollection.maxX) {
            throw new ObjectCanNotBeMovedException(x+xSpeed, y);
        }
        x += xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < MovablesCollection.minX) {
            throw new ObjectCanNotBeMovedException(x-xSpeed, y);
        }
        x -= xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    public TYPE getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")";
    }
}

class MovingCircle implements Movable {
    private int radius;
    private MovingPoint center;
    private final TYPE type = TYPE.CIRCLE;

    public MovingCircle(int radius, MovingPoint center) {
        this.radius = radius;
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public TYPE getType() {
        return type;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (center.getCurrentYPosition() + radius < 0 || center.getCurrentYPosition() + radius > MovablesCollection.maxY) {
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(), center.getCurrentYPosition());
        }
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (center.getCurrentYPosition() + radius < 0 || center.getCurrentYPosition() + radius > MovablesCollection.maxY) {
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(), center.getCurrentYPosition());
        }
        center.moveDown();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (center.getCurrentXPosition() + radius < 0 || center.getCurrentXPosition() + radius > MovablesCollection.maxX) {
            throw new ObjectCanNotBeMovedException(getCurrentXPosition(), getCurrentYPosition());
        }
        center.moveLeft();
    }


    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (center.getCurrentXPosition() + radius < 0 || center.getCurrentXPosition() + radius > MovablesCollection.maxX) {
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(), center.getCurrentYPosition());
        }
        center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + getCurrentXPosition() + "," + getCurrentYPosition() + ") and radius " + getRadius();
    }
}

class MovablesCollection {
    private Movable[] movables;
    public static int maxX = 0;
    public static int maxY = 0;
    public static final int minX = 0;
    public static final int minY = 0;
    private int movabelsCount;

    MovablesCollection(int x_MAX, int y_MAX) {
        maxX = x_MAX;
        maxY = y_MAX;
        this.movabelsCount=0;
    }

    public static void setMaxX(int maxX) {
        MovablesCollection.maxX = maxX;
    }

    public static void setMaxY(int maxY) {
        MovablesCollection.maxY = maxY;
    }

    private boolean isInBounds(Movable m) {
        int x = m.getCurrentXPosition();
        int y = m.getCurrentYPosition();
        if (m instanceof MovingCircle) {
            int r = ((MovingCircle) m).getRadius();
            return ((x - r) >= minX&&(x + r) <= maxX&&(y - r) >= minY&&(y + r) <= maxY);
        } else {
            return (x >= minX&&x <= maxX && y >= minY && y <= maxY);
        }
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (!isInBounds(m)) {
            throw new MovableObjectNotFittableException(m);
        }
        if (movabelsCount == 0) {
            movables = new Movable[1];
            movables[movabelsCount++] = m;
        } else {
            movables = Arrays.copyOf(movables, movables.length + 1);
            movables[movables.length - 1] = m;
        }
    }

    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for (Movable m : movables) {
            if (m.getType() == type){
                switch (direction) {
                    case UP :
                        try {
                            m.moveUp();
                        } catch (ObjectCanNotBeMovedException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case DOWN:
                        try {
                            m.moveDown();
                        } catch (ObjectCanNotBeMovedException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case LEFT:
                        try {
                            m.moveLeft();
                        } catch (ObjectCanNotBeMovedException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case RIGHT:
                        try {
                            m.moveRight();
                        } catch (ObjectCanNotBeMovedException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Collection of movable objects with size "  + movables.length + ":\n");
        for (Movable m : movables) {
            s.append(m);
            s.append("\n");
        }
        return s.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try{
                    collection.addMovableObject(new MovingPoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException m) {
                    System.out.println(m.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try{
                    collection.addMovableObject(new MovingCircle(radius, new MovingPoint(x, y, xSpeed, ySpeed)));
                }catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setMaxX(90);
        MovablesCollection.setMaxY(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}

