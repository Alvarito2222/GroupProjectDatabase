import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class HistoryMenuHandler implements ActionListener
	    {
	
Connection connection;
	
	JTable table;
	JScrollPane scroller;
	JFrame myFrameClass;
	String loggedInUserId;

HistoryMenuHandler(Connection connection, JTable table , JScrollPane scroller , JFrame myFrameClass,String loggedInUserId )
{
	
	this.connection = connection;
	this.table = table;
	this.scroller = scroller;
	this.myFrameClass= myFrameClass;
	this.loggedInUserId = loggedInUserId;
}
	        public void actionPerformed(ActionEvent e) 
	        {
	        	System.out.println("User email is: " + loggedInUserId);
	        	
	        	if (loggedInUserId == null || loggedInUserId.isEmpty()) 
	        	{
	                JOptionPane.showMessageDialog(null, "User not logged in!");
	                return;
	            }

	            try {
	                String query = "SELECT * FROM streams WHERE email = ?";
	                PreparedStatement statement = connection.prepareStatement(query);
	                statement.setString(1, loggedInUserId);

	                ResultSet resultSet = statement.executeQuery();

	                Vector<Vector<Object>> rows = new Vector<>();
	                Vector<Object> columnNames = new Vector<>();

	                ResultSetMetaData metaData = resultSet.getMetaData();
	                int columnCount = metaData.getColumnCount();

	                for (int i = 1; i <= columnCount; ++i) 
	                {
	                    columnNames.addElement(metaData.getColumnName(i));
	                }

	                while (resultSet.next()) 
	                {
	                    Vector<Object> currentRow = new Vector<>();
	                    for (int i = 1; i <= columnCount; ++i) 
	                    {
	                        currentRow.addElement(resultSet.getObject(i));
	                    }
	                    rows.addElement(currentRow);
	                }

	                if (scroller != null) 
	                {
	                	myFrameClass.getContentPane().remove(scroller);
	                }

	                // Display table with ResultSet contents
	                table = new JTable(rows, columnNames);
	                table.setFillsViewportHeight(true);
	                scroller = new JScrollPane(table);
	                scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	                scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	                myFrameClass.getContentPane().add(scroller, BorderLayout.CENTER);
	                myFrameClass.validate();

	                statement.close();
	            } 
	            catch (SQLException ex) 
	            {
	                JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }
