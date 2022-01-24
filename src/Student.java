import java.util.HashMap;
import java.util.Map;

/**
 * @desc Class to conveniently store student.
 */
public class Student {
  private int studentID;
  private int addressID;
  private String firstName;
  private String lastName;
  private String phone1;
  private String phone2;
  public Address address;
  public Map<Integer, Parent> parent;

  /**
   * Default Constructor
   */
  public Student() {}

  /**
   * Overload Constructor
   * @param sid student id
   * @param aid application id
   * @param fn first name
   * @param ln last name
   * @param p1 phone number 1
   * @param p2 phone number 2
   * @param ad address
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Student(int sid, int aid, String fn, String ln, String p1, String p2, Address ad) {
    studentID = sid;
    addressID = aid;
    firstName = fn;
    lastName = ln;
    phone1 = p1;
    phone2 = p2;
    address = ad;
    parent = new HashMap();
  }

  // getters and setters
  public void setStudentID(int i) {studentID = i;}

  public void setAddressID(int i) {addressID = i;}

  public void setFirstName(String s) {firstName = s;}

  public void setLastName(String s) {lastName = s;}

  public void setPhone1(String s) {phone1 = s;}

  public void setPhone2(String s) {phone2 = s;}

  public int getStudentID() {return(studentID);}

  public int getAddressID() {return(addressID);}

  public String getFirstName() {return(firstName);}

  public String getLastName() {return(lastName);}

  public String getPhone1() {return(phone1);}

  public String getPhone2() {return(phone2);}

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
    this.addressID = address.getAddressID();
  }

  public Map<Integer,Parent> getParent() {
    return parent;
  }

  public void setParent(int ID, Parent parent) {
    this.parent.put(ID,parent);
  }

  /**
   * @return easy to read string representing a student
   */
  public String toString() {
   return(String.format("%-15s %-15s %-15s %-15s %-15s", studentID, firstName, lastName, phone1, phone2));
  }
}