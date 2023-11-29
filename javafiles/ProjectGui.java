


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
	JMenu editProfile;
	JMenu topten;
	JMenu allMoviesSeries;
	JMenu allMembers;
	JMenu loginOut;
	JMenuItem m1, m2, m3, m4, m5, m6, m7, h1, e1, loginOutConfirm;

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
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

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
					c.setBackground(row % 2 == 0 ? new Color(230, 230, 250) : new Color(245, 245, 255)); // Light row
					// colors
					c.setForeground(Color.BLACK); // Text color
				} else {
					c.setBackground(new Color(173, 216, 230)); // Color when a row is selected
				}
				return c;
			}
		};

		JTableHeader tableHeader = table.getTableHeader();
		// tableHeader.setBackground(new Color(173, 216, 230)); // Light header
		// background
		tableHeader.setForeground(Color.BLACK); // Header text color

		tableModel = new DefaultTableModel();

		scroller = new JScrollPane(table);

		scroller.getViewport().setBackground(new Color(230, 230, 250)); // Light viewport background

		table.setGridColor(new Color(173, 216, 230)); // Light grid lines

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
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

		loginPanel.setBackground(new Color(57, 100, 195));

		loginButton.setBackground(new Color(57, 100, 195));
		loginButton.setForeground(Color.BLACK);
		loginButton.setForeground(Color.WHITE);
		loginButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		loginPanel.setLayout(new GridLayout(4, 1, 0, 5));

		// STYLING
		tableHeader.setBackground(new Color(57, 100, 195));
		tableHeader.setForeground(Color.WHITE);
		tableHeader.setFont(new Font("Tahoma", Font.BOLD, 12));

		// setUndecorated(true);

		loginPanel.setPreferredSize(new Dimension(100, 100));

		idLabel.setHorizontalAlignment(JLabel.LEFT);
		idLabel.setForeground(Color.WHITE);
		idLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		idField.setHorizontalAlignment(JTextField.CENTER);
		idField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		idField.setPreferredSize(new Dimension(20, 20));
		idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		idField.setForeground(Color.WHITE);
		idField.setBackground(new Color(69, 116, 222));

		pwdLabel.setHorizontalAlignment(JLabel.LEFT);
		pwdLabel.setForeground(Color.WHITE);
		pwdLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		pwdField.setHorizontalAlignment(JTextField.CENTER);
		pwdField.setFont(new Font("Tahoma", Font.BOLD, 20));
		pwdField.setPreferredSize(new Dimension(20, 20));
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

	public class onHover extends MouseAdapter {

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
		setSize(d.width / 3, d.height / 3);
		setLocation(d.width / 3, d.height / 3);

		loginPanel.setVisible(false);
		buttonPanel.setVisible(false);

		menuBar = new JMenuBar();
		menu = new JMenu("Streams");
		history = new JMenu("Streaming trend 24h");
		topten = new JMenu("Top Ten / No Streams");

		allMoviesSeries = new JMenu("Movies / Series");
		allMembers = new JMenu("Members");

		menuBar.setBackground(new Color(69, 116, 222));
		menuBar.setFont(new Font("Tahoma", Font.BOLD, 12));
		menu.setForeground(Color.WHITE);
		menu.setFont(new Font("Tahoma", Font.BOLD, 12));
		menu.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		history.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		history.setForeground(Color.WHITE);
		history.setFont(new Font("Tahoma", Font.BOLD, 12));
		topten.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		topten.setForeground(Color.WHITE);
		topten.setFont(new Font("Tahoma", Font.BOLD, 12));
		loginOut = new JMenu("Login Out");
		loginOutConfirm = new JMenuItem("Confirm Log Out?");

		allMoviesSeries.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		allMoviesSeries.setForeground(Color.WHITE);
		allMoviesSeries.setFont(new Font("Tahoma", Font.BOLD, 12));
		allMembers.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		allMembers.setForeground(Color.WHITE);
		allMembers.setFont(new Font("Tahoma", Font.BOLD, 12));

		// Styling
		loginOut.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		loginOut.setForeground(Color.WHITE);
		loginOut.setFont(new Font("Tahoma", Font.BOLD, 12));
		loginOutConfirm.setForeground(Color.WHITE);
		loginOutConfirm.setBackground(new Color(69, 116, 222));
		loginOutConfirm.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		loginOutConfirm.setFont(new Font("Tahoma", Font.BOLD, 12));

		StreamsMenuHandler streamsHandler = new StreamsMenuHandler(connection, table, tableModel, scroller, this);

		menu.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		history.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		topten.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		loginOut.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		allMoviesSeries.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		allMembers.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JMenuItem menuItem = new JMenuItem("Streams");
		menuItem.addActionListener(streamsHandler);
		menuItem.setForeground(Color.WHITE);
		menuItem.setBackground(new Color(69, 116, 222));
		menuItem.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		menuItem.setFont(new Font("Tahoma", Font.BOLD, 12));
		menu.add(menuItem);

		JMenuItem noStreamItem = new JMenuItem("Titles With 0 Streams");
		noStreamItem.setForeground(Color.WHITE);
		noStreamItem.setBackground(new Color(69, 116, 222));
		noStreamItem.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		noStreamItem.setFont(new Font("Tahoma", Font.BOLD, 12));
		topten.add(noStreamItem);
		NoStreamHandler noHandler = new NoStreamHandler(connection, table, tableModel, scroller, this);
		noStreamItem.addActionListener(noHandler);

		JMenuItem recentStreams = new JMenuItem("Top Streams Today");
		recentStreams.setForeground(Color.WHITE);
		recentStreams.setBackground(new Color(69, 116, 222));
		recentStreams.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		recentStreams.setFont(new Font("Tahoma", Font.BOLD, 12));
		history.add(recentStreams);
		DailyMenuHandler dailyHandler = new DailyMenuHandler(connection, table, tableModel, scroller, this);
		recentStreams.addActionListener(dailyHandler);

		JMenuItem topTenItem = new JMenuItem("Top Ten Streams");
		topTenItem.setForeground(Color.WHITE);
		topTenItem.setBackground(new Color(69, 116, 222));
		topTenItem.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		topTenItem.setFont(new Font("Tahoma", Font.BOLD, 12));
		topten.add(topTenItem);
		TopTenHandler tenHandler = new TopTenHandler(connection, table, tableModel, scroller, this);
		topTenItem.addActionListener(tenHandler);

		JMenuItem allMemberes = new JMenuItem("Show All Members");
		allMemberes.setForeground(Color.WHITE);
		allMemberes.setBackground(new Color(69, 116, 222));
		allMemberes.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		allMemberes.setFont(new Font("Tahoma", Font.BOLD, 12));
		allMembers.add(allMemberes);
		AdminHandler adminHandler = new AdminHandler(connection, table, tableModel, scroller, this);
		allMemberes.addActionListener(adminHandler);

		JMenuItem allMoviesandSeries = new JMenuItem("Show All Movies/Series");
		allMoviesandSeries.setForeground(Color.WHITE);
		allMoviesandSeries.setBackground(new Color(69, 116, 222));
		allMoviesandSeries.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		allMoviesandSeries.setFont(new Font("Tahoma", Font.BOLD, 12));
		allMoviesSeries.add(allMoviesandSeries);
		allMoviesandSeries.addActionListener(adminHandler);

		LoginOutHandler logHandler = new LoginOutHandler(connection, table, tableModel, scroller, this, loggedInUserId);
		loginOutConfirm.addActionListener(logHandler);

		menuBar.add(menu);
		menuBar.add(history);
		menuBar.add(topten);
		menuBar.add(allMoviesSeries);
		menuBar.add(allMembers);
		loginOut.add(loginOutConfirm);
		menuBar.add(loginOut);

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
		setSize(d.width / 3, d.height / 3);
		setLocation(d.width / 3, d.height / 3);

		loginPanel.setVisible(false);
		buttonPanel.setVisible(false);

		streamButtonPanel.add(streamButton);
		streamButtonPanel.setVisible(true);

		menuBar = new JMenuBar();
		menu = new JMenu("Search By");
		history = new JMenu("Streaming History");
		editProfile = new JMenu("Edit Profile");

		m1 = new JMenuItem("Title");
		m2 = new JMenuItem("Genre");
		m3 = new JMenuItem("Actor");
		m4 = new JMenuItem("Director");
		m5 = new JMenuItem("Prequel/Sequel");
		m6 = new JMenuItem("All");
		m7 = new JMenuItem("Awards won");
		h1 = new JMenuItem("View History");
		e1 = new JMenuItem("Edit Profile");
		loginOut = new JMenu("Login Out");
		loginOutConfirm = new JMenuItem("Confirm Log Out?");

		// STYLING
		menuBar.setBackground(new Color(69, 116, 222));
		menu.setForeground(Color.WHITE);
		menu.setFont(new Font("Tahoma", Font.BOLD, 12));
		menu.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		history.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		history.setForeground(Color.WHITE);
		history.setFont(new Font("Tahoma", Font.BOLD, 12));
		loginOut.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		loginOut.setForeground(Color.WHITE);
		loginOut.setFont(new Font("Tahoma", Font.BOLD, 12));
		editProfile.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		editProfile.setForeground(Color.WHITE);
		editProfile.setFont(new Font("Tahoma", Font.BOLD, 12));
		m1.setForeground(Color.WHITE);
		m1.setBackground(new Color(69, 116, 222));
		m1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		m1.setFont(new Font("Tahoma", Font.BOLD, 12));
		m2.setForeground(Color.WHITE);
		m2.setBackground(new Color(69, 116, 222));
		m2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		m2.setFont(new Font("Tahoma", Font.BOLD, 12));
		m3.setForeground(Color.WHITE);
		m3.setBackground(new Color(69, 116, 222));
		m3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		m3.setFont(new Font("Tahoma", Font.BOLD, 12));
		m4.setForeground(Color.WHITE);
		m4.setBackground(new Color(69, 116, 222));
		m4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		m4.setFont(new Font("Tahoma", Font.BOLD, 12));
		m5.setForeground(Color.WHITE);
		m5.setBackground(new Color(69, 116, 222));
		m5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		m5.setFont(new Font("Tahoma", Font.BOLD, 12));
		m6.setForeground(Color.WHITE);
		m6.setBackground(new Color(69, 116, 222));
		m6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		m6.setFont(new Font("Tahoma", Font.BOLD, 12));
		m7.setForeground(Color.WHITE);
		m7.setBackground(new Color(69, 116, 222));
		m7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		m7.setFont(new Font("Tahoma", Font.BOLD, 12));
		h1.setForeground(Color.WHITE);
		h1.setBackground(new Color(69, 116, 222));
		h1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		h1.setFont(new Font("Tahoma", Font.BOLD, 12));
		e1.setForeground(Color.WHITE);
		e1.setBackground(new Color(69, 116, 222));
		e1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		e1.setFont(new Font("Tahoma", Font.BOLD, 12));
		loginOutConfirm.setForeground(Color.WHITE);
		loginOutConfirm.setBackground(new Color(69, 116, 222));
		loginOutConfirm.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		loginOutConfirm.setFont(new Font("Tahoma", Font.BOLD, 12));

		// Button Stlying
		streamButton.setBackground(new Color(57, 100, 195));
		streamButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		streamButton.setForeground(Color.BLACK);
		streamButton.setForeground(Color.WHITE);
		streamButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
				BorderFactory.createLineBorder(new Color(57, 100, 195), 12)));

		TitleMenuHandler titleHandler = new TitleMenuHandler(connection, table, tableModel, scroller, this);
		GenreMenuHandler genreHandler = new GenreMenuHandler(connection, table, tableModel, scroller, this);
		ActorMenuHandler actorHandler = new ActorMenuHandler(connection, table, tableModel, scroller, this);
		DirectorMenuHandler directorHandler = new DirectorMenuHandler(connection, table, tableModel, scroller, this);
		HistoryMenuHandler historyHandler = new HistoryMenuHandler(connection, table, tableModel, scroller, this,
				loggedInUserId);
		SequelMenuHandler sequelHandler = new SequelMenuHandler(connection, table, tableModel, scroller, this);
		AllKeywordHandler allHandler = new AllKeywordHandler(connection, table, tableModel, scroller, this);
		AwardsHandler awardsHandler = new AwardsHandler(connection, table, tableModel, scroller, this);
		EditProfileHandler editHandler = new EditProfileHandler(loggedInUserId, connection);
		LoginOutHandler logHandler = new LoginOutHandler(connection, table, tableModel, scroller, this, loggedInUserId);

		m1.addActionListener(titleHandler);
		m2.addActionListener(genreHandler);
		m3.addActionListener(actorHandler);
		m4.addActionListener(directorHandler);
		m5.addActionListener(sequelHandler);
		m6.addActionListener(allHandler);
		m7.addActionListener(awardsHandler);
		h1.addActionListener(historyHandler);
		e1.addActionListener(editHandler);
		loginOutConfirm.addActionListener(logHandler);

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
		menu.add(new JSeparator());
		menu.add(m7);

		history.add(h1);
		editProfile.add(e1);
		loginOut.add(loginOutConfirm);

		menu.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		history.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		editProfile.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		menuBar.add(menu);
		menuBar.add(history);
		menuBar.add(editProfile);
		menuBar.add(loginOut);

		setJMenuBar(menuBar);

		validate();
		repaint();
	}

	private void streamSelectedMovie() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0) {
			String movieTitle = tableModel.getValueAt(selectedRow, 0).toString();
			JOptionPane.showMessageDialog(this, "Streaming: " + movieTitle);
			String userEmail = loggedInUserId;
			insertIntoDatabase(movieTitle, userEmail);
		} else {
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
			stmt2.setString(2, timestamp); // timeStampID
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
				if (stmt1 != null)
					stmt1.close();
				if (stmt2 != null)
					stmt2.close();
				if (conn != null)
					conn.close();
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
