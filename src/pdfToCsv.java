import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class pdfToCsv {

        /**
         * Parses a PDF to a plain text file.
         * @param pdf the original PDF
         * @param result the resulting text
         * @throws IOException
         */
        private void parsePdf(String pdf, String result) throws IOException {
            PdfReader reader = new PdfReader(pdf);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            TextExtractionStrategy strategy;
            BST tree = new BST();
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                        strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                        String page = strategy.getResultantText();
                        String s2[]=page.split("\n");
                        for(int j=1;j<s2.length;j++){
                                String[] parsedString = s2[j].trim().split(" ");
                                DataNode infoNode = processString(parsedString);
                                if(infoNode!=null) {
                                    String CSVFormat = extractInCSVFormat(infoNode);
//                                    System.out.println(CSVFormat);
                                    tree.insert(infoNode.minimumAmount,CSVFormat);
                                }
                        }
            }
            LinkedList<String> list = tree.inOrderTraversal();
            if(list!=null){
                printList(list,result);
            }
            reader.close();
        }

        private static DataNode processString(String[] parsedString){
            if(parsedString!=null && parsedString.length==7 && parsedString[2].equalsIgnoreCase("")){
                if(!isInteger(removeSpecialChars(parsedString[0])) || !isInteger(removeSpecialChars(parsedString[1])) ||
                        !isInteger(removeSpecialChars(parsedString[3])) || !isInteger(removeSpecialChars(parsedString[4])) ||
                        !isInteger(removeSpecialChars(parsedString[5]))|| !isInteger(removeSpecialChars(parsedString[6])))
                    return null;
                else
                    return new DataNode(getInteger(parsedString[0]),getInteger(parsedString[1]),getInteger(parsedString[3]),
                        getInteger(parsedString[4]),getInteger(parsedString[5]),getInteger(parsedString[6]));
            }
            return null;
        }

        public static boolean isInteger(String s) {
            return isInteger(s,10);
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

        private static int getInteger(String s){
            return Integer.parseInt(removeSpecialChars(s));
        }

        private static String removeSpecialChars(String inputString){
            return inputString.replaceAll("[$,]", "");
        }

        private void printList(LinkedList<String> list,String result){
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

        public static String extractInCSVFormat(DataNode data){
            StringBuilder csv = new StringBuilder();
            csv.append(data.minimumAmount);
            csv.append(",");
            csv.append(data.maximumAmount);
            csv.append(",");
            csv.append(data.single);
            csv.append(",");
            csv.append(data.marriedFilingJointly);
            csv.append(",");
            csv.append(data.marriedFilingSeparately);
            csv.append(",");
            csv.append(data.headOfHouseHold);
            return csv.toString();
        }

        public void processPDFtoCSV (String s){

            try{
                String result = s.substring(0, s.length() - 3)+"csv";
                parsePdf(s, result);
            }
            catch (Exception e){

            }
        }

        /**
         * Main method.
         * @param    args    no arguments needed
         * @throws IOException
         */
        public static void main(String[] args) throws IOException {
//            new pdfToCsv().parsePdf(PREFACE, RESULT);
        }
    }

