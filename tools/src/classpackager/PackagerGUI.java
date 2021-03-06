/*
 * PackagerGUI.java
 *
 * Created on March 6, 2008, 8:24 PM
 */

package classpackager;

import emr.classfileviewer.ClassViewerUI;
import emr.elements.common.Element;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.ListModel;
import javax.swing.UIManager;

/**
 *
 * @author  Evan Ross
 */
public class PackagerGUI extends javax.swing.JFrame {
    
    // Maintain the list of library jars in a DefaultListModel
    DefaultListModel jarListModel;
    
    Packager packager = null;
    
    /** Creates new form PackagerGUI */
    private PackagerGUI() {
        initComponents();
        
        jarListModel = new DefaultListModel();
        jarList.setModel(jarListModel);
        
        bigEndianRadioButton.setSelected(true);
        
        viewButton.setEnabled(false);       
    
    }
    
    private static PackagerGUI gui = null;
    public static PackagerGUI startGUI() {
        
        // Set the Windows look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            
        }
            
        try {
        java.awt.EventQueue.invokeAndWait(new Runnable() {
            public void run() {
                if (gui == null)
                    (gui = new PackagerGUI()).setVisible(true);
                
                
            }
        });
        } catch (Exception e) { }
        
        return gui;
    }
    
    public void addLibrary(final String library_)
    {
        // Add it to the listModel
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                jarListModel.addElement(library_);
            }
        });
    }

    public void removeLibrary(final String library_)
    {
        // Add it to the listModel
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                jarListModel.removeElement(library_);
            }
        });
    }
    
    public void setDefaultPackageName(final String packageName_)
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                packageFileTextField.setText(packageName_);
            }
        });
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jarList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        addJarButton = new javax.swing.JButton();
        removeJarButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        packageFileTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        packageSelectButton = new javax.swing.JButton();
        littleEndianRadioButton = new javax.swing.JRadioButton();
        bigEndianRadioButton = new javax.swing.JRadioButton();
        viewButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        offsetField = new javax.swing.JTextField();
        serializeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Class Packager");

        jScrollPane1.setViewportView(jarList);

        jLabel1.setText("Package Files");

        addJarButton.setText("Add...");
        addJarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJarButtonActionPerformed(evt);
            }
        });

        removeJarButton.setText("Remove");
        removeJarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeJarButtonActionPerformed(evt);
            }
        });

        jButton3.setText("Create Package");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createPackageHandler(evt);
            }
        });

        jLabel2.setText("Package File:");

        packageSelectButton.setText("Select");

        buttonGroup1.add(littleEndianRadioButton);
        littleEndianRadioButton.setText("Little Endian");
        littleEndianRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        littleEndianRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        littleEndianRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                littleEndianSelection(evt);
            }
        });

        buttonGroup1.add(bigEndianRadioButton);
        bigEndianRadioButton.setText("Big Endian");
        bigEndianRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bigEndianRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bigEndianRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bigEndianSelection(evt);
            }
        });

        viewButton.setText("View");
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPackageHandler(evt);
            }
        });

        jLabel3.setText("Package address offset: ");

        offsetField.setText("0");

        serializeButton.setText("Serialize...");
        serializeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serializeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addJarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeJarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                        .addComponent(serializeButton))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(packageFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(packageSelectButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(offsetField, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bigEndianRadioButton)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                                .addComponent(viewButton))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(littleEndianRadioButton)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addJarButton)
                    .addComponent(removeJarButton)
                    .addComponent(serializeButton))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(packageFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(packageSelectButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(offsetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bigEndianRadioButton)
                    .addComponent(littleEndianRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(viewButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void littleEndianSelection(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_littleEndianSelection
// TODO add your handling code here:
        Element.endian = Element.EndianEnum.LITTLE;
    }//GEN-LAST:event_littleEndianSelection

    private void bigEndianSelection(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bigEndianSelection
// TODO add your handling code here:
        Element.endian = Element.EndianEnum.BIG;
    }//GEN-LAST:event_bigEndianSelection

    private void viewPackageHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPackageHandler
// TODO add your handling code here:
       
        if (packager == null)
            return;
        // Package Viewer:
       
        ClassViewerUI classViewer = new ClassViewerUI(packageFileTextField.getText());
        classViewer.setVisible(true);
        classViewer.setDefaultCloseOperation(ClassViewerUI.DISPOSE_ON_CLOSE);
        
        // view the Package
        classViewer.buildTreeFromElement(packager.getPackage());

    }//GEN-LAST:event_viewPackageHandler

    private void createPackageHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createPackageHandler
// TODO add your handling code here:
        
        // Create a packager
        packager = new Packager();
        
        // install native method library
        Packager.installNativeLibrary( new CoreNativeMethodLibrary() );
        
        // Get the package address offset that is applied to all addresses
        int offset = 0;
        try {
            offset = Integer.parseInt(offsetField.getText());
        } catch (NumberFormatException e)
        { offsetField.setText("0"); }
        
        // create the package
        packager.createPackage(jarListModel, packageFileTextField.getText(), offset);
        
        viewButton.setEnabled(true);
        
    }//GEN-LAST:event_createPackageHandler



    private void addJarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJarButtonActionPerformed

        JFileChooser chooser = new JFileChooser("C:/Projects");
        int returnVal = chooser.showDialog(this.getParent(), "Select Jar");
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            addLibrary(chooser.getSelectedFile().getAbsolutePath());
        }

    }//GEN-LAST:event_addJarButtonActionPerformed

    private void removeJarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeJarButtonActionPerformed

        Object[] values = jarList.getSelectedValues();
        for (Object o : values) {
            removeLibrary((String)o);
        }

    }//GEN-LAST:event_removeJarButtonActionPerformed

    private void serializeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serializeButtonActionPerformed

        // Add the files in the "serialized" file system to the "jarList".
        // These files must end in .ser for special treatment.
        // Hack... yes.. but there's so much to do.

        // Dependency: emr.classserializer.Serializer.OUTPUT_ROOT_DIR:
        File serFileDir = new File(emr.classserializer.Serializer.OUTPUT_ROOT_DIR);

        if (serFileDir.exists()) {
            if (serFileDir.isDirectory()) {
                for (File f : serFileDir.listFiles()) {
                    addLibrary(f.getAbsolutePath());
                }
            }
        }

    }//GEN-LAST:event_serializeButtonActionPerformed
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJarButton;
    private javax.swing.JRadioButton bigEndianRadioButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList jarList;
    private javax.swing.JRadioButton littleEndianRadioButton;
    private javax.swing.JTextField offsetField;
    private javax.swing.JTextField packageFileTextField;
    private javax.swing.JButton packageSelectButton;
    private javax.swing.JButton removeJarButton;
    private javax.swing.JButton serializeButton;
    private javax.swing.JButton viewButton;
    // End of variables declaration//GEN-END:variables
    
}
