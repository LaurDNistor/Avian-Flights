package com.company;
/*
  Object to represent a seat.
  Contains:
  - An unique ID;
  - Price;
  - Customer;
  - Availability;
  - Flight name.
*/

public class Seat
{
  // Used to generate unique IDs each time a seat is created
  private static long bookingNoGenerator = 1;
  private long id;
  private Client client;
  private boolean availability;
  private long bookingNumber;


  // Constructor
  public Seat()
  {
    availability = true;
    bookingNumber = bookingNoGenerator++;
    client = null;
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public long getBookingNumber()
  {
    return bookingNumber;
  }

  public boolean isAvailable()
  {
    return availability;
  }

  public void removeClient()
  {
    client = null;
    availability = true;
  }

  public Client getClient()
  {
    return client;
  }

  public void setClient(Client client)
  {
    this.client = client;
    availability = false;
  }

}