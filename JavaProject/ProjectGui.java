package guipackage;


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
	   JMenuBar menuBar;
	   JMenu menu;
	   JMenu history;
	   JMenuItem m1, m2, m3, m4, m5, h1;
	   
	   String role;
	   
	   String loggedInUserId;
	   private JButton loginButton;

	   private JPanel loginPanel;

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
		   
		   
		   loginButton = new JButton("Login");
		   LoginHandler lhandler = new LoginHandler();
		   loginButton.addActionListener(lhandler);
		   
		   
		   	loginPanel = new JPanel();
		    idLabel = new JLabel("User Id");
		    pwdLabel = new JLabel("Password");
		    idField = new JTextField(10);
		    pwdField = new JPasswordField(10);
		    idLabel.setForeground(Color.BLACK); 
		    pwdLabel.setForeground(Color.BLACK); 
		    WindowHandler window = new WindowHandler();
		    this.addWindowListener(window);
		    
		    loginPanel.setBackground(new Color(230, 230, 250)); 
		   
		    
		    loginButton.setBackground(new Color(173, 216, 230)); 
		    loginButton.setForeground(Color.BLACK);
		    
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
	      
	      
		// Inside the actionPerformed method of the LoginHandler class

		   public void actionPerformed(ActionEvent e) {
		       String id = idField.getText();
		       char[] p = pwdField.getPassword();
		       String pwd = new String(p);

		       try {
		           role = checkCredentials(id, pwd);

		           if ("ADMIN".equals(role)) {
		               initializeAdminGUI();
		           } else if ("MEMBER".equals(role)) {
		               initializeMemberGUI();
		           } else {
		               JOptionPane.showMessageDialog(null, "Invalid email or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
		           }
		       } catch (SQLException ex) {
		           JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
		       }
		   }

	    	  
	    	  
	      }
	   private String checkCredentials(String id, String pwd) throws SQLException {
		    // First, check if the user is an administrator
		    String queryAdmin = "SELECT 'ADMIN' as role FROM administrator WHERE email = ? AND password = ?";
		    String queryMember = "SELECT 'MEMBER' as role FROM member WHERE email = ? AND password = ?";

		    try (PreparedStatement stmtAdmin = connection.prepareStatement(queryAdmin)) {
		        stmtAdmin.setString(1, id);
		        stmtAdmin.setString(2, pwd);
		        
		        ResultSet resultSetAdmin = stmtAdmin.executeQuery();
		        if (resultSetAdmin.next()) {
		            return resultSetAdmin.getString("role"); // It will return "ADMIN"
		        }
		    }

		    // If not an admin, check if the user is a member
		    try (PreparedStatement stmtMember = connection.prepareStatement(queryMember)) {
		        stmtMember.setString(1, id);
		        stmtMember.setString(2, pwd);

		        ResultSet resultSetMember = stmtMember.executeQuery();
		        if (resultSetMember.next()) {
		            return resultSetMember.getString("role"); // It will return "MEMBER"
		        }
		    }

		    // If neither, return null
		    return null;
		}

	    	  
	    	  void initializeAdminGUI() {
	    	        //Initialize the GUI for the administrator
	    	        JOptionPane.showMessageDialog(MyFrameClass.this, "Logged in as Administrator", "Login Success", JOptionPane.INFORMATION_MESSAGE);
	    	       
	    	    }
	   
	   
	    	  void initializeMemberGUI() {
	    	        //Initialize the GUI for the member
	    	        JOptionPane.showMessageDialog(MyFrameClass.this, "Logged in as Member", "Login Success", JOptionPane.INFORMATION_MESSAGE);
	    	        
	    	        menuBar = new JMenuBar();
	    	        menu = new JMenu("Search By");
	    	        history = new JMenu("Streaming History");
	    	        
	    	        m1 = new JMenuItem("Title");
	    	        m2 = new JMenuItem("Genre");
	    	        m3 = new JMenuItem("Actor");
	    	        m4 = new JMenuItem("Director");
	    	        m5 = new JMenuItem("Prequel/Sequel");
	    	        h1 = new JMenuItem("View History");
	    	        
	    	        
	    	        
	    	        
	    	        
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
	    		   
	    		    
	    	        validate();
	    	        repaint();
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
