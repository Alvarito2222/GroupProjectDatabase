

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

class ActorMenuHandler implements ActionListener {
	Connection connection;
	JTable table;
	JScrollPane scroller;
	JFrame myFrameClass;
	DefaultTableModel model;

	ActorMenuHandler(Connection connection, JTable table, DefaultTableModel model, JScrollPane scroller,
			JFrame myFrameClass) {
		this.connection = connection;
		this.table = table;
		this.scroller = scroller;
		this.model = model;
		this.myFrameClass = myFrameClass;
	}
	

	public void actionPerformed(ActionEvent e) {
        JTextField searchField = new JTextField(20);
        Object[] message = { "Enter Actor Name:", searchField };

        int optionChosen = JOptionPane.showConfirmDialog(null, message, "Search by Actor",
                JOptionPane.OK_CANCEL_OPTION);

        String searchText = searchField.getText();

        model.setRowCount(0);
        model.setColumnCount(0);

        if (optionChosen == JOptionPane.OK_OPTION && !searchText.isEmpty()) {
            try {
                String query = "SELECT VC.title, name, genre, release_date, hyperlink FROM video_cast VC NATURAL JOIN cast_director JOIN video ON VC.title = video.title WHERE position = 'Actor' AND name LIKE ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, "%" + searchText + "%");

                ResultSet resultSet = statement.executeQuery();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                model.setRowCount(0);
                model.setColumnCount(0);
                for (int i = 1; i <= columnCount; ++i) {
                    model.addColumn(metaData.getColumnName(i));
                }

                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; ++i) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    model.addRow(row);
                }

                if (scroller != null) {
                    myFrameClass.getContentPane().remove(scroller);
                }

                table.setModel(model);
                table.setFillsViewportHeight(true);
                table.getColumnModel().getColumn(4).setCellRenderer(new HyperlinkCellRenderer());
                addHyperlinkListener(table);

                scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                myFrameClass.getContentPane().add(scroller, BorderLayout.CENTER);
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

                if (column == 4) { // Replace with the actual index of your hyperlink column
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
