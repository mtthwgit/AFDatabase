/**
 * @desc easy way to store and reference a parent object
 */
public class Parent {
  private int parentID;
  private int addressID;
  private String firstName;
  private String lastName;
  private String occupation;
  private String phone;
  public Address address;

  /**
   * Default constructor
   */
  public Parent() {}

  public Parent(int parentID, int addressID, String firstName, String lastName, String occupation, String phone) {
    this.parentID = parentID;
    this.addressID = addressID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.occupation = occupation;
    this.phone = phone;
  }

  //series of getters and setters
  public int getParentID() {
    return parentID;
  }

  public void setParentID(int parentID) {
    this.parentID = parentID;
  }

  public int getAddressID() {
    return addressID;
  }

  public void setAddressID(int addressID) {
    this.addressID = addressID;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
    this.addressID = address.getAddressID();
  }

  /**
   * @return an easy to read string
   */
  public String toString() {
    return(String.format("%-15s %-15s %-15s %-15s %-15s", parentID, firstName, lastName, phone, occupation));
  }
}
