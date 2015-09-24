/*
 * $Header$
 * $Revision: 1.2 $
 * $Date$
 * ====================================================================
 *
 *  Copyright 2002-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * .
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.MultipartPostMethod;

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
        private DefaultComboBoxModel cmbURLModel;
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
//                            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
                                    System.out.println(path);
                                    File[] filesInDirectory = targetFile.listFiles();
                                    for ( File file : filesInDirectory ) {
                                        String fileName = file.getName().toString();
                                        if(fileName.length()>4 && fileName.substring(fileName.length()-4).equalsIgnoreCase(".pdf")){
                                            inputFiles.add(path+fileName);
                                            appendMessage("Selected: " + path + fileName);
                                        }
                                    }
                                }

//                                System.out.println(Arrays.toString(inputFiles.toArray()));
                                tfdTargetFile.setText(targetFile.toString());
                                btnDoUpload.setEnabled(true);
                            }
                        }
                    }
            );

            taTextResponse = new JTextArea(10, 40);
            taTextResponse.setEditable(false);

            final JLabel lblURL = new JLabel("PDF Source:");

            btnDoUpload.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    for(int fileIndex=0;fileIndex<inputFiles.size();fileIndex++){
                        pdfToCsv pdf = new pdfToCsv();
                        try{
                            appendMessage("Converting: " + inputFiles.get(fileIndex));
                            pdf.processPDFtoCSV(inputFiles.get(fileIndex));
                            appendMessage("Done! " + inputFiles.get(fileIndex));
                        }catch (Exception e){

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
//            getContentPane().add(cmbURL, c);

            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(10, 5, 5, 0);
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 1;
//            getContentPane().add(lblTargetFile, c);

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
