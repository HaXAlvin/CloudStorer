import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Page_Login extends JFrame implements ActionListener {
    JPanel jp1,jp2,jp3;
    JLabel jl1,jl2;
    JButton login,signup;
    JTextField jtx;
    JPasswordField jpw;
    CloudInterface	o;

    public Page_Login(CloudInterface	o) {
        this.o = o;
        jl1 = new JLabel("name :");
        jl2 = new JLabel("password :");

        jtx = new JTextField(16);
        jpw = new JPasswordField(16);

        login = new JButton("login");
        login.addActionListener(this);
        signup = new JButton("sign up");
        signup.addActionListener(this);

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        jp1.add(jl1);
        jp1.add(jtx);
        jp2.add(jl2);
        jp2.add(jpw);
        jp3.add(login);
        jp3.add(signup);

        this.setResizable(false);
        this.setLayout(new FlowLayout());
        this.add(jp1);this.add(jp2);this.add(jp3);
        this.setSize(400,300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
//  TODO Button Listener Way1
    public void actionPerformed(ActionEvent e) {
        String account = jtx.getText();
        String password = new String(jpw.getPassword());
        jtx.setText("");
        jpw.setText("");
        if(account.equals("") || password.equals("")){
            return;
        }
        //Check button click login or signup
        if(e.getSource() == login) {
            try {
                if (o.login(account, password)) {
                    JOptionPane.showMessageDialog(null, "success");
                    this.dispose();
                    //new Page_Home(".");

                    Page_Home.main(o,account);
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed.","Error",JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception exception) {
                System.out.println("CloudServer exception: " + exception.getMessage());
                exception.printStackTrace();
            }
        }else if(e.getSource() == signup) {
            try{
                if(o.register(account,password)){
                    JOptionPane.showMessageDialog(null, "create success");
                }
                else{
                    JOptionPane.showMessageDialog(null, "account has been create","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception exception)
            {
                System.out.println("CloudServer exception: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
}
