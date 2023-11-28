
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

		StreamsMenuHandler streamsHandler = new StreamsMenuHandler(connection, table, tableModel, scroller, this);

		menu.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		history.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		topten.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
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


		menuBar.add(menu);
		menuBar.add(history);
		menuBar.add(topten);
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



