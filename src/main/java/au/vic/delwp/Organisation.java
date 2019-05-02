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

public class Organisation {

	public int ID;
	public String Name;
	public String Streetaddress;
	public String Postaladdress;
	public String City;
	public String Stateorprovince;
	public String Postalcode;
	public String Country;
	public String Type;
	public String Stakeholdertype;

  public boolean hasAddress() {
    return !StringUtils.isBlank(Postaladdress);
  }
  
  public boolean hasCity() {
    return !StringUtils.isBlank(City);
  }
  
  public boolean hasPostcode() {
    return !StringUtils.isBlank(Postalcode);
  }
  
  public boolean hasStateOrProvince() {
    return !StringUtils.isBlank(Stateorprovince);
  }
  
  public boolean hasCountry() {
    return !StringUtils.isBlank(Country);
  }
  
}
