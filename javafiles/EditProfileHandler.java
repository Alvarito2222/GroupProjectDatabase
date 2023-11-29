




import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class EditProfileHandler implements ActionListener
{
    private String userEmail;
    private Connection connection;

    public EditProfileHandler(String loggedInUserId, Connection connection) 
    {
        this.userEmail = loggedInUserId;
        this.connection = connection;
        //showEditDialog();
    }

    private void showEditDialog() 
    {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try 
        {
            String query = "SELECT * FROM member WHERE email = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JTextField nameField = new JTextField(rs.getString("name"));
                JTextField addressField = new JTextField(rs.getString("address"));
                JTextField phoneNumberField = new JTextField(rs.getString("phonenumber"));
                JTextField planIDField = new JTextField(rs.getString("planID"));
                JPasswordField passwordField = new JPasswordField(rs.getString("password"));

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Address:"));
                panel.add(addressField);
                panel.add(new JLabel("Phone Number:"));
                panel.add(phoneNumberField);
                panel.add(new JLabel("Plan ID:"));
                panel.add(planIDField);
                panel.add(new JLabel("Password:"));
                panel.add(passwordField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Edit Profile",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    // Here, handle the update of the user's information in the database
                    updateUserData(nameField.getText(), addressField.getText(), phoneNumberField.getText(), 
                                   planIDField.getText(), new String(passwordField.getPassword()));
                }
            } else {
                JOptionPane.showMessageDialog(null, "User not found!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching user data!");
        }
    }

    private void updateUserData(String name, String address, String phoneNumber, String planID, String password) {
        try {
            String updateQuery = "UPDATE member SET name = ?, address = ?, phonenumber = ?, planID = ?, password = ? WHERE email = ?";
            PreparedStatement stmt = connection.prepareStatement(updateQuery);
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, planID);
            stmt.setString(5, password);
            stmt.setString(6, userEmail);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No changes were made.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating user data!");
        }
    }
}
