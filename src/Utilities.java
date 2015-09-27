import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class Utilities {

    /*
     *   Validates if the given string is a number.
     */
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    /*
     *  Supposed to remove any special characters in String.
     *  But this method will remove ",","$" and "%" from the String.
     */
    public static String removeSpecialChars(String inputString){
        if(inputString==null)
            return null;
        return removeSpecialCharsUtil(inputString);
    }

    /*
     *   Converts Number in String format to Integer format.
     */
    public static int getInteger(String s){
        return getIntegerUtil(s);
    }

    public static void printList(LinkedList<String> list,String result){
        if(list!=null)
            printListUtil(list, result);
    }

    private static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    private static String removeSpecialCharsUtil(String inputString){
        return inputString.replaceAll("[$,%]", "");
    }

    private static int getIntegerUtil(String s) {
        return Integer.parseInt(s);
    }

    private static void printListUtil(LinkedList<String> list,String result){
        Iterator<String> it = list.iterator();
        try {
            FileWriter fw = new FileWriter(new File(result));
            while(it.hasNext()){
                fw.write(it.next());
                fw.write(System.lineSeparator());
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
