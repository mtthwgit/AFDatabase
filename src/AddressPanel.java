

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import java.text.ParseException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.text.MaskFormatter;

public class AddressPanel extends JPanel {
  
  private static final long serialVersionUID = -3590260317985798634L;

  @SuppressWarnings("unused")
  private Address address;
  
  private JTextField streetName;
  private JTextField city;
  @SuppressWarnings("rawtypes")
  private JComboBox state;
  private JFormattedTextField zipCode;
  private int addressID;
  
  /**
   * @desc Create the panel for address editing.
   */
  @SuppressWarnings({ "unchecked", "rawtypes"})
  public AddressPanel() {
    
    address = new Address();
    setLayout(new FormLayout(new ColumnSpec[] {
        FormSpecs.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"),
        FormSpecs.RELATED_GAP_COLSPEC,
        FormSpecs.DEFAULT_COLSPEC,
        FormSpecs.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"),
        FormSpecs.RELATED_GAP_COLSPEC,},
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
        FormSpecs.DEFAULT_ROWSPEC,}));
    
    JLabel lblStreetName = new JLabel("Street Name");
    add(lblStreetName, "2, 4, 5, 1");
    
    streetName = new JTextField();
    add(streetName, "2, 6, 5, 1, fill, default");
    streetName.setColumns(10);
    
    lblStreetName.setLabelFor(streetName);
    
    JLabel lblCity = new JLabel("City");
    add(lblCity, "2, 8");
    
    city = new JTextField();
    add(city, "2, 10, fill, default");
    city.setColumns(10);
    
    lblCity.setLabelFor(city);
    
    JLabel lblState = new JLabel("State");
    add(lblState, "6, 8");
    
    state = new JComboBox();
    state.setModel(new DefaultComboBoxModel(new String[] {"Select State", "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"}));
    add(state, "6, 10, fill, default");
    
    lblState.setLabelFor(state);
    
    JLabel lblZipCode = new JLabel("Zip Code");
    add(lblZipCode, "2, 12, 5, 1");
    
    try {
    zipCode = new JFormattedTextField(new MaskFormatter("#####"));
    lblZipCode.setLabelFor(zipCode);
    add(zipCode, "2, 14, 5, 1, fill, default");
    } catch (ParseException e) {
      e.printStackTrace();
   }

  }
 
  public Address getNewAddress() {
    Address address = new Address();

    address.setStreetNameNumber(streetName.getText());
    address.setCity(city.getText());
    if(zipCode.getText().replace(" ","").length() > 0) {
      address.setZip(Integer.parseInt(zipCode.getText()));
    }
    if(state.getSelectedIndex() != 0) {
      address.setState(state.getSelectedItem().toString());
    } else {
      address.setState(null);
    }

    return address;
  }

  public Address getAddress() {
    Address address = new Address();

    address.setAddressID(addressID);
    address.setStreetNameNumber(streetName.getText());
    address.setCity(city.getText());
    String temp = zipCode.getText().replace(" ","");
    if(temp.length() > 0) {
      address.setZip(Integer.parseInt(temp));
    }
    if(state.getSelectedIndex() != 0) {
      address.setState(state.getSelectedItem().toString());
    } else {
      address.setState(null);
    }

    return address;
  }
  
  public void updatePanel(Address address) {
    this.address = address;
    streetName.setText(address.getStreetNameNumber());
    city.setText(address.getCity());
    state.setSelectedItem(address.getState());
    zipCode.setText(Integer.toString(address.getZip()));
    addressID = address.getAddressID();
  }

}
