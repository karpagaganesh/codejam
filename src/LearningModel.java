import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class LearningModel {

    private static Utilities utility = new Utilities();

    public static String[] getModel(PdfReader reader) throws IOException{
        if(reader!=null){
            return getModelUtil(reader);
        }
        return null;
    }

    private static String[] getModelUtil(PdfReader reader)  throws IOException {
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        TextExtractionStrategy strategy;
        HashMap<String,Integer> modelRanking = new HashMap<String,Integer>();
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            String page = strategy.getResultantText();
            String s2[] = page.split("\n");
            for (int j = 1; j < s2.length; j++) {
                String[] parsedString = s2[j].trim().split(" ");
                ArrayList<String> dataTypeModel = getDataType(parsedString);
                if (dataTypeModel.size() > 2 && dataTypeModel.get(0) == "Int" && (dataTypeModel.get(1) == "Int" ||
                        dataTypeModel.get(2) == "Int") && noContinuousStrings(dataTypeModel)) {
                    int value = 1;
                    if (modelRanking.containsKey(Arrays.toString(dataTypeModel.toArray()))) {
                        value = modelRanking.get(Arrays.toString(dataTypeModel.toArray())) + 1;
                    }
                    modelRanking.put(Arrays.toString(dataTypeModel.toArray()), value);
                }
            }
        }
        String highestRankingModel = getHighestRankingModel(modelRanking);
        String[] parsedString = highestRankingModel.substring(1,highestRankingModel.length()-1).trim().split(",");
        return parsedString;
    }

    private static String getHighestRankingModel(HashMap<String,Integer> modelRanking){
        int max = Integer.MIN_VALUE;
        Iterator<Map.Entry<String,Integer>> it = modelRanking.entrySet().iterator();
        String result = null;
        while(it.hasNext()){
            Map.Entry<String,Integer> m = (Map.Entry<String,Integer>)it.next();
            String key = m.getKey();
            int value = m.getValue();
            if(value>max){
                max = value;
                result = key;
            }
        }
        return result;
    }

    private static ArrayList<String> getDataType(String[] parsedString){
        ArrayList<String> type = new ArrayList<>();
        for(int i =0;i<parsedString.length;i++){
            if(utility.isInteger(utility.removeSpecialChars(parsedString[i].trim()))){
                type.add("Int");
            }
            else{
                type.add("String");
            }
        }
        return type;
    }

    private static boolean noContinuousStrings(ArrayList<String> dataTypeModel){

        boolean flag = false;

        if(dataTypeModel!=null){
            String prev = dataTypeModel.get(0);
            for(int i=1;i<dataTypeModel.size();i++){
                if(prev==dataTypeModel.get(i)&&prev=="String"){
                    return  false;
                }
                prev = dataTypeModel.get(i);
            }
            flag = true;
        }

        return true; // Note: Forcing to return True. Need to work more on analyzing this assumption.
//      return flag;
    }
}
