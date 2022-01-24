import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import javax.swing.JTabbedPane;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * @desc Class which allows visual interface with student objects as well as editing of said objects
 */
@SuppressWarnings("rawtypes")
public class StudentFrame extends JFrame {

  private static final long serialVersionUID = -6792881554410704537L;
  private JFrame frame;
  private JPanel contentPane;
  private JTextField firstName;
  private JTextField lastName;
  private JFormattedTextField phone_1;
  private JFormattedTextField phone_2;
  private JTextField studentID;
  private JTextPane addressPreview;

  private SQLConnection sql;
  private Student student;
  private AddressPanel addressPanel;
  
  private JList parentList;
  private JList appList;
  private DefaultListModel parentListModel = new DefaultListModel();
  private DefaultListModel appListModel = new DefaultListModel();
  private AppFrame applicationFrame;

  /**
   * Create the frame.
   */
  @SuppressWarnings("unchecked")
  public StudentFrame() {

    frame = this;
    sql = new SQLConnection("angelFundDatabase.db");
    student = new Student();
    
    try {
		frame.setIconImage((ImageIO.read(new File(".favicon.png"))));
    } catch (IOException e) {
		e.printStackTrace();
    }

    frame.setTitle("Students Editor");
    
    setDefaultCloseOperation(StudentFrame.DISPOSE_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    
    JMenuItem mntmSaveAndClose = new JMenuItem("Save And Close");
    menuBar.add(mntmSaveAndClose);
    
    mntmSaveAndClose.addActionListener(new SaveAndClose());
    
    JMenuItem mntmSave = new JMenuItem("Save");
    menuBar.add(mntmSave);
    
    mntmSave.addActionListener(new Save());
    
    JMenuItem mntmDelete = new JMenuItem("Delete");
    menuBar.add(mntmDelete);
    
    mntmDelete.addActionListener(new Delete());
    
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);
    
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    contentPane.add(tabbedPane, BorderLayout.CENTER);
    
    JPanel bioPanel = new JPanel();
    tabbedPane.addTab("Bio", null, bioPanel, null);
    bioPanel.setLayout(new FormLayout(new ColumnSpec[] {
        FormSpecs.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"),
        FormSpecs.RELATED_GAP_COLSPEC,
        FormSpecs.DEFAULT_COLSPEC,
        FormSpecs.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"),},
      new RowSpec[] {
        FormSpecs.RELATED_GAP_ROWSPEC,
        FormSpecs.DEFAULT_ROWSPEC,
        FormSpecs.RELATED_GAP_ROWSPEC,
        RowSpec.decode("default:grow"),
        FormSpecs.RELATED_GAP_ROWSPEC,
        FormSpecs.DEFAULT_ROWSPEC,
        FormSpecs.RELATED_GAP_ROWSPEC,
        FormSpecs.DEFAULT_ROWSPEC,
        FormSpecs.RELATED_GAP_ROWSPEC,
        FormSpecs.DEFAULT_ROWSPEC,
        FormSpecs.RELATED_GAP_ROWSPEC,
        FormSpecs.DEFAULT_ROWSPEC,
        FormSpecs.RELATED_GAP_ROWSPEC,
        FormSpecs.DEFAULT_ROWSPEC,
        FormSpecs.RELATED_GAP_ROWSPEC,
        FormSpecs.DEFAULT_ROWSPEC,}));
    
    JLabel lblFirstName = new JLabel("First Name");
    bioPanel.add(lblFirstName, "2, 2");
    
    JLabel lblAddress = new JLabel("Address");
    bioPanel.add(lblAddress, "6, 2");
    
    firstName = new JTextField();
    lblFirstName.setLabelFor(firstName);
    bioPanel.add(firstName, "2, 4, fill, default");
    firstName.setColumns(10);
    
    addressPreview = new JTextPane();
    addressPreview.setEditable(false);
    lblAddress.setLabelFor(addressPreview);
    bioPanel.add(addressPreview, "6, 4, 1, 5, fill, fill");
    
    JLabel lblLastName = new JLabel("Last Name");
    bioPanel.add(lblLastName, "2, 6");
    
    lastName = new JTextField();
    lblLastName.setLabelFor(lastName);
    bioPanel.add(lastName, "2, 8, fill, default");
    lastName.setColumns(10);
    
    JLabel lblPhone_1 = new JLabel("Phone 1");
    bioPanel.add(lblPhone_1, "2, 10");
    
    JLabel lblPhone_2 = new JLabel("Phone 2");
    bioPanel.add(lblPhone_2, "6, 10");
    
    try {
    phone_1 = new JFormattedTextField(new MaskFormatter("(###) ###-####"));
    lblPhone_1.setLabelFor(phone_1);
    bioPanel.add(phone_1, "2, 12, fill, default");
    phone_1.setColumns(10);
    
    phone_2 = new JFormattedTextField(new MaskFormatter("(###) ###-####"));
    lblPhone_2.setLabelFor(phone_2);
    bioPanel.add(phone_2, "6, 12, fill, default");
    phone_2.setColumns(10);
    
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    JLabel lblStudentID = new JLabel("Student ID");
    bioPanel.add(lblStudentID, "2, 14, 5, 1");
    
    studentID = new JTextField();
    lblStudentID.setLabelFor(studentID);
    studentID.setEditable(false);
    bioPanel.add(studentID, "2, 16, 5, 1, fill, default");
    studentID.setColumns(10);
    
    addressPanel = new AddressPanel();
    tabbedPane.addTab("Address", null, addressPanel, null);
    
    JPanel parentPanel = new JPanel();
    tabbedPane.addTab("Parents", null, parentPanel, null);
    parentPanel.setLayout(new BorderLayout(0, 0));
    
    parentList = new JList(parentListModel);
    parentPanel.add(parentList, BorderLayout.CENTER);
    
    JMenuBar parentMenu = new JMenuBar();
    parentPanel.add(parentMenu, BorderLayout.NORTH);
    
    JMenuItem mntmOpenParent = new JMenuItem("Open Parent");
    parentMenu.add(mntmOpenParent);
    
    mntmOpenParent.addActionListener(new OpenParentAction());
    
    JMenuItem mntmAddParent = new JMenuItem("Add Parent");
    parentMenu.add(mntmAddParent);
    
    mntmAddParent.addActionListener(new AddParentAction());
    
    JMenuItem mntmNewParent = new JMenuItem("New Parent");
    parentMenu.add(mntmNewParent);
    
    mntmNewParent.addActionListener(new NewParentAction());
    
    JMenuItem mntmDeleteParent = new JMenuItem("Delete");
    parentMenu.add(mntmDeleteParent);
    
    mntmDeleteParent.addActionListener(new DeleteParentAction());
    
    JPanel appPanel = new JPanel();
    tabbedPane.addTab("Applications", null, appPanel, null);
    appPanel.setLayout(new BorderLayout(0, 0));
    
    appList = new JList(appListModel);
    appPanel.add(appList, BorderLayout.CENTER);
    
    JMenuBar appMenu = new JMenuBar();
    appPanel.add(appMenu, BorderLayout.NORTH);
    
    JMenuItem mntmOpenApp = new JMenuItem("Open Application");
    appMenu.add(mntmOpenApp);
    
    mntmOpenApp.addActionListener(new OpenAppAction());
    
    JMenuItem mntmDeleteApp = new JMenuItem("Delete");
    appMenu.add(mntmDeleteApp);
    
    mntmDeleteApp.addActionListener(new DeleteAppAction());

  }
  
  public StudentFrame(Student student) {
    
    this();
    
    this.student = student;
    
    firstName.setText(student.getFirstName());
    lastName.setText(student.getLastName());
    phone_1.setText(student.getPhone1());
    phone_2.setText(student.getPhone2());
    studentID.setText(Integer.toString(student.getStudentID()));
    
    if(student.getAddress() != null && student.getAddressID() > 0) {
      Address address = sql.selectAddress(student.getAddressID());
      addressPanel.updatePanel(address);
      addressPreview.setText(address.toString());
    }
    
    queryParents();
    queryApps();
  }
  
  @SuppressWarnings("unchecked")
  private void queryParents() {
	
	parentListModel.clear();
    
    Map<Integer, Parent> parentList;
    
    Map<String, Object> query = new HashMap<>();
    
    if(student != null && student.getStudentID() > 0) {
      query.put("studentID", student.getStudentID());
    }
    
    parentList = sql.selectParent(query);

    parentList.toString();
    for (Parent p : parentList.values()) {
      parentListModel.addElement(p);
    }
  }
  
  @SuppressWarnings("unchecked")
  private void queryApps() {
	  
	appListModel.clear();
    
    Map<Integer, Application> appList;
    
    Map<String, Object> query = new HashMap<>();
    
    if(student != null && student.getStudentID() > 0) {
      query.put("studentID", student.getStudentID());
    }
    
    appList = sql.selectApplication(query);

    appList.toString();
    for (Application a : appList.values()) {
      appListModel.addElement(a);
    }
  
  }
  
  /**
   * Allows parent object to be deleted
   */
  class DeleteParentAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Parent parent = (Parent) parentList.getSelectedValue();
      if(parent != null) {
    	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + parent.getFirstName() + " " + parent.getLastName(), "Delete Parent", JOptionPane.YES_NO_OPTION);
    	if (reply == JOptionPane.YES_OPTION) {
    		sql.delete("parent",parent.getParentID());
    		parentListModel.remove(parentList.getSelectedIndex());
    	}
      }
      queryParents();
    }
  }
 
  /**
   * Allows parent to be created while creating a new student
   */
  class NewParentAction implements ActionListener {
    
    public void actionPerformed(ActionEvent e) {
      if (!(student.getStudentID() > 0)) {
        JOptionPane.showMessageDialog(null, "Please save before creating a new Parent.");
        return;
      }
      ParentsFrame temp = new ParentsFrame();
      temp.setVisible(true);

      String temp1 = studentID.getText();
      temp1 = temp1.replace(" ","");
      if(temp1.length() > 0) {
        temp.setStudentID(Integer.parseInt(temp1));
      }

      queryParents();
    }
  }
  
  /**
   * Allows parent to be added by selection from the student creator
   */
  class AddParentAction implements ActionListener {
    
    public void actionPerformed(ActionEvent e) {
        if (!(student.getStudentID() > 0)) {
            JOptionPane.showMessageDialog(null, "Please save before creating a new Parent.");
            return;
          }
        
        ParentSelectFrame temp = new ParentSelectFrame(student.getStudentID());
        temp.setVisible(true);
    }
  }
  
  /**
   * Allows the parent viewer to be opened from the student creator
   */
  class OpenParentAction implements ActionListener {
    
    public void actionPerformed(ActionEvent e) {
      if(parentList.getSelectedIndex() > -1) {
        Parent parent = (Parent) parentList.getSelectedValue();
        new ParentsFrame(parent).setVisible(true);
      }
    }
  }
  
  /**
   * Allows an application to be deleted from the student creator
   */
  class DeleteAppAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    	Application app = (Application) appList.getSelectedValue();
    	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Application", "Delete Application", JOptionPane.YES_NO_OPTION);
    	if (reply == JOptionPane.YES_OPTION) {
    		sql.delete("application", app.getApplicationID());
    	}
      
    }
  }
  
  /**
   * Allows an application to be viewed from the student creator
   */
  class OpenAppAction implements ActionListener {
    
    public void actionPerformed(ActionEvent e) {
      if(appList.getSelectedIndex() > -1) {
        Application app = (Application) appList.getSelectedValue();
        new AppFrame(app).setVisible(true);
      }
    }
  }
  
  /**
   * Implementation of save and close button
   */
  class SaveAndClose implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(firstName.getText().isEmpty() || lastName.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "An Application requires a Student!");
        return;
      }
      Save s = new Save();
      s.actionPerformed(null);

      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
  }
  
  /**
   * Implementation of save button (does not close)
   */
  class Save implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    	
      if(firstName.getText().isEmpty() || lastName.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "An Application requires a Student!");
      	return;
      }

      //if there is no student ID then it is a new student.
      if(studentID.getText().length() == 0) {

        Address address = addressPanel.getNewAddress();
        int addressID = sql.createAddress(address);
        //Should add ability to add parents to student here.

        student = new Student();
        student.setAddressID(addressID);
        student.setFirstName(firstName.getText());
        student.setLastName(lastName.getText());
        student.setPhone1(phone_1.getText().replaceAll("[^0-9]",""));
        student.setPhone2(phone_2.getText().replaceAll("[^0-9]",""));

        int id = sql.createStudent(student);

        studentID.setText(id+"");
        student.setStudentID(id);

      } else {

        Address address;
        if(addressPanel.getAddress().getAddressID() > 0) {
          address = addressPanel.getAddress();
          sql.updateAddress(address);
        } else {
          address = addressPanel.getNewAddress();
          int id = sql.createAddress(address);
          address.setAddressID(id);
        }

        student = new Student();
        student.setStudentID(Integer.parseInt(studentID.getText()));
        student.setAddressID(address.getAddressID());
        student.setFirstName(firstName.getText());
        student.setLastName(lastName.getText());
        student.setPhone1(phone_1.getText().replaceAll("[^0-9]",""));
        student.setPhone2(phone_2.getText().replaceAll("[^0-9]",""));

        sql.updateStudent(student);

      }

      if(applicationFrame != null) {
        applicationFrame.updateFrameNewStudent(student);
      }

    }
  }
  
  /**
   * Implementation of student delete button
   */
  class Delete implements ActionListener {
    public void actionPerformed(ActionEvent e) {
     if(studentID.getText().length()!=0) {
       sql.delete("student",Integer.parseInt(studentID.getText()));

       frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
     }
    }
  }

  // sets the app's host frame to the frame passed in (i.e. an instance of Database.java)
  public void setAppFrame(AppFrame appF) {
    this.applicationFrame = appF;
  }
}
