
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LoginOutHandler implements ActionListener {
    Connection connection;
    JTable table;
    DefaultTableModel model;
    JScrollPane scroller;
    JFrame myFrameClass;
    String loggedInUserId;
    LoginOutHandler(Connection connection, JTable table, DefaultTableModel model, JScrollPane scroller,
                       JFrame myFrameClass, String loggedInUserId) {
        this.connection = connection;
        this.table = table;
        this.model = model;
        this.scroller = scroller;
        this.myFrameClass = myFrameClass;
        this.loggedInUserId = loggedInUserId;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Login Out");
        loggedInUserId = "";
        new MyFrameClass();
        myFrameClass.dispose();
    }
}
