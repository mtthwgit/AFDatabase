/**
 * @desc Just stores addresses nicely
 */
public class Address {
  private int addressID;
  private String streetNameNumber;
  private String city;
  private String state;
  private int zip;

  /**
   * Default constructor
   */
  public Address() {
	  
  }
  
  /**
   * @desc Address Constructor
   * @param addressID backend database things
   * @param streetNameNumber street number and name
   * @param city city of residence
   * @param state state of residence
   * @param zip zip code of residence
   */
  public Address(int addressID, String streetNameNumber, String city, String state, int zip) {
    this.addressID = addressID;
    this.streetNameNumber = streetNameNumber;
    this.city = city;
    this.state = state;
    this.zip = zip;
  }

  /**
   * @desc addressID getter
   * @return ID of address
   */
  public int getAddressID() {
    return addressID;
  }

  /**
   * @desc streeNameNumber getter
   * @return street name and number of address
   */
  public String getStreetNameNumber() {
    return streetNameNumber;
  }

  /**
   * @desc city getter
   * @return city of address
   */
  public String getCity() {
    return city;
  }

  /**
   * @desc state getter
   * @return state of address
   */
  public String getState() {
    return state;
  }

  /**
   * @desc zip getter
   * @return zip code of address
   */
  public int getZip() {
    return zip;
  }

  /**
   * @desc addressID setter
   */
  public void setAddressID(int addressID) {
    this.addressID = addressID;
  }

  /**
   * @desc streeNameNumber setter
   */
  public void setStreetNameNumber(String streetNameNumber) {
    this.streetNameNumber = streetNameNumber;
  }

  /**
   * @desc city setter
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @desc state setter
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * @desc zip setter
   */
  public void setZip(int zip) {
    this.zip = zip;
  }
  
  /**
   * @desc returns a readable address string
   */
  public String toString() {
    String str = streetNameNumber + "\n" + city + " " + state + " " + zip;
    return str;
  }
}
