import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class pdfToCsv {

        private static Utilities utility = new Utilities();
        private static LearningModel learningModel = new LearningModel();
        private void parsePdf(String pdf, String result) throws IOException {
            PdfReader reader = new PdfReader(pdf);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            TextExtractionStrategy strategy;
            BinarySearchTree tree = new BinarySearchTree();
            String[] expectedPattern = learningModel.getModel(reader);

            boolean needsBreakDownOfColumns = false;
            int breakDownFactor = 0;
            if(expectedPattern.length>9){                   // Based on assumption that, tax tables will always have less than 9 columns.
                needsBreakDownOfColumns = true;             //
                breakDownFactor = expectedPattern.length/4;
            }

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                        strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                        String page = strategy.getResultantText();
                        String s2[]=page.split("\n");
                        for(int j=1;j<s2.length;j++){
                            String[] parsedString = s2[j].trim().split(" ");
                            if(needsBreakDownOfColumns){
                                String[] pString,eString;
                                for(int origIndex=0;origIndex<expectedPattern.length && origIndex<parsedString.length;){

                                    pString = new String[expectedPattern.length/breakDownFactor];
                                    eString = new String[expectedPattern.length/breakDownFactor];
                                    for(int k=0;k<expectedPattern.length/breakDownFactor && origIndex<parsedString.length;k++,origIndex++){
                                        pString[k] = parsedString[origIndex];
                                        eString[k] = expectedPattern[origIndex];
                                    }

                                    DataNode infoNode = processString(pString,eString);
                                    if(infoNode!=null) {
                                        String CSVFormat = extractInCSVFormat(infoNode);
                                        tree.insert(utility.getInteger(infoNode.getCSVElements().get(0)),CSVFormat);
                                    }
                                }
                            }
                            else{
                                DataNode infoNode = processString(parsedString,expectedPattern);
                                if(infoNode!=null) {
                                    String CSVFormat = extractInCSVFormat(infoNode);
                                    tree.insert(utility.getInteger(infoNode.getCSVElements().get(0).trim()),CSVFormat);
                                }
                            }
                        }
            }
            LinkedList<String> list = tree.inOrderTraversal();
            if(list!=null){
                utility.printList(list, result);
            }
            reader.close();
        }

        private static DataNode processString(String[] parsedString,String[] expectedPattern){
            if(parsedString!=null && expectedPattern!= null && parsedString.length==expectedPattern.length ){
                if(!isExpectedPatterMatchingWithParsedString(parsedString,expectedPattern))
                    return null;
                else
                    try{
                        ArrayList<String> csvElements = new ArrayList<>();
                        for(int i=0;i<parsedString.length;i++){
                            String temp = utility.removeSpecialChars(parsedString[i]);
                            if(!temp.equalsIgnoreCase("-")&&!temp.equalsIgnoreCase("")&&!temp.equalsIgnoreCase("–")) {
                                csvElements.add(temp);
                            }
                        }
                        return new DataNode(csvElements);
                    }catch (Exception ex){
                        ex.printStackTrace();
                        return null;
                    }
            }
            return null;
        }

        private static boolean isExpectedPatterMatchingWithParsedString(String[] parsedString,String[] expectedPattern){
            boolean flag = true;
            for(int i=0;i<parsedString.length;i++) {
                if(expectedPattern[i]==null||parsedString[i]==null) {
                    flag = flag && false;
                }
                else if(expectedPattern[i].trim().equalsIgnoreCase("Int")) {
                    flag = flag && utility.isInteger(utility.removeSpecialChars(parsedString[i]).trim());
                }
                else {
                    flag = flag && !utility.isInteger(utility.removeSpecialChars(parsedString[i]));
                }
            }
            return flag;
        }

        private static String extractInCSVFormat(DataNode data){
            StringBuilder csv = new StringBuilder();
            ArrayList<String> elements = data.getCSVElements(); // Convert tax table rows to CSV String.
            for(int i=0;i<elements.size();i++){
                csv.append(elements.get(i));
                if(i+1!=elements.size())
                    csv.append(",");
            }
            return csv.toString();
        }

        public void processPDFtoCSV (String s){
            try{
                String result = s.substring(0, s.length() - 3)+"csv";
                parsePdf(s, result);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

//        public static void main(String[] args) throws IOException {
//            String path = "/Users/kpatchirajan/Desktop/codeJamInputFiles/";
//            String name = "IncomeTax_Bk_f1";
//            String source = path+name+".pdf";
//            String result = path+name+".csv";
//            new pdfToCsv().parsePdf(source,result);
//        }
    }