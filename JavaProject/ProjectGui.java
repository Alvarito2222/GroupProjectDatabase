import java.awt.Color;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ProjectGui {
	
	public static void main(String args[])
	   {
			
		System.out.println("Starting application.");
		
		   new MyFrameClass();
		   
		   System.out.println("Done.");
	      
	   }
}
class MyFrameClass  extends JFrame
{

	   private JLabel idLabel;
	   private JLabel pwdLabel;
	   private JTextField idField;
	   private JPasswordField pwdField;
	   private JTextArea queryArea;
	   JMenuBar menuBar;
	   JMenu menu;
	   JMenu history;
	   JMenuItem m1, m2, m3, m4, m5, h1;
	   
	   String loggedInUserId;
	   private JButton loginButton;
	   private JButton queryButton;

	   private JPanel loginPanel;
	   private JPanel queryPanel;

	   private JTable table;
	   private JScrollPane scroller;
	   
	   final  String driver = "com.mysql.cj.jdbc.Driver";
       final String url = "jdbc:mysql://data-people.csamxrafss3m.us-east-1.rds.amazonaws.com:3306/streamingservice?useLegacyDatetimeCode=false&serverTimezone=America/New_York";

	   Connection connection;

	   MyFrameClass() {
		   setLayout(new BorderLayout());
		   
		   try {
			Class.forName( driver );
		

           
          
			connection = DriverManager.getConnection(url, "alvaro", "groupProject1");
		   }
			catch(ClassNotFoundException ex) 
	         {
	            JOptionPane.showMessageDialog(null, "Failed to load JDBC driver!");
	            System.exit(1);
	         }
	         catch(SQLException ex) 
	         {
	            JOptionPane.showMessageDialog(null, "Access denied!");
	            return;
	         }
		   
	        menuBar = new JMenuBar();
	        menu = new JMenu("Search By");
	        history = new JMenu("Streaming History");
	        
	        m1 = new JMenuItem("Title");
	        m2 = new JMenuItem("Genre");
	        m3 = new JMenuItem("Actor");
	        m4 = new JMenuItem("Director");
	        m5 = new JMenuItem("Prequel/Sequel");
	        h1 = new JMenuItem("View History");
	        
	        loginButton = new JButton("Login");
	        
	        LoginHandler lhandler = new LoginHandler();
		    loginButton.addActionListener(lhandler);
	        
	        TitleMenuHandler titleHandler = new TitleMenuHandler(connection,table ,scroller ,this);
	        GenreMenuHandler genreHandler = new GenreMenuHandler(connection,table ,scroller ,this);
	        ActorMenuHandler actorHandler = new ActorMenuHandler(connection,table ,scroller ,this);
	        DirectorMenuHandler directorHandler = new DirectorMenuHandler(connection,table ,scroller ,this);
	        HistoryMenuHandler historyHandler = new HistoryMenuHandler(connection,table ,scroller ,this, loggedInUserId);
	        SequelMenuHandler sequelHandler = new SequelMenuHandler(connection,table ,scroller ,this);
	        
	        m1.addActionListener(titleHandler);
	        m2.addActionListener(genreHandler);
	        m3.addActionListener(actorHandler);
	        m4.addActionListener(directorHandler);
	        m5.addActionListener(sequelHandler);
	        h1.addActionListener(historyHandler);

	        menu.add(m1);
	        menu.add(new JSeparator());
	        menu.add(m2);
	        menu.add(new JSeparator());
	        menu.add(m3);
	        menu.add(new JSeparator());
	        menu.add(m4);
	        menu.add(new JSeparator());
	        menu.add(m5);
	        
	        history.add(h1);
	        
	        menu.setBorder(new CompoundBorder(
	                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK), 
	                BorderFactory.createEmptyBorder(5, 5, 5, 5) 
	        ));

	        history.setBorder(new CompoundBorder(
	                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
	                BorderFactory.createEmptyBorder(5, 5, 5, 5) 
	        ));
	        
	        menuBar.add(menu);
	        menuBar.add(history);
	        setJMenuBar(menuBar);
		   
		    loginPanel = new JPanel();
		    idLabel = new JLabel("User Id");
		    pwdLabel = new JLabel("Password");
		    idField = new JTextField(10);
		    pwdField = new JPasswordField(10);
		    idLabel.setForeground(Color.BLACK); 
		    pwdLabel.setForeground(Color.BLACK); 

		   
		    loginPanel.setLayout(new GridLayout(2, 2, 0, 5));
		    loginPanel.add(idLabel);
		    loginPanel.add(idField);
		    loginPanel.add(pwdLabel);
		    loginPanel.add(pwdField);
		    

		    // Panel to hold both the loginPanel and loginButton
		    JPanel northPanel = new JPanel(new BorderLayout());
		    northPanel.add(loginPanel, BorderLayout.CENTER);
		    northPanel.add(loginButton, BorderLayout.SOUTH);

