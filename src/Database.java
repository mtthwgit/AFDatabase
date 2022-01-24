import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc Main Class which initializes the database view and manages the tabs in
 *       the pane. Contact j.grosse01@gmail.com, jadenbathon@gmail.com, or
 *       mtthwjb@gmail.com if you have questions or concerns about this code.
 */
public class Database {

  private JFrame frame;

  private final SQLConnection sql;
  private JTextField firstNameStudent;
  private JTextField lastNameStudent;
  private JTextField majorStudent;
  private JTextField firstNameApp;
  private JTextField lastNameApp;
  private JTextField majorApp;
  private JTextField college;
  @SuppressWarnings("rawtypes")
  private JList studentResultsList;
  @SuppressWarnings("rawtypes")
  private JList appResultsList;
  @SuppressWarnings("rawtypes")
  private DefaultListModel studentListModel = new DefaultListModel();
  @SuppressWarnings("rawtypes")
  private DefaultListModel appListModel = new DefaultListModel();
  private JFormattedTextField phoneStudent;
  private JFormattedTextField studentID;
  private JFormattedTextField yearApp;
  private JPanel appTab;
  private JPanel studentTab;
  private JTabbedPane tabbedPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Database window = new Database();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public Database() {
    sql = new SQLConnection("angelFundDatabase.db");
    initialize();
    
    try {
		frame.setIconImage((ImageIO.read(new File(".favicon.png"))));
    } catch (IOException e) {
		e.printStackTrace();
    }
    
    frame.setTitle("Angel Fund Scholarship Database v6.3");
  }

