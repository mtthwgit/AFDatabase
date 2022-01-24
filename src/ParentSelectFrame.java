import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTextField;
import javax.swing.JSpinner;

/**
 * @desc A class which allows a parent selection window from the student creator.
 */
public class ParentSelectFrame extends JFrame {

  private static final long serialVersionUID = 9019050320270880191L;
  private JPanel contentPane;
  private int studentID;
  @SuppressWarnings("rawtypes")
  private JList parentList;
  @SuppressWarnings("rawtypes")
  private DefaultListModel parentListModel = new DefaultListModel();
  private SQLConnection sql;
  private JFrame frame = this;
  private JTextField firstName;
  private JTextField lastName;
  private JSpinner parentID;

  /**
   * Create the frame.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
public ParentSelectFrame(int studentID) {
    
	this.studentID = studentID; 
	
	try {
		frame.setIconImage((ImageIO.read(new File(".favicon.png"))));
	} catch (IOException e) {
		e.printStackTrace();
    }
	
	this.setTitle("Parent Select");
	
	sql = new SQLConnection("angelFundDatabase.db");
	
    setDefaultCloseOperation(ParentSelectFrame.DISPOSE_ON_CLOSE);
	  
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
    
    JLabel lblSelectParent = new JLabel("Select Parent");
    contentPane.add(lblSelectParent, "2, 2, 7, 1");
    
    parentList = new JList(parentListModel);
    contentPane.add(parentList, "2, 4, 7, 1, fill, fill");
    
    JLabel lblFirstName = new JLabel("First Name");
    contentPane.add(lblFirstName, "2, 6");
    
    JLabel lblLastName = new JLabel("Last Name");
    contentPane.add(lblLastName, "4, 6");
    
    JLabel lblParentID = new JLabel("ParentID");
    contentPane.add(lblParentID, "6, 6, 3, 1");
    
    firstName = new JTextField();
    contentPane.add(firstName, "2, 8, fill, default");
    firstName.setColumns(10);
    
    lastName = new JTextField();
    contentPane.add(lastName, "4, 8, fill, default");
    lastName.setColumns(10);
    
    parentID = new JSpinner();
    contentPane.add(parentID, "6, 8");
    
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
   * Allows cancellation of parent select
   */
  class CancelAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
  }
  
  /**
   * Allows the selection of a parent
   */
  class SelectAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Parent selectedParent = (Parent) parentList.getSelectedValue();
		
		if (selectedParent != null) {
			sql.createParentStudent(studentID, selectedParent.getParentID());
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
	}
	  
  }
  
  /**
   * Allows the serach button to function
   */
  class SearchAction implements ActionListener {

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		
		parentListModel.clear();
		
		Map<Integer, Parent> parentList;
		Map<String, Object> query = new HashMap<>();
		
		if(!firstName.getText().isBlank()) {
			query.put("firstName", firstName.getText());
		}
		if(!lastName.getText().isBlank()) {
			query.put("lastName", lastName.getText());
		}
		if((Integer) parentID.getValue() > 0) {
			query.put("parentID", (Integer) parentID.getValue());
		}
		
	    parentList = sql.selectParent(query);

	    parentList.toString();
	    for (Parent p : parentList.values()) {
	      parentListModel.addElement(p);
	    }
	}
	  
  }
  
}
