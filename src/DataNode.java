import java.util.ArrayList;

public class DataNode {

    private ArrayList<String> csvElements = new ArrayList<>();  // Tax table entries in CSV format.

    public DataNode(ArrayList<String> elements){
        csvElements = elements;
    }

    public ArrayList<String> getCSVElements(){
        return csvElements;
    }

}
