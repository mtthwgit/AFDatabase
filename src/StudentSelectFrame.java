import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Class which acts as a student to be selected from a GUI for an application
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class StudentSelectFrame extends JFrame {

	private static final long serialVersionUID = -2836613250046164622L;
	private JPanel contentPane;
	private AppFrame appFrame;
	private JList studentList;
	private DefaultListModel studentListModel = new DefaultListModel();
	private SQLConnection sql;
	private JFrame frame = this;
	private JTextField firstName;
	private JTextField lastName;
	private JSpinner studentID;
	/**
	 * Create the frame.
	 */
	public StudentSelectFrame(AppFrame appFrame) {
		
		try {
			frame.setIconImage((ImageIO.read(new File(".favicon.png"))));
	    } catch (IOException e) {
			e.printStackTrace();
	    }
		
		this.setTitle("Student Selector");

		this.appFrame = appFrame;

		sql = new SQLConnection("angelFundDatabase.db");

		setDefaultCloseOperation(StudentSelectFrame.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
						FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.GROWING_BUTTON_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),},
						new RowSpec[] {
										FormSpecs.RELATED_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.RELATED_GAP_ROWSPEC,
										RowSpec.decode("fill:default:grow"),
										FormSpecs.RELATED_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.RELATED_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.RELATED_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,}));

		JLabel lblSelectParent = new JLabel("Select Student");
		contentPane.add(lblSelectParent, "2, 2, 7, 1");

		studentList = new JList(studentListModel);
		contentPane.add(studentList, "2, 4, 7, 1, fill, fill");

		JLabel lblFirstName = new JLabel("First Name");
		contentPane.add(lblFirstName, "2, 6");

		JLabel lblLastName = new JLabel("Last Name");
		contentPane.add(lblLastName, "4, 6");

		JLabel lblStudentID = new JLabel("StudentID");
		contentPane.add(lblStudentID, "6, 6, 3, 1");

		firstName = new JTextField();
		contentPane.add(firstName, "2, 8, fill, default");
		firstName.setColumns(10);

		lastName = new JTextField();
		contentPane.add(lastName, "4, 8, fill, default");
		lastName.setColumns(10);

		studentID = new JSpinner();
		contentPane.add(studentID, "6, 8");

		JButton btnSearch = new JButton("Search");
		contentPane.add(btnSearch, "8, 8");

		btnSearch.addActionListener(new SearchAction());

		JButton btnCancel = new JButton("Cancel");
		contentPane.add(btnCancel, "6, 10");

		btnCancel.addActionListener(new CancelAction());

		JButton btnSelect = new JButton("Select");
		contentPane.add(btnSelect, "8, 10");

		btnSelect.addActionListener(new SelectAction());
	}

	/**
	 * Implementation of cancel button
	 */
	class CancelAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	/**
	 * Implementation of Select button
	 */
	class SelectAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Student selectedStudent = (Student) studentList.getSelectedValue();
			if(selectedStudent != null) {
				appFrame.updateFrameNewStudent(selectedStudent);
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		}

	}

	/**
	 * Implementation of Search button
	 */
	class SearchAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			studentListModel.clear();

			Map<Integer, Student> studentList;
			Map<String, Object> query = new HashMap<>();

			if(!firstName.getText().isBlank()) {
				query.put("firstName", firstName.getText());
			}
			if(!lastName.getText().isBlank()) {
				query.put("lastName", lastName.getText());
			}
			if((Integer) studentID.getValue() > 0) {
				query.put("studentID", (Integer) studentID.getValue());
			}

			studentList = sql.selectStudent(query);

			studentList.toString();
			for (Student s : studentList.values()) {
				studentListModel.addElement(s);
			}
		}

	}



}