
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
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
            removeMember(movieSerieName);
            model.removeRow(selectedRow);
        }
    }



	 

private void removeMember(Object memberID) {
    
    try {
        String query = "DELETE FROM member WHERE id = ?"; 
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, memberID);
        statement.executeUpdate();
        statement.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void addNewMember() {
    JTextField nameField = new JTextField(10);
    JTextField emailField = new JTextField(10);
    JTextField ageField = new JTextField(10);

    JPanel panel = new JPanel();
    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(Box.createHorizontalStrut(15)); 
    panel.add(new JLabel("Email:"));
    panel.add(emailField);
    panel.add(Box.createHorizontalStrut(15));
    panel.add(new JLabel("Age:"));
    panel.add(ageField);

    int result = JOptionPane.showConfirmDialog(null, panel, 
             "Enter Member Details", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
       
        // String name = nameField.getText();
        // String email = emailField.getText();
        // String age = ageField.getText();
        // Add new member to the database
    }
}

private void addNewMovie() {
    JTextField titleField = new JTextField(10);
    JTextField genreField = new JTextField(10);
    JTextField yearField = new JTextField(10);

    JPanel panel = new JPanel();
    panel.add(new JLabel("Title:"));
    panel.add(titleField);
    panel.add(Box.createHorizontalStrut(15)); // a spacer
    panel.add(new JLabel("Genre:"));
    panel.add(genreField);
    panel.add(Box.createHorizontalStrut(15)); // a spacer
    panel.add(new JLabel("Year:"));
    panel.add(yearField);

    int result = JOptionPane.showConfirmDialog(null, panel, 
             "Enter Movie Details", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
    
        // String title = titleField.getText();
        // String genre = genreField.getText();
        // String year = yearField.getText();
        // Add new movie to the database
    }
}
}

