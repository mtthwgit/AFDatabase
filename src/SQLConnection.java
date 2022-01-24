import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc class which issues and handles SQL commands to interact with the db file storing the information.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class SQLConnection {
    private final String url;

    /**
     * @desc a connection to a sqlite database with the passed name.
     *
     * @param databaseName form "database.db". designed specifically for this application,
     *                     so it will look in the Database folder. Edit the file object if
     *                     you wish to change this.
     */
    public SQLConnection(String databaseName) {
        File file = new File(databaseName);
        url = "jdbc:sqlite:" + file.getPath();
    }

    /**
     * @desc used to make a connection to the passed database on initialization. Will return a
     * Connection object to use the sql connection.
     *
     * @return
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Connection Error");
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public Map<Integer, Student> selectStudent(Map<String, Object> values) {
        /*
        Initial sql statement. String manipulation will happen to get the exact
        statement we want. Joins all the other tables because we can search for students
        based upon factors like "fieldOfStudy" which are not in student table.
         */
        String sql = "SELECT * FROM student" +
                " LEFT JOIN address ON student.addressID = address.addressID" +
                " LEFT JOIN application ON student.studentID = application.studentID";
        ResultSet rs = null;
        //this is what we will return
        Map<Integer,Student> studentList = new HashMap();

        /*
        Check to see if the values map is empty or not. If it is
        we will simply query all students. Otherwise, we have to do
        some string manipulation.
         */
        if (values.size() != 0) {
            //adding "WHERE *value* LIKE ?" to the sql string for each
            //value for filtering.
            int index = 0;
            sql += " WHERE";
            for (String s : values.keySet()) {
                switch(s) {
                    case "fieldOfStudy":
                    case "year":
                        sql += " application." + s + " LIKE ?";
                        index++;
                        break;
                    case "studentID":
                        sql += " student." + s + " = ?";
                        index++;
                        break;
                    default:
                        sql += " student." + s + " LIKE ?";
                        index++;
                        break;
                }
                //add an "and" if we aren't at the end of the values.
                if (index < values.keySet().size()) {
                    sql += " and";
                }
            }
        }

        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            //set the variables we added earlier (LIKE ?).
            if(values.size() != 0) {
                int index = 1;
                for (Object v : values.values()) {
                    switch (v.getClass().toString()) {
                        case "class java.lang.Integer":
                            stmt.setInt(index++, (int) v);
                            break;
                        case "class java.lang.String":
                            String s = "%" + v + "%";
                            stmt.setString(index++, s);
                            break;
                    }
                }
            }
            sql += " ORDER BY DESC application.year";
            //execute the query
            rs = stmt.executeQuery();

            //while rs has values
            while (rs.next()) {
                //check to see if the student has already been added.
                //we will need this because the same student will get repeated
                //because of the joins.
                if(!studentList.containsKey(rs.getInt("studentID"))) {
                    //add the student.
                    studentList.put(rs.getInt("studentID"), new Student(
                            rs.getInt("studentID"),
                            rs.getInt("addressID"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("phone1"),
                            rs.getString("phone2"),
                            new Address(
                                    rs.getInt("addressID"),
                                    rs.getString("streetNameNumber"),
                                    rs.getString("city"),
                                    rs.getString("state"),
                                    rs.getInt("zip")
                            )
                    ));
                }
            }

            //we have to give the students their parents.
            for(Student s:studentList.values()) {
                Map<String,Object> queryValues = new HashMap();
                queryValues.put("studentID",s.getStudentID());

                Map<Integer,Parent> parents = selectParent(queryValues);
                for (Integer i : parents.keySet()) {
                    s.setParent(i, parents.get(i));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentList;
    }

    public Map<Integer,Parent> selectParent(Map<String,Object> values) {
        String sql = "SELECT * FROM parent" +
                " LEFT JOIN address ON parent.addressID = address.addressID" +
                " LEFT JOIN parentStudent ON parent.parentID = parentStudent.parentID";
        Map<Integer, Parent> parentList = new HashMap();

        /*
        Check to see if the values map is empty or not. If it is
        we will simply query all parents. Otherwise, we have to do
        some string manipulation.
         */
        if (values.size() != 0) {
            //adding "WHERE *value* LIKE ?" to the sql string for each
            //value for filtering.
            int index = 0;
            sql += " WHERE";
            for (String s : values.keySet()) {
                switch(s) {
                    case "studentID":
                        sql += " parentStudent." + s + " = ?";
                        index++;
                        break;
                    case "parentID":
                        sql += " parent." + s + " = ?";
                        index++;
                        break;
                    default:
                        sql += " parent." + s + " LIKE ?";
                        index++;
                        break;
                }
                //add an "and" if we aren't at the end of the values.
                if (index < values.keySet().size()) {
                    sql += " and";
                }
            }
        }

        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            //set the variables we added earlier (LIKE ?).
            if(values.size() != 0) {
                int index = 1;
                for (Object v : values.values()) {
                    switch (v.getClass().toString()) {
                        case "class java.lang.Integer":
                            stmt.setInt(index++, (int) v);
                            break;
                        case "class java.lang.String":
                            String s = "%" + v + "%";
                            stmt.setString(index++, s);
                            break;
                    }
                }
            }

            //execute query
            ResultSet rs = stmt.executeQuery();

            //while rs has values
            while (rs.next()) {
                if(!parentList.containsKey(rs.getInt("parentID"))) {
                    parentList.put(rs.getInt("parentID"), new Parent(
                            rs.getInt("parentID"),
                            rs.getInt("addressID"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("occupation"),
                            rs.getString("phone"))
                    );
                    //give the parent we just added their address.
                    parentList.get(rs.getInt("parentID")).setAddress(new Address(
                            rs.getInt("addressID"),
                            rs.getString("streetNameNumber"),
                            rs.getString("city"),
                            rs.getString("state"),
                            rs.getInt("zip")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parentList;
    }

    public Address selectAddress(int addressID) {
        String sql = "select * from address where addressID = ?";
        Address address = null;
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, addressID);

            ResultSet r = stmt.executeQuery();

            if (r.next()) {
                address = new Address(
                        r.getInt("addressID"),
                        r.getString("streetNameNumber"),
                        r.getString("city"),
                        r.getString("state"),
                        r.getInt("zip")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return address;
    }

    public void updateStudent(Student student) {
        String studentSQL = "update student set addressID = ?, firstName = ?, lastName = ?, phone1 = ?, phone2 = ?" +
                " WHERE studentID = ?";
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(studentSQL)) {
            stmt.setInt(1, student.getAddressID());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getPhone1());
            stmt.setString(5, student.getPhone2());
            stmt.setInt(6, student.getStudentID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateParent(Parent parent) {
        String parentSQL = "update parent set addressID = ?, firstName = ?, lastName = ?, occupation = ?, phone = ?" +
                " WHERE parentID = ?";
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(parentSQL)) {
            stmt.setInt(1, parent.getAddressID());
            stmt.setString(2, parent.getFirstName());
            stmt.setString(3, parent.getLastName());
            stmt.setString(4, parent.getOccupation());
            stmt.setString(5, parent.getPhone());
            stmt.setInt(6, parent.getParentID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAddress(Address address) {
        String addressSQL = "update address set streetNameNumber = ?, city = ?, state = ?, zip =?" +
                " WHERE addressID = ?";
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(addressSQL)) {
            stmt.setString(1, address.getStreetNameNumber());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getState());
            stmt.setInt(4, address.getZip());
            stmt.setInt(5, address.getAddressID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateApplication(Application app) {
        String sql = "update application set" +
                " studentID = ?, amountAwarded = ?, year = ?, classPercentile = ?," +
                " highschool = ?, gpa = ?, actScore = ?, satScore = ?, collegeAttending = ?,"+
                " fieldOfStudy = ?, collegeID = ?, hasRecommendationLetter = ?, numberOfScholarships = ?,"+
                " scholarshipAmounts = ?, efcNumber = ?, householdSize = ?, householdInCollege = ?,"+
                " hasReceivedAngelFundSupportPrior = ?, pdfFileLocation = ?" +
                " where applicationID = ?";
        try(Connection conn = this.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1,app.getStudentID());
            stmt.setInt(2,app.getAmountAwarded());
            stmt.setString(3,app.getYear());
            stmt.setFloat(4,app.getClassPercentile());
            stmt.setString(5,app.getHighSchool());
            stmt.setFloat(6,app.getGpa());
            stmt.setInt(7,app.getSatScore());
            stmt.setInt(8,app.getSatScore());
            stmt.setString(9,app.getCollegeAttending());
            stmt.setString(10,app.getFieldOfStudy());
            stmt.setLong(11,app.getCollegeID());
            stmt.setString(12,app.getHasRecommendationLetter());
            stmt.setInt(13,app.getNumberOfScholarships());
            stmt.setLong(14,app.getScholarshipAmounts());
            stmt.setLong(15,app.getEfcNumber());
            stmt.setInt(16,app.getHouseholdSize());
            stmt.setInt(17,app.getHouseholdInCollege());
            stmt.setString(18,app.getHasReceivedAngelFundSupportPrior());
            stmt.setString(19,app.getPdfFileLocation()+"");
            stmt.setInt(20,app.getApplicationID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int createStudent(Student student) {
        String studentSQL = "insert into student (addressID, firstName, lastName, phone1, phone2)" +
                " values (?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(studentSQL)) {
            stmt.setInt(1, student.getAddressID());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getPhone1());
            stmt.setString(5, student.getPhone2());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //We have to get the ID of the student we just added.
        //This is a different way of getting the ID than the other
        //create statements. We will see which works better or
        //if they are functionally the same.
        Map<String,Object> queryValues = new HashMap();
        queryValues.put("addressID",student.getAddressID());
        queryValues.put("firstName",student.getFirstName());
        queryValues.put("lastName",student.getLastName());
        queryValues.put("phone1",student.getPhone1());
        queryValues.put("phone2",student.getPhone2());
        Map<Integer,Student> selectedStudent = selectStudent(queryValues);
        for(Integer i: selectedStudent.keySet())
            return(i);
        //return if above didn't work
        return(-1);
    }

    public int createParent(Parent parent) {
        int retVal = -1;
        String parentSQL = "insert into parent (addressID, firstName, lastName, occupation, phone)" +
                " values (?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(parentSQL)) {
            stmt.setInt(1, parent.getAddressID());
            stmt.setString(2, parent.getFirstName());
            stmt.setString(3, parent.getLastName());
            stmt.setString(4, parent.getOccupation());
            stmt.setString(5, parent.getPhone());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String querySQL = "select parentID from parent where addressID = ? and firstName = ?" +
                " AND lastName = ?";
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            stmt.setInt(1, parent.getAddressID());
            stmt.setString(2, parent.getFirstName());
            stmt.setString(3, parent.getLastName());

            ResultSet r = stmt.executeQuery();

            if (r.next()) {
                retVal = r.getInt("parentID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (retVal);
    }

    public int createAddress(Address address) {
        int retVal = -1;
        String addressSQL = "insert into address (streetNameNumber, city, state, zip)" +
                " values (?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(addressSQL)) {
            stmt.setString(1, address.getStreetNameNumber());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getState());
            stmt.setInt(4, address.getZip());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String querySQL = "select addressID from address where streetNameNumber = ? and city = ? and state = ? and zip = ?";
        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            stmt.setString(1, address.getStreetNameNumber());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getState());
            stmt.setInt(4,address.getZip());

            ResultSet r = stmt.executeQuery();

            if (r.next()) {
                retVal = r.getInt("addressID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (retVal);
    }

    public void createParentStudent(int studentID, int parentID) {
        String sql = "insert into parentStudent (studentID, parentID) values (?,?)";

        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentID);
            stmt.setInt(2, parentID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer,Application> selectApplication(Map<String, Object> values) {
        String sql = "SELECT * FROM application" +
                " LEFT JOIN student ON application.studentID = student.studentID";
        Map<Integer, Application> applicationList = new HashMap();

        if (values.size() != 0) {
            sql += " WHERE";
            int index = 0;
            for (String s : values.keySet()) {
                switch (s) {
                    case "firstName":
                    case "lastName":
                        sql += " student." + s + " LIKE ?";
                        index++;
                        break;
                    case "applicationID":
                    case "studentID":
                        sql += " application." + s + " = ?";
                        index++;
                        break;
                    default:
                        sql += " application." + s + " LIKE ?";
                        index++;
                        break;
                }
                if (index < values.keySet().size()) {
                    sql += " and";
                }
            }
        }

        sql += " ORDER BY application.applicationID ASC";

        try (Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            for (Object v : values.values()) {
                switch (v.getClass().toString()) {
                    case "class java.lang.Integer":
                        stmt.setInt(index++, (int) v);
                        break;
                    case "class java.lang.String":
                        String s = "%" + v + "%";
                        stmt.setString(index++, s);
                        break;
                }
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if(!applicationList.containsKey(rs.getInt("applicationID"))) {

                    Map<String,Object> queryMap = new HashMap();
                    queryMap.put("studentID",rs.getInt("studentID"));
                    Student s = selectStudent(queryMap).get(rs.getInt("studentID"));

                    applicationList.put(rs.getInt("applicationID"), new Application(
                            rs.getInt("applicationID"),
                            rs.getInt("studentID"),
                            s,
                            rs.getInt("amountAwarded"),
                            rs.getString("year"),
                            rs.getFloat("classPercentile"),
                            rs.getString("highSchool"),
                            rs.getFloat("gpa"),
                            rs.getInt("actScore"),
                            rs.getInt("satScore"),
                            rs.getString("collegeAttending"),
                            rs.getString("fieldOfStudy"),
                            rs.getLong("collegeID"),
                            rs.getString("hasRecommendationLetter"),
                            rs.getInt("numberOfScholarships"),
                            rs.getLong("scholarshipAmounts"),
                            rs.getLong("efcNumber"),
                            rs.getInt("householdSize"),
                            rs.getInt("householdInCollege"),
                            rs.getString("hasReceivedAngelFundSupportPrior"),
                            rs.getString("pdfFileLocation")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicationList;
    }

    public int createApplication(Application app) {
        int appID = -1;
        String sql = "insert into application" +
                " (studentID, amountAwarded, year, classPercentile," +
                " highschool, gpa, actScore, satScore, collegeAttending,"+
                " fieldOfStudy, collegeID, hasRecommendationLetter, numberOfScholarships,"+
                " scholarshipAmounts, efcNumber, householdSize, householdInCollege,"+
                " hasReceivedAngelFundSupportPrior, pdfFileLocation)" +
                " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try(Connection conn = this.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1,app.getStudentID());
            stmt.setInt(2,app.getAmountAwarded());
            stmt.setString(3,app.getYear());
            stmt.setFloat(4,app.getClassPercentile());
            stmt.setString(5,app.getHighSchool());
            stmt.setFloat(6,app.getGpa());
            stmt.setInt(7,app.getSatScore());
            stmt.setString(8,app.getCollegeAttending());
            stmt.setString(9,app.getFieldOfStudy());
            stmt.setLong(10,app.getCollegeID());
            stmt.setString(11,app.getHasRecommendationLetter());
            stmt.setInt(12,app.getNumberOfScholarships());
            stmt.setLong(13,app.getScholarshipAmounts());
            stmt.setLong(14,app.getEfcNumber());
            stmt.setInt(15,app.getHouseholdSize());
            stmt.setInt(16,app.getHouseholdInCollege());
            stmt.setString(17,app.getHasReceivedAngelFundSupportPrior());
            stmt.setString(18,app.getPdfFileLocation()+"");

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<String,Object> queryValues = new HashMap();
        queryValues.put("year",app.getYear());
        queryValues.put("studentID",app.getStudentID());
        Map<Integer,Application> selectedApp = selectApplication(queryValues);
        for(Integer i: selectedApp.keySet())
            return(i);

        return appID;
    }

    public void delete(String table, int id) {
        String sql = "DELETE FROM "+table+" WHERE "+table+"ID"+" = ?";

        if(table == "student") {
            deleteParentStudent(-1,id);

            Map<String, Object> query = new HashMap();
            query.put("studentID",id);
            Map<Integer, Application> result = selectApplication(query);
            for(Integer i : result.keySet()) {
                delete("application",i);
            }

            query = new HashMap();
            query.put("studentID",id);
            Map<Integer, Student> result1 = selectStudent(query);
            delete("address", result1.get(id).getAddressID());
        } else if (table == "parent") {
            deleteParentStudent(id,-1);

            Map<String, Object> query = new HashMap();
            query.put("parentID",id);
            Map<Integer, Parent> result = selectParent(query);
            delete("address",result.get(id).getAddressID());
        } else if (table == "application") {
            Map<String, Object> query = new HashMap();
            query.put("applicationID",id);
            Map<Integer, Application> result = selectApplication(query);
            if(result.get(id).getPdfFileLocation() != null) {
                File pdf = new File(result.get(id).getPdfFileLocation());
                if(pdf.exists()) {
                    pdf.delete();
                }
            }
        }

        try(Connection conn = this.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1,id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Put -1 for a value if you only want to delete things with one of those id's
     * @param parentID
     * @param studentID
     */
    public void deleteParentStudent(int parentID, int studentID) {
        String sql = "DELETE FROM parentStudent WHERE parentID = ? or studentID = ?";

        try(Connection conn = this.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1,parentID);
            stmt.setInt(2,studentID);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}