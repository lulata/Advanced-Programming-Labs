package NP.MidTerm2.lab4;

import java.util.*;

class Component {
    private String color;
    private int weight;
    private Set<Component> innerComponents;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.innerComponents = new TreeSet<>(Comparator.comparing(Component::getWeight).thenComparing(Component::getColor));
    }

    void addComponent(Component component) {
        innerComponents.add(component);
    }

    @Override
    public String toString() {
        return print(0);
    }

    private String print(int indent) {
        StringBuilder sb = new StringBuilder();
        int temp = indent;
        while (temp > 0) {
            sb.append("-");
            temp--;
        }
        sb.append(weight).append(":").append(color).append("\n");
        for (Component c : innerComponents) {
            sb.append(c.print(indent + 3));
        }
        return sb.toString();
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void tryChangingColor(int weight, String color) {
        if (this.weight < weight) {
            this.setColor(color);
        }
        innerComponents.forEach(c -> c.tryChangingColor(weight, color));
    }
}

class Window {
    String name;
    Map<Integer, Component> components;

    public Window(String name) {
        this.name = name;
        components = new TreeMap<>();
    }

    void addComponent(int position, Component component) throws InvalidPositionException {
        if (components.containsKey(position)) {
            throw new InvalidPositionException(position);
        } else {
            components.put(position, component);
        }
    }

    void changeColor(int weight, String color) {
        components.values().forEach(c -> c.tryChangingColor(weight, color));
    }


    void switchComponents(int pos1, int pos2) {
        Component temp = components.get(pos1);
        components.put(pos1, components.get(pos2));
        components.put(pos2, temp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW ").append(name).append("\n");
        components.keySet().stream()
                .map(position -> String.format("%d:%s",
                        position, components.get(position)))
                .forEach(sb::append);
        return sb.toString();
    }
}

class InvalidPositionException extends Exception {
    public InvalidPositionException(int position) {
        super(String.format("Invalid position %d, alredy taken!", position));
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.switchComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде
