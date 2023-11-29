
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class AdminHandler  implements ActionListener {

	Connection connection;
	JTable table;
	DefaultTableModel model;
	JScrollPane scroller;
	JFrame myFrameClass;
	
	JButton addButton, removeButton;
    JPanel buttonPanel;
    
    private String currentView = "";

	AdminHandler(Connection connection, JTable table, DefaultTableModel model, JScrollPane scroller,
			JFrame myFrameClass) {
		this.connection = connection;
		this.table = table;
		this.model = model;
		this.scroller = scroller;
		this.myFrameClass = myFrameClass;
		
		
		
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		addButton.setVisible(false);
		
        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        removeButton.setVisible(false);
        
        removeButton.setEnabled(false);
		
		 table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	            @Override
	            public void valueChanged(ListSelectionEvent e) {
	                // Enable remove button only when a row is selected
	                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
	                    removeButton.setEnabled(true);
	                } else {
	                    removeButton.setEnabled(false);
	                }
	            }
	        });


       
        buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        
        myFrameClass.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        
        
        
	}
	
	public void actionPerformed(ActionEvent e) {
		
		addButton.setVisible(true);
        removeButton.setVisible(true);
		
        if ("Show All Members".equals(e.getActionCommand())) {
            currentView = "members";
            showMembers();
        } else if ("Show All Movies/Series".equals(e.getActionCommand())) {
            currentView = "movies";
            showMovies();
        } else if (e.getSource() == removeButton) {
        	if(currentView.equals("members")) {
        		
            removeSelectedMember();
        	}
        	else if(currentView.equals("movies")) {
        		removeSelectedMovie();
        	}
        	
        } else if (e.getSource() == addButton) {
            if (currentView.equals("members")) {
                addNewMember();
            } else if (currentView.equals("movies")) {
                addNewMovie();
            }
        }
    }
	
	
	private void showMembers() {
        clearAndSetTable();
        
        try {
            String query = "SELECT * FROM member"; 
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Set column names
            model.setColumnCount(0);
            for (int i = 1; i <= columnCount; ++i) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add rows to the model
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; ++i) {
                    row[i - 1] = resultSet.getObject(i);
                }
                model.addRow(row);
            }

            
            table.setModel(model);
            table.setFillsViewportHeight(true);
            scroller.setViewportView(table);

            myFrameClass.getContentPane().add(scroller, BorderLayout.CENTER);
            myFrameClass.validate();
            statement.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }

    private void showMovies() {
        clearAndSetTable();
        
        
        try {
            String query = "SELECT * FROM video"; 
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Set column names
            model.setColumnCount(0);
            for (int i = 1; i <= columnCount; ++i) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add rows to the model
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; ++i) {
                    row[i - 1] = resultSet.getObject(i);
                }
                model.addRow(row);
            }

            // Set the model on the table and update the scroll pane
            table.setModel(model);
            table.setFillsViewportHeight(true);
            scroller.setViewportView(table);

            myFrameClass.getContentPane().add(scroller, BorderLayout.CENTER);
            myFrameClass.validate();
            statement.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearAndSetTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
    }

    private void removeSelectedMember() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
        	Object memberID = model.getValueAt(selectedRow, 0); 
            removeMember(memberID);
            model.removeRow(selectedRow);
            
        }
    }
    
    private void removeSelectedMovie() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
        	Object movieSerieName = model.getValueAt(selectedRow, 0); // Assuming the first column is the member ID
            removeMovie(movieSerieName);
            model.removeRow(selectedRow);
        }
    }


    private void removeMovie(Object memberID) {
        
        try {
            String query = "DELETE FROM video WHERE title = ?"; 
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, memberID);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
	 

private void removeMember(Object memberID) {
    
    try {
        String query = "DELETE FROM member WHERE email = ?"; 
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, memberID);
        statement.executeUpdate();
        statement.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void addNewMember() {
    JTextField emailField = new JTextField(10);
    JTextField planIDField = new JTextField(10);
    JTextField passwordField = new JTextField(10);
    JTextField nameField = new JTextField(10);
    JTextField addressField = new JTextField(10);
    JTextField phoneField = new JTextField(10);
    String[] planTypeOptions = { "Individual", "Family" }; 
    JComboBox<String> planTypeComboBox = new JComboBox<>(planTypeOptions);
    JTextField numberOfLoginsField = new JTextField(10);

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Email:"));
    panel.add(emailField);
    panel.add(new JLabel("PlanId:"));
    panel.add(planIDField);
    panel.add(new JLabel("Password:"));
    panel.add(passwordField);
    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Address:"));
    panel.add(addressField);
    panel.add(new JLabel("Phone Number:"));
    panel.add(phoneField);
    panel.add(new JLabel("Plan Type:"));
    panel.add(planTypeComboBox);
    panel.add(new JLabel("Number of Logins:"));
    panel.add(numberOfLoginsField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Enter Member Details", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        String email = emailField.getText();
        String planID = planIDField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String planType = (String) planTypeComboBox.getSelectedItem();
        String numberOfLogins = numberOfLoginsField.getText();
        try {
            String subscriptionQuery = "INSERT INTO subscription (planID, planType, logins) VALUES (?, ?, ?)";
            PreparedStatement subscriptionStmt = connection.prepareStatement(subscriptionQuery);
            subscriptionStmt.setString(1, planID);
            subscriptionStmt.setString(2, planType);
            subscriptionStmt.setString(3, numberOfLogins);
            subscriptionStmt.executeUpdate();

            String memberQuery = "INSERT INTO member (email, planID, password, name, address, phonenumber) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement memberStmt = connection.prepareStatement(memberQuery);
            memberStmt.setString(1, email);
            memberStmt.setString(2, planID);
            memberStmt.setString(3, password);
            memberStmt.setString(4, name);
            memberStmt.setString(5, address);
            memberStmt.setString(6, phone);
            memberStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "New member added successfully!");
        } catch (SQLIntegrityConstraintViolationException ex) {
           
            JOptionPane.showMessageDialog(null, "Duplicate entry for ID. Please use a unique ID.", "Integrity Constraint Violation", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (SQLException ex) {
           
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
           
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}


private void setStringOrNull(CallableStatement stmt, int parameterIndex, String value) throws SQLException {
    if (value == null || value.trim().isEmpty()) {
        stmt.setNull(parameterIndex, Types.VARCHAR);
    } else {
        stmt.setString(parameterIndex, value);
    }
}

private void addNewMovie() {
    JTextField titleField = new JTextField(10);
    JTextField releaseDateField = new JTextField(10);
    JTextField hyperLinkField = new JTextField(10);
    JTextField castIdField = new JTextField(10);
    JTextField directorIdField = new JTextField(10);
    JTextField prequelField = new JTextField(10);
    JTextField sequelField = new JTextField(10);
    JTextField castNameField = new JTextField(10);
    JTextField castPositionField = new JTextField(10);
    JTextField castProvinceField = new JTextField(10);
    JTextField castCountryField = new JTextField(10);

    JPanel panel = new JPanel(new GridLayout(0,1));
    
    String[] genreOptions = { "Drama", "Action", "Sci-Fi", "History", "Adventure", "Crime", "Animation", "Fantasy",
	"Romance" };
JComboBox<String> genreComboBox = new JComboBox<>(genreOptions);

    // Add components to the panel using GridBagConstraints
   panel.add(new JLabel("Title:"));
   panel.add(titleField);
   panel.add(new JLabel("Release Date:"));
   panel.add(releaseDateField);
   panel.add(new JLabel("Genre:"));
   panel.add(genreComboBox);
   panel.add(new JLabel("HyperLink:"));
   panel.add(hyperLinkField);
   panel.add(new JLabel("Cast Id:"));
   panel.add(castIdField);
   panel.add(new JLabel("Director Id:"));
   panel.add(directorIdField);
   panel.add(new JLabel("Prequel Title:"));
   panel.add(prequelField);
   panel.add(new JLabel("Sequel Title:"));
   panel.add(sequelField);
   panel.add(new JLabel("Cast Name:"));
   panel.add(castNameField);
   panel.add(new JLabel("Cast Position:"));
   panel.add(castPositionField);
   panel.add(new JLabel("Cast Province:"));
   panel.add(castProvinceField);
   panel.add(new JLabel("Cast Country:"));
   panel.add(castCountryField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Enter Movie Details", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        
        
        
        
        try {
            String callProcedure = "{CALL AddMovie(?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement stmt = connection.prepareCall(callProcedure);

            // Set parameters using the utility method
            setStringOrNull(stmt, 1, titleField.getText());
            setStringOrNull(stmt, 2, releaseDateField.getText());
            setStringOrNull(stmt, 3, genreComboBox.getSelectedItem().toString());
            setStringOrNull(stmt, 4, hyperLinkField.getText());
            setStringOrNull(stmt, 5, castIdField.getText());
            setStringOrNull(stmt, 6, directorIdField.getText());
            setStringOrNull(stmt, 7, prequelField.getText());
            setStringOrNull(stmt, 8, sequelField.getText());
            setStringOrNull(stmt, 9, castNameField.getText());
            setStringOrNull(stmt, 10, castPositionField.getText());
            setStringOrNull(stmt, 11, castProvinceField.getText());
            setStringOrNull(stmt, 12, castCountryField.getText());

            
            stmt.execute();
            int updateCount = stmt.getUpdateCount();
            if (updateCount > 0) {
                JOptionPane.showMessageDialog(null, "Movie added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No changes were made.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating movie data: " + ex.getMessage());
        }
    }
}
}



