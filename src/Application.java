/**
 * @class class which handles application formatting
 */
public class Application {
  private int applicationID;
  private int studentID;
  private Student student;
  private int amountAwarded;
  private String year;
  private Float classPercentile;
  private String highSchool;
  private Float gpa;
  private int actScore;
  private int satScore;
  private String collegeAttending;
  private String fieldOfStudy;
  private long collegeID;
  private String hasRecommendationLetter;
  private int numberOfScholarships;
  private long scholarshipAmounts;
  private long efcNumber;
  private int householdSize;
  private int householdInCollege;
  private String hasReceivedAngelFundSupportPrior;
  private String pdfFileLocation;

  /**
   * Default Constructor
   */
  public Application() {}

  /**
   * @desc Constructor for an application
   * @param applicationID
   * @param studentID
   * @param student
   * @param amountAwarded
   * @param year
   * @param classPercentile
   * @param highSchool
   * @param gpa
   * @param actScore
   * @param satScore
   * @param collegeAttending
   * @param fieldOfStudy
   * @param collegeID
   * @param hasRecommendationLetter
   * @param numberOfScholarships
   * @param scholarshipAmounts
   * @param efcNumber
   * @param householdSize
   * @param householdInCollege
   * @param hasReceivedAngelFundSupportPrior
   * @param pdfFileLocation
   */
  public Application(int applicationID, int studentID, Student student, int amountAwarded, String year, Float classPercentile, String highSchool, Float gpa, int actScore, int satScore, String collegeAttending, String fieldOfStudy, long collegeID, String hasRecommendationLetter, int numberOfScholarships, long scholarshipAmounts, long efcNumber, int householdSize, int householdInCollege, String hasReceivedAngelFundSupportPrior, String pdfFileLocation) {
    this.applicationID = applicationID;
    this.studentID = studentID;
    this.student = student;
    this.amountAwarded = amountAwarded;
    this.year = year;
    this.classPercentile = classPercentile;
    this.highSchool = highSchool;
    this.gpa = gpa;
    this.actScore = actScore;
    this.satScore = satScore;
    this.collegeAttending = collegeAttending;
    this.fieldOfStudy = fieldOfStudy;
    this.collegeID = collegeID;
    this.hasRecommendationLetter = hasRecommendationLetter;
    this.numberOfScholarships = numberOfScholarships;
    this.scholarshipAmounts = scholarshipAmounts;
    this.efcNumber = efcNumber;
    this.householdSize = householdSize;
    this.householdInCollege = householdInCollege;
    this.hasReceivedAngelFundSupportPrior = hasReceivedAngelFundSupportPrior;
    this.pdfFileLocation = pdfFileLocation;
  }

  // series of getters and setters
  public int getApplicationID() {
    return applicationID;
  }

  public void setApplicationID(int applicationID) {
    this.applicationID = applicationID;
  }

  public int getStudentID() {
    return studentID;
  }

  public void setStudentID(int studentID) {
    this.studentID = studentID;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public int getAmountAwarded() {
    return amountAwarded;
  }

  public void setAmountAwarded(int amountAwarded) {
    this.amountAwarded = amountAwarded;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public Float getClassPercentile() {
    return classPercentile;
  }

  public void setClassPercentile(Float classPercentile) {
    this.classPercentile = classPercentile;
  }

  public String getHighSchool() {
    return highSchool;
  }

  public void setHighSchool(String highSchool) {
    this.highSchool = highSchool;
  }

  public Float getGpa() {
    return gpa;
  }

  public void setGpa(Float gpa) {
    this.gpa = gpa;
  }

  public int getActScore() {
    return actScore;
  }

  public void setActScore(int actScore) {
    this.actScore = actScore;
  }

  public int getSatScore() {
    return satScore;
  }

  public void setSatScore(int satScore) {
    this.satScore = satScore;
  }

  public String getCollegeAttending() {
    return collegeAttending;
  }

  public void setCollegeAttending(String collegeAttending) {
    this.collegeAttending = collegeAttending;
  }

  public String getFieldOfStudy() {
    return fieldOfStudy;
  }

  public void setFieldOfStudy(String fieldOfStudy) {
    this.fieldOfStudy = fieldOfStudy;
  }

  public long getCollegeID() {
    return collegeID;
  }

  public void setCollegeID(long collegeID) {
    this.collegeID = collegeID;
  }

  public String getHasRecommendationLetter() {
    return hasRecommendationLetter;
  }

  public void setHasRecommendationLetter(String hasRecommendationLetter) {
    this.hasRecommendationLetter = hasRecommendationLetter;
  }

  public int getNumberOfScholarships() {
    return numberOfScholarships;
  }

  public void setNumberOfScholarships(int numberOfScholarships) {
    this.numberOfScholarships = numberOfScholarships;
  }

  public long getScholarshipAmounts() {
    return scholarshipAmounts;
  }

  public void setScholarshipAmounts(long scholarshipAmounts) {
    this.scholarshipAmounts = scholarshipAmounts;
  }

  public long getEfcNumber() {
    return efcNumber;
  }

  public void setEfcNumber(long efcNumber) {
    this.efcNumber = efcNumber;
  }

  public int getHouseholdSize() {
    return householdSize;
  }

  public void setHouseholdSize(int householdSize) {
    this.householdSize = householdSize;
  }

  public int getHouseholdInCollege() {
    return householdInCollege;
  }

  public void setHouseholdInCollege(int householdInCollege) {
    this.householdInCollege = householdInCollege;
  }

  public String getHasReceivedAngelFundSupportPrior() {
    return hasReceivedAngelFundSupportPrior;
  }

  public void setHasReceivedAngelFundSupportPrior(String hasReceivedAngelFundSupportPrior) {
    this.hasReceivedAngelFundSupportPrior = hasReceivedAngelFundSupportPrior;
  }

  public String getPdfFileLocation() {
    return pdfFileLocation;
  }

  public void setPdfFileLocation(String pdfFileLocation) {
    this.pdfFileLocation = pdfFileLocation;
  }

  /**
   * A nice easy and readable string for an application
   */
  public String toString() {
    return(String.format("%-15s %-15s %-15s %-15s %-15s %-15s", applicationID, student.getFirstName(), student.getLastName(), year, collegeAttending, fieldOfStudy));
  }
}
