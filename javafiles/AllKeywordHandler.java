
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

class AllKeywordHandler implements ActionListener {

	Connection connection;
	JTable table;
	DefaultTableModel model;
	JScrollPane scroller;
	JFrame myFrameClass;

	AllKeywordHandler(Connection connection, JTable table, DefaultTableModel model, JScrollPane scroller,
			JFrame myFrameClass) {
		this.connection = connection;
		this.table = table;
		this.model = model;
		this.scroller = scroller;
		this.myFrameClass = myFrameClass;
	}

	public void actionPerformed(ActionEvent e) {
		JTextField searchField = new JTextField(20);
		Object[] message = { "Enter title, genre, actor, or director:", searchField };

		int optionChosen = JOptionPane.showConfirmDialog(null, message, "Search by title, genre, actor, or director",
				JOptionPane.OK_CANCEL_OPTION);

		String searchText = searchField.getText();
		
		model.setRowCount(0);
		model.setColumnCount(0);
		
		if (optionChosen == JOptionPane.OK_OPTION && !searchText.isEmpty()) {
			
			try {
				String query = "Select V.title, V.genre, V.release_date, V.hyperlink, CD.name AS Director_Name, DC.name AS Actor_Name "
                        + "From video V natural join videodirector VD Join cast_director CD on VD.cast_id = CD.cast_id "
                        + "join video_cast VC on V.title = VC.title join cast_director DC on VC.cast_id = DC.cast_id "
                        + "Where V.title LIKE ? OR V.genre LIKE ? OR CD.name LIKE ? OR DC.name LIKE ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, "%" + searchText + "%");
				statement.setString(2, "%" + searchText + "%");
				statement.setString(3, "%" + searchText + "%");
				statement.setString(4, "%" + searchText + "%");

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
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
