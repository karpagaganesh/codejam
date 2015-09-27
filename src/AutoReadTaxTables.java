import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AutoReadTaxTables{

    public static void main(String[] args) {
        MultipartFileUploadFrame f = new MultipartFileUploadFrame();
        f.setTitle("PDF 2 CSV: Tax table data Converter");
        f.pack();
        f.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        f.setVisible(true);
    }

    public static class MultipartFileUploadFrame extends JFrame {

        private File targetFile;
        private JTextArea taTextResponse;
        private ArrayList<String> inputFiles;

        public MultipartFileUploadFrame() {
            final JTextField tfdTargetFile = new JTextField(30);
            tfdTargetFile.setEditable(false);
            final JButton btnDoUpload = new JButton("Convert");
            btnDoUpload.setEnabled(false);

            final JButton btnSelectFile = new JButton("Select a file...");
            btnSelectFile.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setFileHidingEnabled(false);
                            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                            chooser.setMultiSelectionEnabled(false);
                            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                            chooser.setDialogTitle("Choose a file...");
                            if (
                                    chooser.showOpenDialog(MultipartFileUploadFrame.this)
                                            == JFileChooser.APPROVE_OPTION
                                    ) {
                                taTextResponse.setText("");
                                targetFile = chooser.getSelectedFile();


                                if(targetFile.toString().substring(targetFile.toString().length()-4).equalsIgnoreCase(".pdf")){
                                    inputFiles = new ArrayList<String>();
                                    inputFiles.add(targetFile.toString());
                                    appendMessage("Selected: " + targetFile.toString());
                                }
                                else{
                                    inputFiles = new ArrayList<String>();
                                    String path = targetFile.toString()+"/";
                                    File[] filesInDirectory = targetFile.listFiles();
                                    for ( File file : filesInDirectory ) {
                                        String fileName = file.getName().toString();
                                        if(fileName.length()>4 && fileName.substring(fileName.length()-4).equalsIgnoreCase(".pdf")){
                                            inputFiles.add(path+fileName);
                                            appendMessage("Selected: " + path + fileName);
                                        }
                                    }
                                }
                                tfdTargetFile.setText(targetFile.toString());
                                btnDoUpload.setEnabled(true);
                            }
                        }
                    }
            );

            taTextResponse = new JTextArea(10, 40);
            taTextResponse.setEditable(false);

            final JLabel lblURL = new JLabel("PDF Source:");

            btnDoUpload.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae) {

                    for(int fileIndex=0;fileIndex<inputFiles.size();fileIndex++){
                        pdfToCsv pdf = new pdfToCsv();
                        try{
                            appendMessage("Converting: " + inputFiles.get(fileIndex));
                            pdf.processPDFtoCSV(inputFiles.get(fileIndex));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

            getContentPane().setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.gridheight = 1;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 5, 5, 0);
            c.weightx = 1;
            c.weighty = 1;
            getContentPane().add(lblURL, c);

            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 2;
            c.gridx = 1;
            c.insets = new Insets(5, 5, 5, 10);

            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(10, 5, 5, 0);
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 1;

            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(5, 5, 5, 5);
            c.gridwidth = 1;
            c.gridx = 1;
            getContentPane().add(tfdTargetFile, c);

            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(5, 5, 5, 10);
            c.gridwidth = 1;
            c.gridx = 2;
            getContentPane().add(btnSelectFile, c);

            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(10, 10, 10, 10);
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 2;
            getContentPane().add(btnDoUpload, c);

            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(10, 10, 10, 10);
            c.gridwidth = 3;
            c.gridheight = 3;
            c.weighty = 3;
            c.gridx = 0;
            c.gridy = 3;
            getContentPane().add(new JScrollPane(taTextResponse), c);
        }

        private void appendMessage(String m) {
            taTextResponse.append(m + "\n");
        }
    }
}
