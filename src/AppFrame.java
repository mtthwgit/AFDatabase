import java.awt.BorderLayout;
import java.awt.Desktop;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * @desc Frame which opens to edit and create applications in the database. Triggered by "New Student" Button
 */
public class AppFrame extends JFrame {

  private static final long serialVersionUID = -924496593602878519L;
	
  private JPanel contentPane;
  private JTextField appID;
  private JTextField highSchool;
  private JTextField college;
  private JTextField major;
  private final ButtonGroup buttonGroupSupport = new ButtonGroup();
  private final ButtonGroup buttonGroupLetter = new ButtonGroup();
  private JTextField pdfName;
  private JTextField studentFirstName;
  private JTextField studentLastName;
  private AppFrame thisAppFrame;
  private JFormattedTextField efcNum;
  private JFormattedTextField gpa;
  private JSpinner houseInCollegeSpinner;
  private File pdf;
  private JSpinner houseSizeSpinner;
  private JSpinner scholarshipSpinner;
  private JFormattedTextField year;
  private SQLConnection sql;
  private JFormattedTextField classPercentile;
  private Application app;
  private JRadioButton rdbtnYesLetter;
  private JRadioButton rdbtnYesSupport;
  private JRadioButton rdbtnNoLetter;
  private JRadioButton rdbtnNoSupport;

