import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.rmi.RemoteException;


class Page_Home extends JFrame {
    private final CloudInterface	o;
    private final String            account;

    private final JFilePicker       filePicker      = new JFilePicker("Choose a file: ", "Browse");
    private final JButton           buttonUpload    = new JButton("Upload");
    private final JButton           buttonShowFile  = new JButton("Download");
    private final JButton           buttonDelFile   = new JButton("Delete File");
    private final JButton           buttonAddAccessor   = new JButton("Add Accessor");
    private final JButton           buttonDelAccount    = new JButton("Del Account");

    /*
    TODO ProgressBar
    private JLabel labelProgress = new JLabel("Progress:");
    private JProgressBar progressBar = new JProgressBar(0, 100);
    */

    public Page_Home(CloudInterface	o,String account) {
        super("Home");
        this.o = o;
        this.account = account;
        // set up layout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        // set up components
        filePicker.setMode(JFilePicker.MODE_OPEN);

//      TODO Button Listener Way2
        buttonUpload.addActionListener(event -> buttonUploadActionPerformed());
        buttonShowFile.addActionListener(event -> buttonShowFileAction());
        buttonDelFile.addActionListener(event -> buttonDelFile());
        buttonAddAccessor.addActionListener(event -> buttonAddAccAction());
        buttonDelAccount.addActionListener(event -> buttonDelAccountAction());
/*
      TODO ProgressBar
      progressBar.setPreferredSize(new Dimension(200, 30));
      progressBar.setStringPainted(true);
*/


        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        add(filePicker, constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonUpload, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonShowFile, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonDelFile, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonAddAccessor, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonDelAccount, constraints);


        pack();
        setLocationRelativeTo(null);    // center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void buttonDelAccountAction(){
        try {
            String password = JOptionPane.showInputDialog(this, "If you want to delete your account,\nPlease enter your password.");
            if(password != null && JOptionPane.showConfirmDialog (null, "Are you sure?","WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                if(o.delAccount(account,password)){
                    JOptionPane.showMessageDialog(null,
                            "Your Account Now Have Deleted successfully!", "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    new Page_Login(o);
                }else{
                    JOptionPane.showMessageDialog(this,
                            "Password Error", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this,
                    "Error executing del account: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void buttonAddAccAction() {
        try {
            Object[][] myFiles = o.showCreatedFile(account);
            Object[] cols = {
                    "ID", "FileName", "Accessor", "Date"
            };
            JTable table = new JTable(myFiles, cols);
            Object[] values = new Object[myFiles.length];
            for (int i = 0; i < myFiles.length; i++) {
                values[i] = i;
            }
//          TODO InputDialog1
            Object selected = JOptionPane.showInputDialog(
                    null,
                    new JScrollPane(table),
                    "Selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    values,
                    "0"
            );

            if (selected != null) {//null if the user cancels.
                int choose = (int) selected;
//              TODO InputDialog2
                String addAccessor = JOptionPane.showInputDialog(this, "Who you want to add?");

                if (o.addAccessor(account, myFiles[choose][1].toString(), addAccessor)) {
                    //TODO MessageDialog1
                    JOptionPane.showMessageDialog(
                            this,
                            "Add accessor  successfully!",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    //TODO MessageDialog2
                    JOptionPane.showMessageDialog(
                            this,
                            "Add accessor failed",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "User cancelled", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this,
                    "Error executing add accessor task: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttonDelFile(){
        try {
            Object[][] myFiles = o.showCreatedFile(account);
            Object[] cols = {
                    "ID","FileName","Accessor","Date"
            };
            JTable table = new JTable(myFiles, cols);
            Object[] values = new Object[myFiles.length];
            for(int i=0;i<myFiles.length;i++){
                values[i] = i;
            }

            Object selected = JOptionPane.showInputDialog(null, new JScrollPane(table), "Selection", JOptionPane.PLAIN_MESSAGE, null, values, "0");


            //Object selected = JOptionPane.showInputDialog(null, "What is the target Nicotine level?", "Selection", JOptionPane.DEFAULT_OPTION, null, myFiles, "0");
            if ( selected != null ){//null if the user cancels.
                int choose = (int) selected;
                if(o.delFile(myFiles[choose][1].toString(),account)){
                    JOptionPane.showMessageDialog(null,
                            "File has been delete successfully!", "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(this,
                            "File delete failed", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this,
                        "User cancelled", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            //JOptionPane.showMessageDialog(null,"File has been uploaded successfully!", "Message",JOptionPane.QUESTION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error executing del task: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void buttonShowFileAction(){
        try {
            Object[][] myFiles = o.showMyFile(account);
            Object[] cols = {
                    "ID","FileName","Creator","Date"
            };
            JTable table = new JTable(myFiles, cols);
            Object[] values = new Object[myFiles.length];
            for(int i=0;i<myFiles.length;i++){
                values[i] = i;
            }

            Object selected = JOptionPane.showInputDialog(null, new JScrollPane(table), "Selection", JOptionPane.PLAIN_MESSAGE, null, values, "0");


            if ( selected != null ){//null if the user cancels.
                int choose = (int) selected;
                byte[] fileData = o.loadDataFromServer(myFiles[choose][1].toString(),myFiles[choose][2].toString());
                if(fileData != null){
                    File file = new File(System.getProperty("user.dir")+File.separator+myFiles[choose][1].toString());
                    if(!file.createNewFile()) {
                        fileData = null;
                    }else {
                        FileOutputStream out = new FileOutputStream(file, true);
                        out.write(fileData, 0, fileData.length);
                        out.flush();
                        out.close();
                        JOptionPane.showMessageDialog(null,
                                "File has been download successfully!", "Message",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                if(fileData == null){
                    JOptionPane.showMessageDialog(this,
                            "Error executing upload task", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this,
                        "User cancelled", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            //JOptionPane.showMessageDialog(null,"File has been uploaded successfully!", "Message",JOptionPane.QUESTION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error executing upload task: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void buttonUploadActionPerformed() {
        String filePath = filePicker.getSelectedFilePath();
        String[] splitPath = filePath.split(File.separator);
        String fileName = splitPath[splitPath.length-1];

        if (filePath.equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Please choose a file to upload!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File(filePath);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
//              TODO Upload
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);

                buf.close();
                if(o.writeDataToServer(fileName,bytes,bytes.length,account)){
                    JOptionPane.showMessageDialog(null,
                            "File has been uploaded successfully!", "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Error uploading file",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error executing upload task: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(CloudInterface o, String account) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Page_Home(o,account).setVisible(true));
    }
}