  /**
   * Initialize the contents of the frame.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 1280, 720);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);

    JMenuItem mntmExport = new JMenuItem("Export as CSV");
    mntmExport.addActionListener(new ExportAction());
    menuBar.add(mntmExport);

    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

    appTab = new JPanel();
    tabbedPane.addTab("Application", null, appTab, null);
    appTab.setLayout(new FormLayout(new ColumnSpec[] {
            FormSpecs.GROWING_BUTTON_COLSPEC,
            FormSpecs.RELATED_GAP_COLSPEC,
            FormSpecs.GROWING_BUTTON_COLSPEC,
            FormSpecs.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("171px:grow"),
            FormSpecs.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("171px:grow"),
            FormSpecs.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("250px"),},
            new RowSpec[] {
                    RowSpec.decode("14px"),
                    FormSpecs.RELATED_GAP_ROWSPEC,
                    RowSpec.decode("23px"),
                    FormSpecs.RELATED_GAP_ROWSPEC,
                    RowSpec.decode("14px"),
                    FormSpecs.RELATED_GAP_ROWSPEC,
                    RowSpec.decode("23px"),
                    FormSpecs.RELATED_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC,
                    FormSpecs.RELATED_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC,
                    FormSpecs.RELATED_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC,
                    RowSpec.decode("fill:default:grow"),}));

    JLabel lblFirstNameApp = new JLabel("First Name");
    appTab.add(lblFirstNameApp, "1, 1, 3, 1, fill, top");

    JLabel lblLastNameApp = new JLabel("Last Name");
    appTab.add(lblLastNameApp, "5, 1, 3, 1, fill, top");
    lblFirstNameApp.setLabelFor(firstNameApp);

    firstNameApp = new JTextField();
    firstNameApp.setColumns(10);
    appTab.add(firstNameApp, "1, 3, 3, 1, fill, center");

    lastNameApp = new JTextField();
    lblLastNameApp.setLabelFor(lastNameApp);
    lastNameApp.setColumns(10);
    appTab.add(lastNameApp, "5, 3, 3, 1, fill, center");

    JButton btnNewApp = new JButton("New Application");
    appTab.add(btnNewApp, "9, 3, fill, top");

    btnNewApp.addActionListener(new NewAppAction());

    JLabel lblYear = new JLabel("Year");
    appTab.add(lblYear, "1, 5, 3, 1, fill, top");

    JLabel lblApplicationId = new JLabel("Application ID");
    appTab.add(lblApplicationId, "5, 5, 3, 1, fill, top");

    yearApp = new JFormattedTextField();
    lblYear.setLabelFor(yearApp);
    appTab.add(yearApp, "1, 7, 3, 1, fill, center");

    JFormattedTextField appID = new JFormattedTextField();
    lblApplicationId.setLabelFor(appID);
    appTab.add(appID, "5, 7, 3, 1, fill, center");

    JLabel lblMajorApp = new JLabel("Field of Study");
    appTab.add(lblMajorApp, "1, 9, 3, 1, fill, top");

    JLabel lblNewLabel = new JLabel("College");
    appTab.add(lblNewLabel, "5, 9, 3, 1");

    majorApp = new JTextField();
    majorApp.setColumns(10);
    appTab.add(majorApp, "1, 11, 3, 1, fill, center");
    lblMajorApp.setLabelFor(majorApp);

    college = new JTextField();
    lblNewLabel.setLabelFor(college);
    appTab.add(college, "5, 11, 3, 1, fill, default");
    college.setColumns(10);

    JButton btnSearchApp = new JButton("Search");
    appTab.add(btnSearchApp, "9, 11, fill, top");

    btnSearchApp.addActionListener(new SearchAppAction());

    JToolBar appToolBar = new JToolBar();
    appTab.add(appToolBar, "1, 13, 9, 1");

    JButton btnOpenApp = new JButton("Open Application");
    appToolBar.add(btnOpenApp);

    btnOpenApp.addActionListener(new OpenAppAction());

    JButton btnDeleteApp = new JButton("Delete Application");
    appToolBar.add(btnDeleteApp);

    btnDeleteApp.addActionListener(new DeleteAppAction());

    appResultsList = new JList(appListModel);
    appTab.add(new JScrollPane(appResultsList), "1, 14, 9, 1, fill, fill");

    Font currentFont1 = appResultsList.getFont();
    appResultsList.setFont(new Font(Font.MONOSPACED,currentFont1.getStyle(),currentFont1.getSize()));

    appListModel.addElement(String
            .format("%-15s %-15s %-15s %-15s %-15s %-15s", "App ID", "First Name", "Last Name", "Year", "College", "Major"));

    studentTab = new JPanel();
    tabbedPane.addTab("Student", null, studentTab, null);
    studentTab.setLayout(new FormLayout(new ColumnSpec[] {
            FormSpecs.GROWING_BUTTON_COLSPEC,
            FormSpecs.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("max(50dlu;pref):grow(2)"),
            FormSpecs.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("max(50dlu;pref):grow(2)"),
            FormSpecs.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("max(50dlu;pref):grow(2)"),
            FormSpecs.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("max(50dlu;pref):grow(2)"),
            FormSpecs.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("max(50dlu;pref):grow(2)"),
            FormSpecs.RELATED_GAP_COLSPEC,
            FormSpecs.GROWING_BUTTON_COLSPEC,
            FormSpecs.RELATED_GAP_COLSPEC,
            FormSpecs.GROWING_BUTTON_COLSPEC,},
            new RowSpec[] {
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
                    RowSpec.decode("default:grow"),}));

    JLabel lblFirstNameStudent = new JLabel("First Name");
    studentTab.add(lblFirstNameStudent, "1, 2, 5, 1");

    JLabel lblLastNameStudent = new JLabel("Last Name");
    studentTab.add(lblLastNameStudent, "7, 2, 5, 1");

    firstNameStudent = new JTextField();
    lblFirstNameStudent.setLabelFor(firstNameStudent);
    studentTab.add(firstNameStudent, "1, 4, 5, 1, fill, default");
    firstNameStudent.setColumns(10);

    lastNameStudent = new JTextField();
    lblLastNameStudent.setLabelFor(lastNameStudent);
    studentTab.add(lastNameStudent, "7, 4, 5, 1, fill, default");
    lastNameStudent.setColumns(10);

    JButton btnNewStudent = new JButton("New Student");
    studentTab.add(btnNewStudent, "13, 4, 3, 1");

    btnNewStudent.addActionListener(new NewStudentAction());

    JLabel lblPhoneStudent = new JLabel("Phone");
    studentTab.add(lblPhoneStudent, "1, 6, 3, 1");

    JLabel lblStudentID = new JLabel("Student ID");
    studentTab.add(lblStudentID, "5, 6, 2, 1");

    JLabel lblMajorStudent = new JLabel("Field of Study");
    studentTab.add(lblMajorStudent, "9, 6, 3, 1");

    try {
      phoneStudent = new JFormattedTextField(new MaskFormatter("(###) ###-####"));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    lblPhoneStudent.setLabelFor(phoneStudent);
    studentTab.add(phoneStudent, "1, 8, 3, 1, fill, default");

    studentID = new JFormattedTextField(new NumberFormatter(NumberFormat.getInstance()));
    lblStudentID.setLabelFor(studentID);
    studentTab.add(studentID, "5, 8, 3, 1, fill, default");

    majorStudent =new JTextField();
    lblMajorStudent.setLabelFor(majorStudent);
    studentTab.add(majorStudent, "9, 8, 3, 1, fill, default");
    majorStudent.setColumns(10);

    JButton btnSearchStudent = new JButton("Search");
    studentTab.add(btnSearchStudent, "13, 8, 3, 1");

    btnSearchStudent.addActionListener(new SearchStudentAction());

    JToolBar studentToolBar = new JToolBar();
    studentTab.add(studentToolBar, "1, 10, 15, 1");

    JButton btnOpenStudent = new JButton("Open Student Info");
    studentToolBar.add(btnOpenStudent);

    btnOpenStudent.addActionListener(new OpenStudentAction());

    JButton btnDeleteStudent = new JButton("Delete Student");
    studentToolBar.add(btnDeleteStudent);

    btnDeleteStudent.addActionListener(new DeleteStudentAction());

    studentResultsList = new JList<>(studentListModel);
    studentTab.add(new JScrollPane(studentResultsList), "1, 12, 15, 1, fill, fill");

    Font currentFont2 = studentResultsList.getFont();
    studentResultsList.setFont(new Font(Font.MONOSPACED,currentFont2.getStyle(),currentFont2.getSize()));

    studentListModel.addElement(String
            .format("%-15s %-15s %-15s %-15s %-15s", "Student ID", "First Name", "Last Name", "Phone 1", "Phone 2"));
  }

  /**
   * @desc Allows the searching of students
   */
  class SearchStudentAction implements ActionListener {

