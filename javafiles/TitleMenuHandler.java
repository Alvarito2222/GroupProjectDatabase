import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class TitleMenuHandler implements ActionListener {

	Connection connection;
	JTable table;
	DefaultTableModel model;
	JScrollPane scroller;
	JFrame myFrameClass;

	TitleMenuHandler(Connection connection, JTable table, DefaultTableModel model, JScrollPane scroller,
			JFrame myFrameClass) {
		this.connection = connection;
		this.table = table;
		this.model = model;
		this.scroller = scroller;
		this.myFrameClass = myFrameClass;
	}

	public void actionPerformed(ActionEvent e) {
		JTextField searchField = new JTextField(20);
		Object[] message = { "Enter Title:", searchField };

		int optionChosen = JOptionPane.showConfirmDialog(null, message, "Search by Title",
				JOptionPane.OK_CANCEL_OPTION);

		String searchText = searchField.getText();

		model.setRowCount(0);
		model.setColumnCount(0);

		if (optionChosen == JOptionPane.OK_OPTION && !searchText.isEmpty()) {

			try {
				String query = "SELECT title, genre , release_date , hyperlink , awards_won FROM video NATURAL JOIN movies  WHERE title LIKE ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, "%" + searchText + "%");

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
				table.getColumnModel().getColumn(4).setCellRenderer(new HyperlinkCellRenderer());
                addHyperlinkListener(table);

				myFrameClass.getContentPane().add(scroller, BorderLayout.CENTER);

				scroller.setViewportView(table);

				myFrameClass.validate();
				statement.close();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private void addHyperlinkListener(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (column == 3) { 
                    Object value = table.getValueAt(row, column);
                    if (value instanceof String && ((String) value).startsWith("http")) {
                        try {
                            Desktop.getDesktop().browse(new URI((String) value));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
