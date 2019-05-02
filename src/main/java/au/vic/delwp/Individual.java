package au.gov.vic.delwp;

import java.util.ArrayList; 
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap; 
import java.util.List; 
import java.util.Map;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.regex.*;
import java.text.ParseException;
import java.util.StringTokenizer;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class Individual {

	public int ID;
	public String Firstname;
	public String Lastname;
	public String Title;
	public String Position;
	public String Workphone;
	public String Mobilephone;
	public String Faxnumber;
	public String Email;
	public String Retired;
	public Organisation OrganisationDetails;

  public boolean hasName() {
    System.out.println("Names "+Firstname+" "+Lastname);
    return !(StringUtils.isBlank(Firstname) && StringUtils.isBlank(Lastname));
  }	

  public String getName() {
    String name = "";
    if (!StringUtils.isBlank(Title)) {
      name += Title;
    }
    name += " " + Firstname + " " + Lastname;
    return name;
  } 

  public boolean hasContactInfo() {
    System.out.println("CI "+hasPhone()+" "+hasEmail());
    return hasPhone() || hasEmail();
  }

  public boolean hasPhone() {
    return hasVoice() || hasFax() || hasMobile();
  }

  public String getPhone(){
    String phone_plus_area = Workphone;

    if (phone_plus_area == null){
      return "";
      }

    phone_plus_area = phone_plus_area.trim();

    if (phone_plus_area.equals("") || phone_plus_area.equals(".")){
      return "";
      }

    if (!phone_plus_area.startsWith("+61 3")){
      if (phone_plus_area.startsWith("(03)") || phone_plus_area.startsWith("03")){
        phone_plus_area = phone_plus_area.replaceAll("^\\s*\\(?03\\)?\\s*", "+61 3 ");
        }
      else{
        phone_plus_area = "+61 3 " + phone_plus_area;
        }
      }

    return phone_plus_area;
  }

  public String getFax(){
    String fax_plus_area = Faxnumber;

    if (fax_plus_area == null){
      return "";
      }

    fax_plus_area = fax_plus_area.trim();

    if (fax_plus_area.equals("") || fax_plus_area.equals(".")){
      return "";
      }

    if (!fax_plus_area.startsWith("+61 3")){
      if (fax_plus_area.startsWith("(03)") || fax_plus_area.startsWith("03")){
        fax_plus_area = fax_plus_area.replaceAll("^\\s*\\(?03\\)?\\s*", "+61 3 ");
        }
      else{
        fax_plus_area = "+61 3 " + fax_plus_area;
        }
      }

    return fax_plus_area;
    }

  public boolean hasVoice() {
    return !StringUtils.isBlank(Workphone);
  }

  public boolean hasFax() {
    return !StringUtils.isBlank(Faxnumber);
  }

  public boolean hasMobile() {
    return !StringUtils.isBlank(Mobilephone);
  }

  public boolean hasEmail() {
    return !StringUtils.isBlank(Email);
  }

  public boolean hasPosition() {
    return !StringUtils.isBlank(Position);
  }
}
