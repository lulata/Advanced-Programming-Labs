package NP.lab8.ex3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


interface XMLComponent{
    String print(int indent);
    void addAttribute(String attrname, String attrValue);
    void addComponent(XMLComponent xmlComponent);

}

class XMLComposite implements XMLComponent{
    List<XMLComponent> componentList;
    Map<String, String> attrs;
    String name;
    public XMLComposite(String name) {
        componentList = new ArrayList<>();
        attrs = new LinkedHashMap<>();
        this.name = name;
    }

    @Override
    public String print(int indent) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= indent; i++)
            sb.append("\t");
        String str = "";
        for(Map.Entry<String, String> entry: attrs.entrySet()){
            str += " " + entry.getKey();
            str += "=\"";
            str += entry.getValue();
            str += "\"";
        }
        sb.append("<" + name + str + ">\n");
        componentList.forEach(xmlComponent -> sb.append(xmlComponent.print(indent + 1)));
        for(int i = 1; i <= indent; i++)
            sb.append("\t");
        sb.append("</" + name + ">\n");
        return  sb.toString();
    }

    @Override
    public void addAttribute(String attrname, String attrValue) {
        attrs.put(attrname, attrValue);
    }

    @Override
    public void addComponent(XMLComponent xmlComponent) {
        componentList.add(xmlComponent);
    }
}
class XMLLeaf implements XMLComponent{
    String name;
    String text;
    Map<String, String> attrs;
    public XMLLeaf(String name, String text) {
        this.name = name;
        this.text = text;
        attrs = new LinkedHashMap<>();
    }

    @Override
    public String print(int indent) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <=indent; i++)
            sb.append("\t");
        String str = "";
        for(Map.Entry<String, String> entry: attrs.entrySet()){
            str += " " + entry.getKey();
            str += "=\"";
            str += entry.getValue();
            str += "\"";
        }
        sb.append("<" + name + str + ">" + text + "</" + name + ">\n");
        return sb.toString();
    }

    @Override
    public void addAttribute(String attrname, String attrValue) {
        attrs.put(attrname, attrValue);
    }

    @Override
    public void addComponent(XMLComponent xmlComponent) {

    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase==1) {
            //TODO Print the component object
            System.out.println(component.print(0));
        } else if(testCase==2) {
            //TODO print the composite object
            System.out.println(composite.print(0));
        } else if (testCase==3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level","1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level","2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level","3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            //TODO print the main object
            System.out.println(main.print(0));
        }
    }
}