  /**
   * @desc Create the frame which hosts all other windows.
   */
  public AppFrame() {
	  
	try {
		this.setIconImage((ImageIO.read(new File(".favicon.png"))));
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	this.setTitle("Application Editor");

    app = new Application();

    sql = new SQLConnection("angelFundDatabase.db");

    thisAppFrame = this;

    setDefaultCloseOperation(AppFrame.DISPOSE_ON_CLOSE);
    setBounds(100, 100, 650, 600);

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenuItem mntmSaveAndClose = new JMenuItem("Save And Close");
    menuBar.add(mntmSaveAndClose);
    
    mntmSaveAndClose.addActionListener(new saveAndCloseAction());

    JMenuItem mntmSave = new JMenuItem("Save");
    menuBar.add(mntmSave);
    
    mntmSave.addActionListener(new saveAppAction());

    JMenuItem mntmDelete = new JMenuItem("Delete");
    mntmDelete.addActionListener(new DeleteAction());
    menuBar.add(mntmDelete);

    JMenuItem mntmSelectStudent = new JMenuItem("Select Student");
    mntmSelectStudent.addActionListener(new selectStudentAction());
    menuBar.add(mntmSelectStudent);

    JMenuItem mntmNewStudent = new JMenuItem("New Student");
    mntmNewStudent.addActionListener(new newStudentAction());
    menuBar.add(mntmNewStudent);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);

    JPanel appPanel = new JPanel();
    contentPane.add(appPanel, BorderLayout.CENTER);
    appPanel.setLayout(new FormLayout(new ColumnSpec[] {
            FormSpecs.RELATED_GAP_COLSPEC,
            FormSpecs.GROWING_BUTTON_COLSPEC,
            FormSpecs.RELATED_GAP_COLSPEC,
            FormSpecs.GROWING_BUTTON_COLSPEC,
            FormSpecs.RELATED_GAP_COLSPEC,
            FormSpecs.DEFAULT_COLSPEC,
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
    appPanel.add(lblFirstName, "2, 2");

    JLabel lblLastName = new JLabel("Last Name");
    lblLastName.setLabelFor(lblLastName);
    appPanel.add(lblLastName, "8, 2");

    studentFirstName = new JTextField();
    lblFirstName.setLabelFor(studentFirstName);
    studentFirstName.setEditable(false);
    appPanel.add(studentFirstName, "2, 4, 3, 1, fill, default");
    studentFirstName.setColumns(10);

    studentLastName = new JTextField();
    studentLastName.setEditable(false);
    appPanel.add(studentLastName, "8, 4, 3, 1, fill, default");
    studentLastName.setColumns(10);

    JLabel lblYear = new JLabel("Year");
    appPanel.add(lblYear, "2, 6, 3, 1");

    JLabel lblAppID = new JLabel("ApplicationID");
    appPanel.add(lblAppID, "8, 6, 3, 1");

    year = new JFormattedTextField(new NumberFormatter(NumberFormat.getInstance()));
    lblYear.setLabelFor(year);
    appPanel.add(year, "2, 8, 3, 1, fill, default");

    appID = new JTextField();
    lblAppID.setLabelFor(appID);
    appID.setEditable(false);
    appPanel.add(appID, "8, 8, 3, 1, fill, default");
    appID.setColumns(10);

    JLabel lblHighSchool = new JLabel("High School");
    lblHighSchool.setLabelFor(lblHighSchool);
    appPanel.add(lblHighSchool, "2, 10, 9, 1");

    highSchool = new JTextField();
    appPanel.add(highSchool, "2, 12, 9, 1, fill, default");
    highSchool.setColumns(10);

    JLabel lblGPA = new JLabel("GPA");
    appPanel.add(lblGPA, "2, 14, 3, 1");

    JLabel lblClassPercentile = new JLabel("Class Precentile");
    appPanel.add(lblClassPercentile, "8, 14, 3, 1");

    gpa = new JFormattedTextField(new NumberFormatter(NumberFormat.getInstance()));
    lblGPA.setLabelFor(gpa);
    appPanel.add(gpa, "2, 16, 3, 1, fill, default");

    classPercentile = new JFormattedTextField(new NumberFormatter(NumberFormat.getInstance()));
    lblClassPercentile.setLabelFor(classPercentile);
    appPanel.add(classPercentile, "8, 16, 3, 1, fill, default");

    JLabel lblCollege = new JLabel("College Attending");
    appPanel.add(lblCollege, "2, 18, 3, 1");

    JLabel lblMajor = new JLabel("Field of Study");
    appPanel.add(lblMajor, "8, 18, 3, 1");

    college = new JTextField();
    lblCollege.setLabelFor(college);
    appPanel.add(college, "2, 20, 3, 1, fill, default");
    college.setColumns(10);

    major = new JTextField();
    lblMajor.setLabelFor(major);
    appPanel.add(major, "8, 20, 3, 1, fill, default");
    major.setColumns(10);

    JLabel lblScholarships = new JLabel("Number of Scholarships");
    appPanel.add(lblScholarships, "2, 22, 3, 1");

    JLabel lblEfcNum = new JLabel("EFC Number");
    appPanel.add(lblEfcNum, "8, 22, 3, 1");

    scholarshipSpinner = new JSpinner();
    lblScholarships.setLabelFor(scholarshipSpinner);
    appPanel.add(scholarshipSpinner, "2, 24, 3, 1");

    efcNum = new JFormattedTextField(new NumberFormatter(NumberFormat.getInstance()));
    lblEfcNum.setLabelFor(efcNum);
    appPanel.add(efcNum, "8, 24, 3, 1, fill, default");

    JLabel lblHouseSize = new JLabel("Household Size");
    appPanel.add(lblHouseSize, "2, 26, 3, 1");

    JLabel lblHouseInCollege = new JLabel("Number of Household in College");
    appPanel.add(lblHouseInCollege, "8, 26, 3, 1");

    houseSizeSpinner = new JSpinner();
    lblHouseSize.setLabelFor(houseSizeSpinner);
    appPanel.add(houseSizeSpinner, "2, 28, 3, 1");

    houseInCollegeSpinner = new JSpinner();
    lblHouseInCollege.setLabelFor(houseInCollegeSpinner);
    appPanel.add(houseInCollegeSpinner, "8, 28, 3, 1");

    JLabel lblReccoLetter = new JLabel("Has Recommendation Letter");
    appPanel.add(lblReccoLetter, "2, 34, 3, 1");

    JLabel lblRecievedSupportPrior = new JLabel("Recieved Angel Fund Support Prior");
    appPanel.add(lblRecievedSupportPrior, "8, 34, 3, 1");

    rdbtnYesLetter = new JRadioButton("Yes");
    buttonGroupLetter.add(rdbtnYesLetter);
    appPanel.add(rdbtnYesLetter, "2, 36");


    rdbtnNoLetter = new JRadioButton("No");
    rdbtnNoLetter.setSelected(true);
    buttonGroupLetter.add(rdbtnNoLetter);
    appPanel.add(rdbtnNoLetter, "4, 36");


    rdbtnYesSupport = new JRadioButton("Yes");
    buttonGroupSupport.add(rdbtnYesSupport);
    appPanel.add(rdbtnYesSupport, "8, 36");

    rdbtnNoSupport = new JRadioButton("No");
    rdbtnNoSupport.setSelected(true);
    buttonGroupSupport.add(rdbtnNoSupport);
    appPanel.add(rdbtnNoSupport, "10, 36");


    JLabel lblAppPDF = new JLabel("Application PDF");
    appPanel.add(lblAppPDF, "2, 38, 9, 1");

    pdfName = new JTextField();
    lblAppPDF.setLabelFor(pdfName);
    pdfName.setEditable(false);
    pdfName.setToolTipText("PDF Path");
    appPanel.add(pdfName, "2, 40, 3, 1, fill, default");
    pdfName.setColumns(10);

    JButton btnBrowse = new JButton("Browse");
    appPanel.add(btnBrowse, "8, 40");

    btnBrowse.addActionListener(new BrowseAction());

    JButton btnOpenPDF = new JButton("Open PDF");
    appPanel.add(btnOpenPDF, "10, 40");

    btnOpenPDF.addActionListener(new OpenPDFAction());
  }

  /**
   * Overloaded Constructor which makes the frame with specific application data.
   * @param app
   */
  public AppFrame(Application app) {
    this();

    this.app = app;

    Map<String, Object> query = new HashMap<String, Object>();
    query.put("studentID",app.getStudentID());
    Map<Integer, Student> stuList = sql.selectStudent(query);
    Student stu = stuList.get(app.getStudentID());

    appID.setText(app.getApplicationID()+"");
    studentFirstName.setText(stu.getFirstName());
    studentLastName.setText(stu.getLastName());
    year.setText(app.getYear());
    highSchool.setText(app.getHighSchool());
    gpa.setText(app.getGpa()+"");
    classPercentile.setText(app.getClassPercentile()+"");
    college.setText(app.getCollegeAttending());
    major.setText(app.getFieldOfStudy());
    scholarshipSpinner.setValue(app.getNumberOfScholarships());
    efcNum.setValue(app.getEfcNumber());
    houseSizeSpinner.setValue(app.getHouseholdSize());
    houseInCollegeSpinner.setValue(app.getHouseholdInCollege());
    //recommendation letter and prior support
    if(app.getPdfFileLocation()!=null) {
      pdfName.setText(app.getPdfFileLocation().toString());
      pdf = new File(app.getPdfFileLocation().toString());
    }
  }

  /**
   * @desc ability to browse file system
   */
  class BrowseAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser chooser = new JFileChooser();

      chooser.setFileFilter(new FileNameExtensionFilter("pdf files","*.pdf","pdf"));

      int returnValue = chooser.showOpenDialog(null);

      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = chooser.getSelectedFile();
        pdfName.setText(selectedFile.getName());
        pdf = selectedFile;

      }
    }

  }

