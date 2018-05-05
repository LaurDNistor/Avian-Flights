package com.company;
/*
Object to represent a route.
Contains:
- fromTo;
- seats;
*/
public class Route
{
  private String fromTo;
  private Seat seats[];
  private double price;
  private int[] flights;

  public Route()
  {
    flights = new int[14];
    for (int i = 0; i < 14; i++)
      flights[i] = 0;
  }

  public Seat[] getSeats()
  {
    return seats;
  }

  public void setSeats(Seat[] seats)
  {
    this.seats = seats;
  }

  public void setFromTo(String fromTo)
  {
    this.fromTo = fromTo;
  }

  public String getFromTo()
  {
    return fromTo;
  }

  public double getPrice()
  {
    return price;
  }

  public void setPrice(double price)
  {
    this.price = price;
  }

  public int[] getFlights()
  {
    return flights;
  }

  public void setFlights(int[] flights)
  {
    this.flights = flights;
  }
}