    @SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
      Map<String, Object> query = new HashMap<>();

      Map<Integer,Student> studentList;

      studentListModel.removeAllElements();

      studentListModel.addElement(String
              .format("%-15s %-15s %-15s %-15s %-15s", "Student ID", "First Name", "Last Name", "Phone 1", "Phone 2"));

      String firstName = firstNameStudent.getText();
      if (firstName.length() > 0) {
        query.put("firstName", firstName);
      }
      String lastName = lastNameStudent.getText();
      if (lastName.length() > 0) {
        query.put("lastName", lastName);
      }
      String major = majorStudent.getText();
      if (major.length() > 0) {
        query.put("fieldOfStudy", major);
      }

      String phone = phoneStudent.getText();
      String phoneContent = phone.replaceAll("[^0-9]","");
      if( phoneContent.length() > 0) {
        query.put("phone1",phone);
        query.put("phone2",phone);
      }

      String id = studentID.getText();
      if(id.length() > 0) {
        query.put("studentID",id);
      }

      studentList = sql.selectStudent(query);

      for (Student s : studentList.values()) {
        studentListModel.addElement(s);
      }
    }
  }

  /**
   * @desc allows searching by application
   */
  class SearchAppAction implements ActionListener {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void actionPerformed(ActionEvent e) {
      Map<String, Object> query = new HashMap();

      Map<Integer, Application> appList;

      appListModel.removeAllElements();

      appListModel.addElement(String
              .format("%-15s %-15s %-15s %-15s %-15s %-15s", "App ID", "First Name", "Last Name", "Year", "College", "Major"));

      String firstName = firstNameApp.getText();
      if(firstName.length() > 0) {
        query.put("firstName",firstName);
      }

      String lastName = lastNameApp.getText();
      if(lastName.length() > 0) {
        query.put("lastName",lastName);
      }

      String year = yearApp.getText();
      if(year.length() > 0) {
        query.put("year",year);
      }

      String coll = college.getText();
      if(coll.length() > 0 ) {
        query.put("collegeAttending",coll);
      }

      String fos = majorApp.getText();
      if(fos.length() > 0) {
        query.put("fieldOfStudy",fos);
      }

      appList = sql.selectApplication(query);

      for(Application a : appList.values()) {
        appListModel.addElement(a);
      }
    }

  }

  /**
   * Allows the creation of a new student in the database view.
   */
  class NewStudentAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      new StudentFrame().setVisible(true);
    }
  }

  /**
   * Allows the student editor to be opened from database view
   */
  class OpenStudentAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if(studentResultsList.getSelectedIndex() > 0) {
        Student student = (Student) studentResultsList.getSelectedValue();
        new StudentFrame(student).setVisible(true);
      }
    }
  }

  /**
   * Allows a student to be deleted
   */
  class DeleteStudentAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if(studentResultsList.getSelectedIndex() > 0) {
    		Student student = (Student) studentResultsList.getSelectedValue();
      	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + student.getFirstName() + " " + student.getLastName(), "Delete Student", JOptionPane.YES_NO_OPTION);
      	if (reply == JOptionPane.YES_OPTION) {
      		sql.delete("student",student.getStudentID());
      		studentListModel.remove(studentResultsList.getSelectedIndex());
      	}
      }
    }
  }


  /**
   * Allows a new application to be made.
   */
  class NewAppAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      new AppFrame().setVisible(true);
    }
  }

  /**
   * Allows the application editor to be opened.
   */
  class OpenAppAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if(appResultsList.getSelectedIndex() > 0) {
        Application app = (Application) appResultsList.getSelectedValue();
        new AppFrame(app).setVisible(true);
      }
    }
  }

  /**
   * Allows an application to be edited.
   */
  class DeleteAppAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if(appResultsList.getSelectedIndex() > 0) {
      	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Application", "Delete Application", JOptionPane.YES_NO_OPTION);
      	if (reply == JOptionPane.YES_OPTION) {
            Application app = (Application) appResultsList.getSelectedValue();
            sql.delete("application",app.getApplicationID());
            appListModel.remove(appResultsList.getSelectedIndex());
      	}
      }
    }
  }

  class ExportAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(tabbedPane.getSelectedIndex() == 0) {
        try {
          JFileChooser jfc = new JFileChooser();
          jfc.setDialogTitle("Select CSV save location");
          jfc.setFileFilter(new FileNameExtensionFilter("csv files","*.csv","csv"));
          int userSelect = jfc.showSaveDialog(frame);
          if (userSelect == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            FileWriter fw = new FileWriter(file.getAbsolutePath()+".csv");
            fw.write("Application ID,Student ID,First Name,Last Name,Year,College,Major,Household Size,Household In College,"+
                    "Has Received AF Support,Has Recommendation Letter,Number of Scholarships,Class Percentile,"+
                    "GPA,High School,EFC Number");
            for (int i = 1; i < appListModel.size(); i++) {
              Application app = (Application) appListModel.get(i);
              Map<String,Object> query = new HashMap();
              query.put("studentID",app.getStudentID());
              Map<Integer, Student> rs = sql.selectStudent(query);
              Student stu = new Student();
              for(Student s : rs.values()) {stu = s;}
              fw.write("\n"+app.getApplicationID()+","+app.getStudentID()+","+stu.getFirstName()+","+stu.getLastName()+","+app.getYear()+","+app.getCollegeAttending()+","+
                      app.getFieldOfStudy()+","+app.getHouseholdSize()+","+app.getHouseholdInCollege()+","+
                      app.getHasReceivedAngelFundSupportPrior()+","+app.getHasRecommendationLetter()+","+
                      app.getNumberOfScholarships()+","+app.getClassPercentile()+","+
                      app.getGpa()+","+app.getHighSchool()+","+app.getEfcNumber());
            }
            fw.close();

          } else {
            System.out.println("File already exists.");
          }
        } catch (IOException e0) {
          System.out.println("An error occurred.");
          e0.printStackTrace();
        }
      } else {
        try {
          JFileChooser jfc = new JFileChooser();
          jfc.setDialogTitle("Select CSV save location");
          jfc.setFileFilter(new FileNameExtensionFilter("csv files","*.csv","csv"));
          int userSelect = jfc.showSaveDialog(frame);
          if (userSelect == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            FileWriter fw = new FileWriter(file.getAbsolutePath()+".csv");
            fw.write("Student ID,First Name,Last Name,Phone 1,Phone 2");
            for (int i = 1; i < studentListModel.size(); i++) {
              Student stu = (Student) studentListModel.get(i);
              fw.write("\n"+stu.getStudentID()+","+stu.getFirstName()+","+stu.getLastName()+","+
                      stu.getPhone1()+","+stu.getPhone2());
            }
            fw.close();

          } else {
            System.out.println("File already exists.");
          }
        } catch (IOException e1) {
          System.out.println("An error occurred.");
          e1.printStackTrace();
        }
      }
    }
  }
}