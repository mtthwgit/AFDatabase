import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import javax.swing.JTabbedPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @desc class which allows the displaying of parents
 */
public class ParentsFrame extends JFrame {

  private static final long serialVersionUID = -7160811888070326530L;
  private JTextField firstName;
  private JTextField lastName;
  private JTextField occupation;
  private JTextField phone;
  private JTextField parentID;

  private AddressPanel addressPanel;
  private JTextPane addressPreview;

  private Frame frame = this;
  private Parent parent;
  private SQLConnection sql;
  private int studentID;

  /**
   * Create the frame.
   */
  public ParentsFrame() {
	  
	try {
			frame.setIconImage((ImageIO.read(new File(".favicon.png"))));
	} catch (IOException e) {
			e.printStackTrace();
	}
	
	this.setTitle("Parent Editor");

    sql = new SQLConnection("angelFundDatabase.db");
    parent = new Parent();
    
    setDefaultCloseOperation(ParentsFrame.DISPOSE_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    getContentPane().add(tabbedPane, BorderLayout.CENTER);
    
    JPanel bioPanel = new JPanel();
    tabbedPane.addTab("Bio", null, bioPanel, null);
    bioPanel.setLayout(new FormLayout(new ColumnSpec[] {
        FormSpecs.UNRELATED_GAP_COLSPEC,
        ColumnSpec.decode("191px"),
        ColumnSpec.decode("31px"),
        ColumnSpec.decode("191px"),},
      new RowSpec[] {
        FormSpecs.UNRELATED_GAP_ROWSPEC,
        RowSpec.decode("14px"),
        FormSpecs.RELATED_GAP_ROWSPEC,
        RowSpec.decode("23px"),
        FormSpecs.UNRELATED_GAP_ROWSPEC,
        RowSpec.decode("14px"),
        FormSpecs.RELATED_GAP_ROWSPEC,
        RowSpec.decode("20px"),
        FormSpecs.RELATED_GAP_ROWSPEC,
        RowSpec.decode("14px"),
        FormSpecs.RELATED_GAP_ROWSPEC,
        RowSpec.decode("20px"),
        FormSpecs.RELATED_GAP_ROWSPEC,
        RowSpec.decode("14px"),
        FormSpecs.RELATED_GAP_ROWSPEC,
        RowSpec.decode("20px"),}));
    
    JLabel lblFirstName = new JLabel("First Name");
    bioPanel.add(lblFirstName, "2, 2, fill, top");
    
    JLabel lblAddress = new JLabel("Address");
    bioPanel.add(lblAddress, "4, 2, fill, top");
    
    firstName = new JTextField();
    lblFirstName.setLabelFor(firstName);
    firstName.setColumns(10);
    bioPanel.add(firstName, "2, 4, fill, bottom");
    
    addressPreview = new JTextPane();
    addressPreview.setEditable(false);
    lblAddress.setLabelFor(addressPreview);
    bioPanel.add(addressPreview, "4, 4, 1, 5, fill, fill");
    
    JLabel lblLastName = new JLabel("Last Name");
    bioPanel.add(lblLastName, "2, 6, fill, top");
    
    lastName = new JTextField();
    lblLastName.setLabelFor(lastName);
    lastName.setColumns(10);
    bioPanel.add(lastName, "2, 8, fill, top");
    
    JLabel lblOccupation = new JLabel("Occupation");
    bioPanel.add(lblOccupation, "2, 10, fill, top");
    
    JLabel lblPhone = new JLabel("Phone");
    bioPanel.add(lblPhone, "4, 10, fill, top");
    
    occupation = new JTextField();
    lblOccupation.setLabelFor(occupation);
    occupation.setColumns(10);
    bioPanel.add(occupation, "2, 12, fill, top");
    
    try {
    phone = new JFormattedTextField(new MaskFormatter("(###) ###-####"));
    lblPhone.setLabelFor(phone);
    bioPanel.add(phone, "4, 12, fill, default");
    phone.setColumns(10);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    JLabel lblParentID = new JLabel("Parent ID");
    bioPanel.add(lblParentID, "2, 14, 3, 1, fill, top");
    
    parentID = new JTextField();
    lblParentID.setLabelFor(parentID);
    parentID.setEditable(false);
    parentID.setColumns(10);
    bioPanel.add(parentID, "2, 16, 3, 1, fill, top");
    
    addressPanel = new AddressPanel();
    tabbedPane.addTab("Address", null, addressPanel, null);
    
    JMenuBar menuBar = new JMenuBar();
    getContentPane().add(menuBar, BorderLayout.NORTH);
    
    JMenuItem mntmSaveAndClose = new JMenuItem("Save And Close");
    menuBar.add(mntmSaveAndClose);
    
    mntmSaveAndClose.addActionListener(new SaveAndClose());
    
    JMenuItem mntmSave = new JMenuItem("Save");
    menuBar.add(mntmSave);
    
    mntmSave.addActionListener(new Save());
    
    JMenuItem mntmDelete = new JMenuItem("Delete");
    menuBar.add(mntmDelete);
    
    mntmDelete.addActionListener(new Delete());
  }
  
  
  /**
   * Constructor for parent frame
   * @param parent
   */
  public ParentsFrame(Parent parent) {
    this();

    this.parent = parent;
    
    firstName.setText(parent.getFirstName());
    lastName.setText(parent.getLastName());
    occupation.setText(parent.getOccupation());
    phone.setText(parent.getPhone());
    parentID.setText(parent.getParentID()+"");
    
    if(parent.getAddress() != null && parent.getAddressID() != 0) {
      Address address = parent.getAddress();
      addressPanel.updatePanel(address);
      addressPreview.setText(address.toString());
    }
  }
  
  /**
   * Save and close button
   */
  class SaveAndClose implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Save temp = new Save();
      temp.actionPerformed(null);

      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
  }
  
  /**
   * Allows save button
   */
  class Save implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(parentID.getText().length() == 0) {

        Address address = addressPanel.getNewAddress();
        int addressID = sql.createAddress(address);
        //Should add ability to add parents to student here.

        parent.setAddressID(addressID);
        parent.setFirstName(firstName.getText());
        parent.setLastName(lastName.getText());
        parent.setPhone(phone.getText());
        parent.setOccupation(occupation.getText());

        int id = sql.createParent(parent);
        sql.createParentStudent(studentID,id);

      } else {

        Address address = addressPanel.getAddress();
        sql.updateAddress(address);

        parent.setParentID(Integer.parseInt(parentID.getText()));
        parent.setAddressID(address.getAddressID());
        parent.setFirstName(firstName.getText());
        parent.setLastName(lastName.getText());
        parent.setPhone(phone.getText().replaceAll("[^0-9]",""));
        parent.setOccupation(occupation.getText());

        sql.updateParent(parent);
      }
    }
  }
  
  /**
   * Allows deleting of an object
   */
  class Delete implements ActionListener {
    public void actionPerformed(ActionEvent e) {
     
    }
  }

  // setter for student ID for database interaction
  public void setStudentID(int id) {studentID = id;}
}
