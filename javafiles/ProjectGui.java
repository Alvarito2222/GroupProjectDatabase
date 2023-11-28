

import java.awt.Color;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class ProjectGui {

	public static void main(String args[]) {

		System.out.println("Starting application.");

		new MyFrameClass();

		System.out.println("Done.");

	}
}

class MyFrameClass extends JFrame {

	private JLabel idLabel;
	private JLabel pwdLabel;
	private JTextField idField;
	private JPasswordField pwdField;
	JMenuBar menuBar;
	JMenu menu;
	JMenu history;
	JMenu topten; 
	JMenu allMoviesSeries;
	JMenu allMembers;
	JMenuItem m1, m2, m3, m4, m5, m6, h1;

	
	JPanel streamButtonPanel;
	private JButton streamButton;
	String role;

	String loggedInUserId;
	private JButton loginButton;

	private JPanel loginPanel;
	JPanel buttonPanel;

	JTable table;
	JScrollPane scroller;
	DefaultTableModel tableModel;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://data-people.csamxrafss3m.us-east-1.rds.amazonaws.com:3306/streamingservice?useLegacyDatetimeCode=false&serverTimezone=America/New_York";

	Connection connection;

	MyFrameClass() {
		setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));

		try {
			Class.forName(driver);

			connection = DriverManager.getConnection(url, "alvaro", "groupProject1");
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "Failed to load JDBC driver!");
			System.exit(1);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Access denied!");
			return;
		}

		table = new JTable() {
	        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	            Component c = super.prepareRenderer(renderer, row, column);
	            if (!isRowSelected(row)) {
	                c.setBackground(row % 2 == 0 ? new Color(230, 230, 250) : new Color(245, 245, 255)); // Light row colors
	                c.setForeground(Color.BLACK); // Text color
	            } else {
	                c.setBackground(new Color(173, 216, 230)); // Color when a row is selected
	            }
	            return c;
	        }
	    };
	    
	    JTableHeader tableHeader = table.getTableHeader();
	    tableHeader.setBackground(new Color(173, 216, 230)); // Light header background
	    tableHeader.setForeground(Color.BLACK); // Header text color
	    
		tableModel = new DefaultTableModel();

		scroller = new JScrollPane(table);
		
		scroller.getViewport().setBackground(new Color(230, 230, 250)); // Light viewport background
		
		table.setGridColor(new Color(173, 216, 230)); // Light grid lines

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.PAGE_AXIS));
		buttonPanel.setBackground(new Color(57, 100, 195)); // Match the loginPanel background
		loginButton = new JButton("Login");
		LoginHandler lhandler = new LoginHandler();
		onHover mouseAdapter = new onHover();
		loginButton.addActionListener(lhandler);
		loginButton.addMouseListener(mouseAdapter);
		
		
		streamButtonPanel = new JPanel();
		streamButtonPanel.setVisible(false);


		
		loginButton.setPreferredSize(new Dimension(80, 25)); // Adjust width and height as needed

		// Add the login button to the buttonPanel

		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setFont(new Font("Tahoma", Font.BOLD, 20));

		buttonPanel.add(add(Box.createRigidArea(new Dimension(5, 10))));
		buttonPanel.add(loginButton);
		buttonPanel.add(add(Box.createRigidArea(new Dimension(5, 10))));


		loginPanel = new JPanel();
		idLabel = new JLabel("  User Id");
		pwdLabel = new JLabel("  Password");
		idField = new JTextField(10);
		pwdField = new JPasswordField(10);
		idLabel.setForeground(Color.BLACK);
		pwdLabel.setForeground(Color.BLACK);
		WindowHandler window = new WindowHandler();
		this.addWindowListener(window);

		loginPanel.setBackground(new Color( 57, 100, 195));

		loginButton.setBackground(new Color(57, 100, 195));
		loginButton.setForeground(Color.BLACK);
		loginButton.setForeground(Color.WHITE);
		loginButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());



		loginPanel.setLayout(new GridLayout(4, 1, 0, 5));

		//STYLING

		//setUndecorated(true);

		loginPanel.setPreferredSize(new Dimension(100,100));

		idLabel.setHorizontalAlignment(JLabel.LEFT);
		idLabel.setForeground(Color.WHITE);
		idLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		idField.setHorizontalAlignment(JTextField.CENTER);
		idField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		idField.setPreferredSize(new Dimension(20,20));
		idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		idField.setForeground(Color.WHITE);
		idField.setBackground(new Color(69, 116, 222));

		pwdLabel.setHorizontalAlignment(JLabel.LEFT);
		pwdLabel.setForeground(Color.WHITE);
		pwdLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		pwdField.setHorizontalAlignment(JTextField.CENTER);
		pwdField.setFont(new Font("Tahoma", Font.BOLD, 20));
		pwdField.setPreferredSize(new Dimension(20,20));
		pwdField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		pwdField.setForeground(Color.WHITE);
		pwdField.setBackground(new Color(69, 116, 222));

		buttonPanel.add(add(Box.createRigidArea(new Dimension(100, 0))));
		loginPanel.add(idLabel);
		loginPanel.add(idField);
		loginPanel.add(pwdLabel);
		loginPanel.add(pwdField);
		
		streamButton = new JButton("Stream");
		streamButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        streamSelectedMovie();
		    }
		});

		// Panel to hold both the loginPanel and loginButton
		JPanel northPanel = new JPanel(new BorderLayout());

		northPanel.add(loginPanel, BorderLayout.CENTER);
		northPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Add the northPanel to the NORTH region of the BorderLayout
		add(northPanel, BorderLayout.NORTH);
		add(streamButtonPanel, BorderLayout.SOUTH);

		setupMainFrame();
	}

	void setupMainFrame() {

		Toolkit tk;
		Dimension d;

		tk = Toolkit.getDefaultToolkit();
		d = tk.getScreenSize();
		setSize(d.width / 8, d.height / 4);
		setLocation(d.width / 3, d.height / 3);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setTitle("DataPeopleGroupProject");

		setVisible(true);
	}

	public class onHover extends MouseAdapter{

		public void mouseEntered(MouseEvent e) {
			loginButton.setForeground(new Color(69, 116, 222));
		}
		public void mouseExited(MouseEvent e) {
			loginButton.setForeground(Color.WHITE);
		}
	}

	// inner class for handling login
	class LoginHandler implements ActionListener {

		

		public void actionPerformed(ActionEvent e) {
			String id = idField.getText();
			loggedInUserId = id;
			char[] p = pwdField.getPassword();
			String pwd = new String(p);

			try {
				role = checkCredentials(id, pwd);

				if ("ADMIN".equals(role)) {
					initializeAdminGUI();
				} else if ("MEMBER".equals(role)) {
					initializeMemberGUI();
				} else {
					JOptionPane.showMessageDialog(null, "Invalid email or password!", "Login Failed",
							JOptionPane.ERROR_MESSAGE);
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
		// Initialize the GUI for the administrator
		JOptionPane.showMessageDialog(MyFrameClass.this, "Logged in as Administrator", "Login Success",
				JOptionPane.INFORMATION_MESSAGE);

		Toolkit tk;
		Dimension d;

		tk = Toolkit.getDefaultToolkit();
		d = tk.getScreenSize();
		setSize(d.width / 3 , d.height / 3);
		setLocation(d.width / 3, d.height / 3);


		loginPanel.setVisible(false);
		buttonPanel.setVisible(false);

		menuBar = new JMenuBar();
		menu = new JMenu("Streams");
		history = new JMenu("Streaming trend 24h");
		topten = new JMenu("Top Ten");
		
	    allMoviesSeries = new JMenu("Movies / Series");
		allMembers = new JMenu("Members");
		

		StreamsMenuHandler streamsHandler = new StreamsMenuHandler(connection, table, tableModel, scroller, this);
		
		
		

		menu.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		history.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		topten.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		allMoviesSeries.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		allMembers.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		try {

			Statement statement = connection.createStatement();
			String query = "SELECT DISTINCT title FROM streams";
			ResultSet rs = statement.executeQuery(query);

			// Iterate over the results and add each movie as a menu item
			while (rs.next()) {
				String movieTitle = rs.getString("title");
				JMenuItem menuItem = new JMenuItem(movieTitle);

				menuItem.setActionCommand(movieTitle); // Set the action command to the movie title
				menuItem.addActionListener(streamsHandler);

				menu.add(menuItem);
				menu.add(new JSeparator()); // Add a separator between menu items
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error retrieving streaming data!", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
		
		
		JMenuItem recentStreams = new JMenuItem("Top Streams Today");
        history.add(recentStreams);
        DailyMenuHandler dailyHandler = new DailyMenuHandler(connection, table,tableModel, scroller, this);
        recentStreams.addActionListener(dailyHandler);

        JMenuItem topTenItem = new JMenuItem("Top Ten Streams");
        topten.add(topTenItem);
        TopTenHandler tenHandler = new TopTenHandler(connection, table,tableModel, scroller, this);
        topTenItem.addActionListener(tenHandler);
        
        JMenuItem allMemberes = new JMenuItem("Show All Members");
        allMembers.add(allMemberes);
        AdminHandler adminHandler = new AdminHandler(connection,table,tableModel,scroller,this);
        allMemberes.addActionListener(adminHandler);
        
        JMenuItem allMoviesandSeries = new JMenuItem("Show All Movies/Series");
        allMoviesSeries.add(allMoviesandSeries);
        allMoviesandSeries.addActionListener(adminHandler);
        
        
        
        


		menuBar.add(menu);
		menuBar.add(history);
		menuBar.add(topten);
		menuBar.add(allMoviesSeries);
		menuBar.add(allMembers);
		
		setJMenuBar(menuBar);

		validate();
		repaint();

	}

	void initializeMemberGUI() {
		// Initialize the GUI for the member
		JOptionPane.showMessageDialog(MyFrameClass.this, "Logged in as Member", "Login Success",
				JOptionPane.INFORMATION_MESSAGE);

		Toolkit tk;
		Dimension d;

		tk = Toolkit.getDefaultToolkit();
		d = tk.getScreenSize();
		setSize(d.width / 3 , d.height / 3);
		setLocation(d.width / 3, d.height / 3);

		loginPanel.setVisible(false);
		buttonPanel.setVisible(false);
		
		streamButtonPanel.add(streamButton);
		streamButtonPanel.setVisible(true);

		menuBar = new JMenuBar();
		menu = new JMenu("Search By");
		history = new JMenu("Streaming History");
		

		m1 = new JMenuItem("Title");
		m2 = new JMenuItem("Genre");
		m3 = new JMenuItem("Actor");
		m4 = new JMenuItem("Director");
		m5 = new JMenuItem("Prequel/Sequel");
		m6 = new JMenuItem("All");
		h1 = new JMenuItem("View History");

		TitleMenuHandler titleHandler = new TitleMenuHandler(connection, table, tableModel, scroller, this);
		GenreMenuHandler genreHandler = new GenreMenuHandler(connection, table, tableModel, scroller, this);
		ActorMenuHandler actorHandler = new ActorMenuHandler(connection, table, tableModel, scroller, this);
		DirectorMenuHandler directorHandler = new DirectorMenuHandler(connection, table, tableModel, scroller, this);
		HistoryMenuHandler historyHandler = new HistoryMenuHandler(connection, table, tableModel, scroller, this,
				loggedInUserId);
		SequelMenuHandler sequelHandler = new SequelMenuHandler(connection, table, tableModel, scroller, this);
		AllKeywordHandler allHandler = new AllKeywordHandler(connection, table, tableModel, scroller, this);

		m1.addActionListener(titleHandler);
		m2.addActionListener(genreHandler);
		m3.addActionListener(actorHandler);
		m4.addActionListener(directorHandler);
		m5.addActionListener(sequelHandler);
		m6.addActionListener(allHandler);
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
		menu.add(new JSeparator());
		menu.add(m6);

		history.add(h1);

		menu.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		history.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		menuBar.add(menu);
		menuBar.add(history);

		setJMenuBar(menuBar);

		validate();
		repaint();
	}
	
	private void streamSelectedMovie() 
	{
	    int selectedRow = table.getSelectedRow();
	    if (selectedRow >= 0) {
	        String movieTitle = tableModel.getValueAt(selectedRow, 0).toString(); 
	        JOptionPane.showMessageDialog(this, "Streaming: " + movieTitle);
	        String userEmail = loggedInUserId; 
	        insertIntoDatabase(movieTitle, userEmail);
	    } 
	    else 
	    {
	        JOptionPane.showMessageDialog(this, "Please select a movie to stream.");
	    }
	}

	private void insertIntoDatabase(String movieTitle, String userEmail) {
	    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

	    Connection conn = null;
	    PreparedStatement stmt1 = null;
	    PreparedStatement stmt2 = null;

	    try {
	        conn = DriverManager.getConnection(url, "alvaro", "groupProject1");
	        conn.setAutoCommit(false); // Start transaction
	        
	        String timestamp = generateUniqueTimestampID();

	        // Insert into timestamp table
	        String sqlTimestamp = "INSERT INTO timestamp (timeStampID, stream_datetime) VALUES (?, ?)";
	        stmt1 = conn.prepareStatement(sqlTimestamp);
	        stmt1.setString(1, timestamp); // timeStampID
	        stmt1.setTimestamp(2, currentTimestamp);
	        stmt1.executeUpdate();

	        // Insert into streams table
	        String sqlStreams = "INSERT INTO streams (title, timeStampID, email) VALUES (?, ?, ?)";
	        stmt2 = conn.prepareStatement(sqlStreams);
	        stmt2.setString(1, movieTitle);
	        stmt2.setString(2, timestamp); //timeStampID
	        stmt2.setString(3, loggedInUserId);
	        stmt2.executeUpdate();

	        conn.commit(); // Commit transaction
	    } catch (SQLException e) {
	        if (conn != null) {
	            try {
	                conn.rollback(); // Rollback transaction in case of error
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	    } finally {
	        // Clean-up
	        try {
	            if (stmt1 != null) stmt1.close();
	            if (stmt2 != null) stmt2.close();
	            if (conn != null) conn.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	private String generateUniqueTimestampID() {
	    // Format the current time to a specific pattern
	    SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssS");
	    String timeComponent = dateFormat.format(new Date());

	    // Ensure the string is exactly 7 characters long
	    if (timeComponent.length() > 7) {
	        timeComponent = timeComponent.substring(0, 7);
	    } else {
	        while (timeComponent.length() < 7) {
	            // Append a random digit if the string is shorter than 7 characters
	            timeComponent += ThreadLocalRandom.current().nextInt(0, 10);
	        }
	    }

	    System.out.println(timeComponent);
	    return timeComponent;
	}

	// inner class for handling window event
	private class WindowHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Unable to disconnect!");
			}
			System.exit(0);
		}
	}

}