  /**
   * @desc ability to open PDF files
   */
  class OpenPDFAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(pdf != null && pdf.exists()) {
        if (Desktop.isDesktopSupported()) {
          try {
            Desktop.getDesktop().open(pdf);
          } catch (IOException ex) {
            // no application registered for PDFs
        	// not yet implemented
          }
        }

      }
    }

  }

  /**
   * @desc ability to make new student from application editor
   */
  class newStudentAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      StudentFrame studentFrame = new StudentFrame();
      studentFrame.setVisible(true);
      studentFrame.setAppFrame(thisAppFrame);
    }
  }
  
  /**
   * @desc ability to delete an item from the database from any editor
   */
  class DeleteAction implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  if (!appID.getText().isEmpty()) {
	      	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Application", "Delete Application", JOptionPane.YES_NO_OPTION);
	    	  if (reply == JOptionPane.YES_OPTION) {
	    		  sql.delete("application", app.getApplicationID());
	    		  thisAppFrame.dispatchEvent(new WindowEvent(thisAppFrame, WindowEvent.WINDOW_CLOSING));
	    	  }
		  }
	  }
	  
  }

  /**
   * @desc ability to save and close
   */
  class saveAndCloseAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(studentFirstName.getText().isEmpty() || studentLastName.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "An Application requires a Student!");
        return;
    }
      saveAppAction temp = new saveAppAction();
      temp.actionPerformed(null);
      thisAppFrame.dispatchEvent(new WindowEvent(thisAppFrame, WindowEvent.WINDOW_CLOSING));
    }
  }

  /**
   * @desc ability to select a student for an application in the editor
   */
  class selectStudentAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      new StudentSelectFrame(thisAppFrame).setVisible(true);
    }
  }

  class saveAppAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    	
      if(studentFirstName.getText().isEmpty() || studentLastName.getText().isEmpty()) {
    	  JOptionPane.showMessageDialog(null, "An Application requires a Student!");
    	  return;
      }
      //if there is no app ID then it is a new application. There needs to be a student
      //associated.
      if(appID.getText().length() == 0 && studentFirstName.getText().length() > 0) {

        String temp = efcNum.getText().replace(" ","").replaceAll("[^0-9.]","");
        if(temp.length() > 0) {
          app.setEfcNumber(Long.parseLong(temp));
        }
        app.setFieldOfStudy(major.getText());
        temp = gpa.getText().replace(" ","").replaceAll("[^0-9.]","");
        if(temp.length() > 0) {
          app.setGpa(Float.parseFloat(temp));
        } else {
          app.setGpa(0.0f);
        }
        if(rdbtnYesLetter.isSelected()) {
          app.setHasRecommendationLetter("Yes");
        } else {
          app.setHasRecommendationLetter("No");
        }
        if(rdbtnYesSupport.isSelected()) {
          app.setHasReceivedAngelFundSupportPrior("Yes");
        } else {
          app.setHasReceivedAngelFundSupportPrior("No");
        }
        temp = classPercentile.getText().replace(" ","").replaceAll("[^0-9.]","");
        if(temp.length() > 0) {
          app.setClassPercentile(Float.parseFloat(temp));
        } else {
          app.setClassPercentile(0.0f);
        }
        app.setHighSchool(highSchool.getText());
        app.setHouseholdInCollege((Integer) houseInCollegeSpinner.getValue());
        app.setCollegeAttending(college.getText());
        app.setHouseholdSize((Integer) houseSizeSpinner.getValue());
        app.setNumberOfScholarships((Integer) scholarshipSpinner.getValue());
        app.setYear(year.getText().replaceAll("[^0-9.]",""));

        int id = sql.createApplication(app);

        appID.setText(id+"");
        app.setApplicationID(id);

        if(pdf != null && pdf.exists()) {
          Path source = pdf.toPath();
          File f = new File("applicationPDFs" + File.separator + studentFirstName.getText() + "_" + studentLastName.getText() + "_" + appID.getText() + ".pdf");
          try {
            f.mkdirs();
            f.createNewFile();
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          Path target = Paths.get(f.getPath());

          try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            app.setPdfFileLocation(target.toString());
            pdf = target.toFile();
            pdfName.setText(pdf.getName());
            sql.updateApplication(app);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }

      } else {

        String temp = efcNum.getText().replace(" ","").replaceAll("[^0-9.]","");
        if(temp.length() > 0) {
          app.setEfcNumber(Long.parseLong(temp));
        }
        app.setFieldOfStudy(major.getText());
        temp = gpa.getText().replace(" ","").replaceAll("[^0-9.]","");
        if(temp.length() > 0) {
          app.setGpa(Float.parseFloat(temp));
        }
        temp = classPercentile.getText().replace(" ","").replaceAll("[^0-9.]","");
        if(temp.length() > 0) {
          app.setClassPercentile(Float.parseFloat(temp));
        } else {
          app.setClassPercentile(0.0f);
        }
        if(rdbtnYesLetter.isSelected()) {
          app.setHasRecommendationLetter("Yes");
        } else {
          app.setHasRecommendationLetter("No");
        }
        if(rdbtnYesSupport.isSelected()) {
          app.setHasReceivedAngelFundSupportPrior("Yes");
        } else {
          app.setHasReceivedAngelFundSupportPrior("No");
        }
        app.setHighSchool(highSchool.getText());
        app.setHouseholdInCollege((Integer) houseInCollegeSpinner.getValue());
        app.setHouseholdSize((Integer) houseSizeSpinner.getValue());
        app.setCollegeAttending(college.getText());
        app.setNumberOfScholarships((Integer) scholarshipSpinner.getValue());
        app.setYear(year.getText().replaceAll("[^0-9.]",""));
        app.setPdfFileLocation("");
        sql.updateApplication(app);

        if(pdf != null && pdf.exists()) {
          Path source = pdf.toPath();
          File f = new File("applicationPDFs" + File.separator + studentFirstName.getText() + "_" + studentLastName.getText() + "_" + appID.getText() + ".pdf");
          try {
            f.mkdirs();
            f.createNewFile();
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          Path target = Paths.get(f.getAbsolutePath());

          try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            app.setPdfFileLocation(target.toString());
            pdf = target.toFile();
            pdfName.setText(pdf.getName());
            sql.updateApplication(app);
          } catch (IOException e1) {
            e1.printStackTrace();
          }

        }

      }
    }
  }

  /**
   * Updates the datafields in the info viewer with information from a newly created student.
   * @param student
   */
  public void updateFrameNewStudent(Student student) {
    studentFirstName.setText(student.getFirstName());
    studentLastName.setText(student.getLastName());
    app.setStudentID(student.getStudentID());
  }

}