		    // Add the northPanel to the NORTH region of the BorderLayout
		    add(northPanel, BorderLayout.NORTH);

		    

		    queryPanel = new JPanel();
		    queryArea = new JTextArea(5, 30); // 5 rows, 30 columns
		    queryArea.setLineWrap(true);
		    queryArea.setWrapStyleWord(true);
		    queryButton = new JButton("Submit");
		    queryButton.setEnabled(false);

		    JScrollPane queryAreaScrollPane = new JScrollPane(queryArea);
		    queryAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		    queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.X_AXIS));
		    queryPanel.setBorder(BorderFactory.createTitledBorder("Query"));
		    queryPanel.add(queryAreaScrollPane);
		    queryPanel.add(queryButton);

		    add(queryPanel, BorderLayout.SOUTH);


		   
		    add(queryPanel, BorderLayout.SOUTH);

		    QueryHandler qhandler = new QueryHandler();
		    queryButton.addActionListener(qhandler);

		    WindowHandler window = new WindowHandler();
		    this.addWindowListener(window);
		    
		    loginPanel.setBackground(new Color(230, 230, 250)); 
		    queryPanel.setBackground(new Color(176, 224, 230));
		    
		    loginButton.setBackground(new Color(173, 216, 230)); 
		    loginButton.setForeground(Color.BLACK);
		    queryButton.setBackground(new Color(173, 216, 230)); 
		    queryButton.setForeground(Color.BLACK); 
		    
		    queryArea.setBackground(new Color(245, 245, 245)); 
		    queryArea.setForeground(Color.BLACK); 

		    
		    
		    
		    
		 
		    
		    setupMainFrame();
		}

	   
	   void setupMainFrame()
	   {
		   
		   Toolkit   tk;
		   Dimension  d;
		   
		   tk = Toolkit.getDefaultToolkit();
		   d= tk.getScreenSize();
		   setSize(d.width/2,d.height/2);
		   setLocation(d.width/4, d.height/4);
		   
		   setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		   
		   setTitle("DataPeopleGroupProject");
		   
		   setVisible(true);
	   }
	   
	   
	   
	// inner class for handling login
	   class LoginHandler implements ActionListener
	   {
	      public void actionPerformed(ActionEvent e)
	      {
	         String id = idField.getText();
	         char[] p = pwdField.getPassword();
	         String pwd = new String(p);

	        

	            // Store the logged-in user ID
                loggedInUserId = id;
	            
	            loginButton.setEnabled(false);
	            queryButton.setEnabled(true);

	       
	      }
	   }
	   
	   
	    public String getLoggedInUserId() 
	    {
	        return loggedInUserId;
	    }
	   
	   
	  

	    
	    
	    

	   
	    
	   
	    
	    
	   // inner class for handling query
	   private class QueryHandler implements ActionListener
	   {
	      public void actionPerformed(ActionEvent e)
	      {
	         String query = queryArea.getText();
	         Statement statement;
	         ResultSet resultSet;
	         try {
	            statement = connection.createStatement();
	            resultSet = statement.executeQuery(query);

	            //If there are no records, display a message
	            if(!resultSet.next()) {
	               JOptionPane.showMessageDialog(null,"No records found!");
	               return;
	            }
	            else {
	               // columnNames holds the column names of the query result      
	               Vector<Object> columnNames = new Vector<Object>(); 

	               // rows is a vector of vectors, each vector is a vector of
	               // values representing a certain row of the query result
	               Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
	 
	               // get column headers
	               ResultSetMetaData metaData = resultSet.getMetaData();

	               for(int i = 1; i <= metaData.getColumnCount(); ++i)
	                  columnNames.addElement(metaData.getColumnName(i));

	               // get row data
	               do {
	                  Vector<Object> currentRow = new Vector<Object>();
	                  for(int i = 1; i <= metaData.getColumnCount(); ++i)
	                     currentRow.addElement(resultSet.getObject(i));
	                  rows.addElement(currentRow);
	               } while(resultSet.next()); //moves cursor to next record
	               if(scroller != null) {
	                   getContentPane().remove(scroller);
	               }

	               // Display table with ResultSet contents
	               table = new JTable(rows, columnNames);
	              
	               table.setFillsViewportHeight(true); 

	               scroller = new JScrollPane(table);
	               scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	               
	               scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	            
	               getContentPane().add(scroller, BorderLayout.CENTER);
	               validate();
	               


	            }
	            statement.close();
	         }

	         catch(SQLException ex) {
	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
	         }
	      }
	   }

	   // inner class for handling window event
	   private class WindowHandler extends WindowAdapter
	   {
	      public void windowClosing(WindowEvent e)
	      {
	         try {
	            if(connection!=null)
	               connection.close();
	         }
	         catch(SQLException ex) {
	            JOptionPane.showMessageDialog(null, "Unable to disconnect!");
	         }
	         System.exit(0);
	      }
	   }


	}
