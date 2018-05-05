package com.company;
import java.util.Date;

  /*
  Object to represent a customer.
  Contains:
  - First name;
  - Surname;
  - Date of birth;
  - Address;
  - Postcode;
  - Phone number;
  - Email;
  - Passsport number.
  (In case of under 16)
  - Parent name;
  - Parent phone;
  - Parent email;
  - Parent address;
*/
public class Client
{
  private String name;
  private String surname;
  private Date   dateOfBirth;
  private String address;
  private String postcode;
  private String phoneNumber;
  private String email;
  private String passsportNumber;

  private String parentName;
  private String parentPhone;
  private String parentEmail;
  private String parentAddress;

  public Client()
  {
    name = null;
    surname = null;
    dateOfBirth = null;
    address = null;
    postcode = null;
    phoneNumber = null;
    email = null;
    passsportNumber = null;
    parentName = null;
    parentPhone = null;
    parentEmail = null;
    parentAddress = null;
  }

  public String getName() { return name; }
  public String getSurname() { return surname; }
  public Date getDateOfBirth() { return dateOfBirth; }
  public String getPostcode() { return postcode; }
  public String getAddress() { return address; }
  public String getPhoneNumber() { return phoneNumber; }
  public String getEmail() { return email; }
  public String getPasssportNumber() { return passsportNumber; }
  public void setName(String name) { this.name = name; }
  public void setSurname(String surname) { this.surname = surname; }
  public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
  public void setPostcode(String postcode) { this.postcode = postcode; }
  public void setAddress(String address) { this.address = address; }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
  public void setEmail(String email) { this.email = email; }
  public void setPasssportNumber(String passsportNumber) { this.passsportNumber = passsportNumber; }


  public void setParentName(String parentName)
  { this.parentName = parentName; }

  public void setParentPhone(String parentPhone)
  { this.parentPhone = parentPhone; }

  public void setParentEmail(String parentEmail)
  { this.parentEmail = parentEmail; }

  public void setParentAddress(String parentAddress)
  { this.parentAddress = parentAddress; }

  public String toString()
  {
    if (parentName == null)
      return "   Client: " + name + " " + surname + "\n" +
             "           " + dateOfBirth.getMonth() + " " + dateOfBirth.getYear() + "\n" +
             "           " + address + " " + postcode + "\n" +
             "           " + phoneNumber + "\n" +
             "           " + email + "\n" +
             "           " + passsportNumber;
    else
      return "Client  " + name + " " + surname + "\n" +
          "           " + dateOfBirth.getMonth() + " " + dateOfBirth.getYear() + "\n" +
          "           " + address + " " + postcode + "\n" +
          "           " + phoneNumber + "\n" +
          "           " + email + "\n" +
          "           " + passsportNumber + "\n\n" +
          "   Parent: " + parentName + "\n" +
          "           " + parentPhone + "\n" +
          "           " + parentEmail + "\n" +
          "           " + parentAddress;
  }

}
