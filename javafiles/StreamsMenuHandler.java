import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StreamsMenuHandler implements ActionListener {

	Connection connection;
	JTable table;
	DefaultTableModel model;
	JScrollPane scroller;
	JFrame myFrameClass;

	StreamsMenuHandler(Connection connection, JTable table, DefaultTableModel model, JScrollPane scroller,
			JFrame myFrameClass) {
		this.connection = connection;
		this.table = table;
		this.model = model;
		this.scroller = scroller;
		this.myFrameClass = myFrameClass;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
	        // Fetch movie titles
	        String titleQuery = "SELECT DISTINCT title FROM streams";
	        PreparedStatement titleStmt = connection.prepareStatement(titleQuery);
	        ResultSet titleRs = titleStmt.executeQuery();
	        JComboBox<String> comboBox = new JComboBox<>();

	        while (titleRs.next()) {
	            comboBox.addItem(titleRs.getString("title"));
	        }

	        // Check if there are movie titles to display
	        if (comboBox.getItemCount() == 0) {
	            JOptionPane.showMessageDialog(myFrameClass, "No movies found!", "Information", JOptionPane.INFORMATION_MESSAGE);
	            return;
	        }

	        JOptionPane.showMessageDialog(myFrameClass, comboBox, "Select Movie", JOptionPane.QUESTION_MESSAGE);
	        titleStmt.close();

	        // Get the selected movie title from the combo box
	        String selectedTitle = (String) comboBox.getSelectedItem();

	        if (selectedTitle != null && !selectedTitle.isEmpty()) {
	            // Query to get stream details based on the selected title
	            String query = "SELECT email, title FROM streams WHERE title LIKE ?";
	            PreparedStatement statement = connection.prepareStatement(query);
	            statement.setString(1, "%" + selectedTitle + "%");

	            ResultSet resultSet = statement.executeQuery();
	            ResultSetMetaData metaData = resultSet.getMetaData();
	            int columnCount = metaData.getColumnCount();

	            // Clear existing data
	            model.setRowCount(0);

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

	            // Set the model on the table and update the scroll pane if needed
	            table.setModel(model);
	            table.setFillsViewportHeight(true);

	            myFrameClass.getContentPane().add(scroller, BorderLayout.CENTER);
	            scroller.setViewportView(table);

	            myFrameClass.validate();
	            statement.close();
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

}
