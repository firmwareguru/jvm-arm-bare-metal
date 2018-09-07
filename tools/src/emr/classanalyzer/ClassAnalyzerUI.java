/*
 * ClassAnalyzerUI.java
 *
 * Created on March 22, 2006, 10:31 AM
 */

package emr.classanalyzer;
import java.io.*;
import emr.classanalyzer.structures.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.util.Vector;
/**
 *
 * @author  Ross
 */
public class ClassAnalyzerUI extends javax.swing.JFrame implements TreeSelectionListener {
    
    /** Creates new form ClassAnalyzerUI */
    public ClassAnalyzerUI() {
        fileChooser = new javax.swing.JFileChooser();
        fileChooser.setCurrentDirectory(new File("/"));
        
        classFile = null;
        
        initComponents();      
       
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoArea = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        selectFileButton = new javax.swing.JButton();
        selectedFileText = new javax.swing.JTextField();
        analyzeButton = new javax.swing.JButton();
        fileSizeLabel = new javax.swing.JLabel();
        classStructure = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        classTree = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        infoArea.setColumns(20);
        infoArea.setEditable(false);
        infoArea.setRows(5);
        jScrollPane1.setViewportView(infoArea);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Input File", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        selectFileButton.setText("Select File...");
        selectFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getSelectedFile(evt);
            }
        });

        analyzeButton.setText("Analyze");
        analyzeButton.setEnabled(false);
        analyzeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analyzeClassFile(evt);
            }
        });

        fileSizeLabel.setText("File size:");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(analyzeButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(selectFileButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(selectedFileText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                    .add(fileSizeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(selectFileButton)
                    .add(selectedFileText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(15, 15, 15)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(analyzeButton)
                    .add(fileSizeLabel)))
        );

        classStructure.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Class Structure", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        displayArea.setColumns(20);
        displayArea.setRows(5);
        jScrollPane3.setViewportView(displayArea);

        classTree.setAutoscrolls(true);
        jScrollPane4.setViewportView(classTree);

        org.jdesktop.layout.GroupLayout classStructureLayout = new org.jdesktop.layout.GroupLayout(classStructure);
        classStructure.setLayout(classStructureLayout);
        classStructureLayout.setHorizontalGroup(
            classStructureLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(classStructureLayout.createSequentialGroup()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 242, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
        );
        classStructureLayout.setVerticalGroup(
            classStructureLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, classStructure, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(classStructure, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void analyzeClassFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analyzeClassFile
// TODO add your handling code here:
        /* lets analyze!
         * Required: a valid 'file'
         */
        infoArea.setText("");
        
        infoArea.append("Analyzing file..." + "\n");
        if(file == null)
            return;
        
        /* setup the input stream */
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (Exception e) {
            return;
        }
        
        /* create array of bytes */
        infoArea.append("Reading " + file.length() + " bytes.\n");
        byte[] classData = new byte[(int)file.length()];
        
        try {
            fis.read(classData);
        } catch (Exception e) {
            infoArea.append("Error reading file, aborting.\n");
        }
        
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        TreeModel model = new DefaultTreeModel(rootNode);
        classTree.setModel(model);
        
        classTree.addTreeSelectionListener(this);
        
        // construct a ClassFile
        
            classFile = new ClassFile(classData, rootNode, infoArea);
        
        classFile.parseClassFile();
        
        classTree.setRootVisible(true);
        
        
    }//GEN-LAST:event_analyzeClassFile

   /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           classTree.getLastSelectedPathComponent();

        if (node == null) return;

        Displayable nodeInfo = (Displayable)node.getUserObject();
        
        Vector<String> contents = nodeInfo.displayContents();
        
        displayArea.setText("");
        for(int i = 0; i < contents.size(); i++)
            displayArea.append(contents.get(i));
   
 
    }    
    
    /* read in the structures in the constant pool area and return an integer specifying the
     * byte index immediately after the constant pool area.
     */
    private int readConstantPoolData(byte[] data, int count) {
        // the constant pool data begins at the 11th byte in the file.
        
        // iterate 'count' times, creating appropriate structures.
        
        return 0;
    }
    
    
    private void getSelectedFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getSelectedFile
// TODO add your handling code here:
        fileChooser.setCurrentDirectory(new File("c:/java/classanalyzer/build/classes/emr/classanalyzer/samples"));
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            String fileName = file.getName();
            
            if(!fileName.endsWith(".class")) {
                selectedFileText.setText("Invalid File");
                analyzeButton.setEnabled(false);
            }
            else {
                selectedFileText.setText(file.getPath());
                fileSizeLabel.setText("File size: " + file.length() + " bytes");
                
                
                analyzeButton.setEnabled(true);
            }
            
        } else {
        }

    }//GEN-LAST:event_getSelectedFile
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClassAnalyzerUI().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton analyzeButton;
    private javax.swing.JPanel classStructure;
    private javax.swing.JTree classTree;
    private javax.swing.JTextArea displayArea;
    private javax.swing.JLabel fileSizeLabel;
    private javax.swing.JTextArea infoArea;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton selectFileButton;
    private javax.swing.JTextField selectedFileText;
    // End of variables declaration//GEN-END:variables
    
    private javax.swing.JFileChooser fileChooser;
    
    /*  This is the Class file that is to be analyzed */
    private File file;
    
    /*  This class extracts structure from the class file */
    ClassFile classFile;
}
