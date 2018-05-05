package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class AvianFlights
{
  private static String[] days;
  private static String[] time;
  private static Route[] routes;
  private static double income;
  private static double outcome;
  private static int noOfRoutes;
  private static int option;
  private static boolean exit = false;

  public static void main(String[] args) throws IOException
  {

    init();
    Scanner scanner = new Scanner(System.in);

    System.out.println("********WELCOME TO Avian Airlines********");
    System.out.println("today 29.03.2018\n");

    while (!exit)
    {
      System.out.print("1. Book a flight\n"
          + "2. Cancel a flight\n"
          + "3. View available seats\n"
          + "4. View price\n"
          + "5. List all flights\n"
          + "6. ***Management Reporting***\n"
          + "7. Exit\n"
          + "   Please select an option: ");
      option = scanner.nextInt();

      switch (option)
      {
        case 1: { bookFlight(scanner);        break; }
        case 2: { cancelFlight(scanner);      break; }
        case 3: { availableSeats(scanner);    break; }
        case 4: { prices(scanner);            break; }
        case 5: { listFlights(scanner);       break; }
        case 6: { stats(scanner);             break; }
        case 7: { outputMessage();            break; }
      }
    }
    scanner.close();

  }

  private static void init() throws IOException
  {

    try
    {
      double price = 150;
      outcome = 62.50 * 15;
      days = new String[7];
      time = new String[2];
      Scanner fileScanner = new Scanner(new File("seats.txt"));

      noOfRoutes = fileScanner.nextInt();
      routes = new Route[noOfRoutes];
      for (int i = 0; i < noOfRoutes; i++)
        routes[i] = new Route();

      fileScanner.nextLine();
      for (int i = 0; i < noOfRoutes; i++)
      {
        Seat[] seats = new Seat[5];
        for (int j = 0; j < 5; j++)
        {
          seats[j] = new Seat();
          seats[j].setId(fileScanner.nextInt());
        }
        routes[i].setPrice(price);
        routes[i].setSeats(seats);
      }

      routes[0].setFromTo("London to Cluj");
      routes[1].setFromTo("Paris to London");
      routes[2].setFromTo("Rome to Madrid");

      days[0] = "Monday";
      days[1] = "Tuesday";
      days[2] = "Wednesday";
      days[3] = "Thursday";
      days[4] = "Friday";
      days[5] = "Saturday";
      days[6] = "Sunday";

      time[0] = "Morning";
      time[1] = "Afternoon";

      fileScanner.close();

    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

  private static void bookFlight(Scanner scanner)
  {
    boolean seatsLeft = false;
    int routeOption;
    int dayOption;
    int timeOption;
    int seatOption;
    int flightCounter;

    // Selecting a route
    System.out.println("\nBooking a flight");
    for (int i = 0; i < noOfRoutes; i++)
    {
      System.out.println((i+1) + ". " + routes[i].getFromTo());
    }
    System.out.print("   Please select the wished route: ");
    routeOption = scanner.nextInt();
    while (routeOption < 1 || routeOption > noOfRoutes)
    {
      System.out.print("   Please select a valid option: ");
      routeOption = scanner.nextInt();
    }

    // Selecting a day
    System.out.println("\nBooking > " + routes[routeOption - 1].getFromTo() );
    for (int i = 0; i < 7; i++)
      System.out.println((i+1) + ". " + days[i]);
    System.out.print("   Please select the wished day: ");
    dayOption = scanner.nextInt();
    while (dayOption < 1 || dayOption > 7)
    {
      System.out.print("   Please select a valid option: ");
      dayOption = scanner.nextInt();
    }
    flightCounter = dayOption - 1;

    // Selecting a time
    System.out.println("\nBooking > " + routes[routeOption - 1].getFromTo() +
                       " > " + days[dayOption - 1]);
    System.out.print("1. Morning\n" +
                     "2. evening\n");
    System.out.print("   Please select the wished time: ");
    timeOption = scanner.nextInt();
    while (timeOption < 1 || timeOption > 2)
    {
      System.out.print("   Please select a valid option: ");
      timeOption = scanner.nextInt();
    }
    flightCounter = flightCounter + 7*(timeOption-1);

    // Selecting a seat
    System.out.println("\nBooking > " + routes[routeOption - 1].getFromTo() +
                       " > " + days[dayOption - 1] +
                       " > " + time[timeOption - 1]);
    System.out.print("   Available seats: ");
    for ( int i = 0; i < 5; i++)
    {
      if (routes[routeOption - 1].getSeats()[i].isAvailable())
      {
        seatsLeft = true;
        System.out.print(routes[routeOption - 1].getSeats()[i].getId() + " ");
      }
    }
    if (!seatsLeft)
    {
      System.out.println("There are no seats available for this flight");
    }
    else
    {
      System.out.print("\n   Please select a seat: ");
      seatOption = loopSeats(scanner, routeOption);

      // getting client info
      System.out.println("\nBooking > " + routes[routeOption - 1].getFromTo() +
          " > " + days[dayOption - 1] +
          " > " + time[timeOption - 1] +
          " > Seat " + seatOption);
      Client newClient = setNewClient(scanner);

      for (int i = 0; i < 5; i++)
      {
        if (routes[routeOption - 1].getSeats()[i].getId() == seatOption)
        {
          routes[routeOption - 1].getSeats()[i].setClient(newClient);
          updatePrice(routes[routeOption - 1]);
          routes[routeOption - 1].getFlights()[flightCounter]++;
          income += routes[routeOption - 1].getPrice();
          System.out.println("   Seat " + seatOption + " was booked\n" +
              "   Booking number: " +
              routes[routeOption - 1].getSeats()[i].getBookingNumber());
          System.out.print("   " + newClient.toString());
        }
      }
    }
    finalOption(scanner);

  }

  private static void availableSeats(Scanner scanner)
  {
    int routeOption;
    int seatOption;

    System.out.println("\nViewing available seats");
    for (int i = 0; i < noOfRoutes; i++)
      System.out.println((i + 1) + ". " + routes[i].getFromTo());
    System.out.println((noOfRoutes + 1) + ". View all routes");
    System.out.print("   Please select an option: ");
    routeOption = scanner.nextInt();

    while (routeOption < 1 || routeOption > noOfRoutes + 1)
    {
      System.out.print("   Please select a valid option: ");
      routeOption = scanner.nextInt();
    }

    if (routeOption == noOfRoutes + 1)
    {
      System.out.println("\nSeats > All routes");
      for (int i = 0; i < noOfRoutes; i++)
      {
        System.out.print("   " + routes[i].getFromTo() + ": " );
        for (int j = 0; j < 5; j++)
          if (routes[i].getSeats()[j].isAvailable())
            System.out.print(routes[i].getSeats()[j].getId() + " ");
        System.out.println();
      }
    }
    else
    {
      System.out.println("\nSeats > " + routes[routeOption - 1].getFromTo());
      System.out.print("All seats: ");
      for (int i = 0; i < 5; i++)
        System.out.print(routes[routeOption - 1].getSeats()[i].getId() + " ");

      System.out.print("\n   Please select a seat: ");
      seatOption = loopSeats(scanner, routeOption);
      for (int i = 0; i < 5; i++)
        if (routes[routeOption - 1].getSeats()[i].getId() == seatOption)
          if (routes[routeOption - 1].getSeats()[i].isAvailable())
            System.out.println("   Status: Available");
          else
          {
            System.out.println("   Status: Reserved");
            System.out.println("\n" + routes[routeOption - 1].getSeats()[i].getClient().toString());
          }
    }
    finalOption(scanner);

  }

  private static void cancelFlight (Scanner scanner)
  {
    int routeOption;
    String passportNumber;
    String option;
    boolean bookingFound = false;

    // Selecting a route
    System.out.println("\nCanceling a flight");
    for (int i = 0; i < noOfRoutes; i++)
    {
      System.out.println((i + 1) + ". " + routes[i].getFromTo());
    }
    System.out.print("   Please select the route: ");
    routeOption = scanner.nextInt();
    while (routeOption < 1 || routeOption > noOfRoutes)
    {
      System.out.print("   Please select a valid option: ");
      routeOption = scanner.nextInt();
    }
    System.out.print("   Please insert your passport number: ");
    passportNumber = scanner.next();

    System.out.println("\nCanceling > " + routes[routeOption - 1].getFromTo() + " Passport: " + passportNumber);

    for (int i = 0; i < 5; i++)
    {
      if (!routes[routeOption - 1].getSeats()[i].isAvailable())
      {
        if (Objects.equals(routes[routeOption - 1].getSeats()[i].getClient().getPasssportNumber(), passportNumber))
        {
          System.out.println("\n   The booking number is: " +
              routes[routeOption - 1].getSeats()[i].getBookingNumber());
          bookingFound = true;
          System.out.print("   Do you wish to continue(y|n)?: ");

          option = scanner.next();
          while (!Objects.equals(option, "y") && !Objects.equals(option, "n"))
          {
            System.out.print("   Please select a valid option: ");
            option = scanner.next();
          }
          if (Objects.equals(option, "y"))
          {
            routes[routeOption - 1].getSeats()[i].removeClient();
            System.out.println("\n   The booking was successfully cancelled");
            income -=routes[routeOption - 1].getPrice();
            updatePrice(routes[routeOption - 1]);
          }
        }
      }
    }
    if (!bookingFound)
      System.out.println("   There was no booking found");

    finalOption(scanner);
  }

  private static void prices (Scanner scanner)
  {
    int routeOption;

    System.out.println("\nViewing prices");
    for (int i = 0; i < noOfRoutes; i++)
      System.out.println((i + 1) + ". " + routes[i].getFromTo());
    System.out.println((noOfRoutes + 1) + ". View all prices");
    System.out.print("   Please select an option: ");
    routeOption = scanner.nextInt();

    while (routeOption < 1 || routeOption > noOfRoutes + 1)
    {
      System.out.print("   Please select a valid option: ");
      routeOption = scanner.nextInt();
    }

    if (routeOption == noOfRoutes + 1)
    {
      System.out.println("\nPrices > All routes");
      for (int i = 0; i < noOfRoutes; i++)
      {
        System.out.println("   " + routes[i].getFromTo() + ": £"+ routes[i].getPrice());
      }
    }
    else
    {
      System.out.println("\nPrices > " + routes[routeOption - 1].getFromTo());
      System.out.println("   Seats for route " + routes[routeOption - 1].getFromTo() + " cost £"+ routes[routeOption - 1].getPrice());
    }

    finalOption(scanner);
  }


  private static void listFlights(Scanner scanner)
  {
    int routeOption;

    System.out.println("\nListing flights");
    for (int i = 0; i < noOfRoutes; i++)
      System.out.println((i + 1) + ". " + routes[i].getFromTo());
    System.out.print("   Please select an option: ");
    routeOption = scanner.nextInt();
    while (routeOption < 1 || routeOption > noOfRoutes)
    {
      System.out.print("   Please select a valid option: ");
      routeOption = scanner.nextInt();
    }

    System.out.println("\nListing > " + routes[routeOption - 1].getFromTo());

    System.out.println("          ----------------------------------------- ");
    System.out.println("         | MON | TUE | WED | THU | FRI | SAT | SUN |");
    System.out.println("          ----------------------------------------- ");
    System.out.print(" morning |");
    for (int i = 0; i < 7; i++)
      System.out.print("  " + routes[routeOption - 1].getFlights()[i] + "  |");
    System.out.print("\n evening |");
    for (int i = 7; i < 14; i++)
      System.out.print("  " + routes[routeOption - 1].getFlights()[i] + "  |");
    System.out.println("\n          ----------------------------------------- ");

    finalOption(scanner);
  }

  private static void stats(Scanner scanner)
  {
    double profit = income - outcome;
    int seatsSold;
    System.out.println("\nStatistics");
    System.out.println("Seats sold:");
    for (int i = 0; i < noOfRoutes; i++)
    {
      seatsSold = 0;
      for (int j = 0; j < 14; j++)
        seatsSold += routes[i].getFlights()[j];
      System.out.println(routes[i].getFromTo() + " > " + seatsSold);
    }

    System.out.println("\nTotal profit: " + profit );

    finalOption(scanner);
  }




  private static Client setNewClient(Scanner scanner)
  {
    Date dateOfBirth = new Date();
    Client newClient = new Client();
    System.out.println("   Please provide the following information: ");
    System.out.print("   - Name: ");
    newClient.setName(scanner.next());

    System.out.print("   - Surname: ");
    newClient.setSurname(scanner.next());

    System.out.println("   - Date of birth: ");
    System.out.print("     Month ");
    dateOfBirth.setMonth(scanner.nextInt());
    System.out.print("     Year ");
    dateOfBirth.setYear(scanner.nextInt());
    newClient.setDateOfBirth(dateOfBirth);

    System.out.print("   - Address: ");
    newClient.setAddress(scanner.next());

    System.out.print("   - Postcode: ");
    newClient.setPostcode(scanner.next());

    System.out.print("   - Phone number: ");
    newClient.setPhoneNumber(scanner.next());

    System.out.print("   - Email: ");
    newClient.setEmail(scanner.next());

    System.out.print("   - Passport number: ");
    newClient.setPasssportNumber(scanner.next());

    if (under16(dateOfBirth))
    {
      System.out.print("   - Parent name: ");
      newClient.setParentName(scanner.next());

      System.out.print("   - Parent phone: ");
      newClient.setParentPhone(scanner.next());

      System.out.print("   - Parent email: ");
      newClient.setParentEmail(scanner.next());

      System.out.print("   - Parent address: ");
      newClient.setParentAddress(scanner.next());
    }
    System.out.println();

    return newClient;

  }

  private static boolean under16(Date dateOfBirth)
  {
    boolean isUnder;
    isUnder = 2018 - dateOfBirth.getYear() <= 16 && (2018 - dateOfBirth.getYear() < 16 || 3 <= dateOfBirth.getMonth());
    return isUnder;
  }

  private static void updatePrice (Route route)
  {
    double price = 150;
    for (int i = 0; i < 5; i++)
    {
      if ( !route.getSeats()[i].isAvailable() )
        price = price * 1.28;
    }
    route.setPrice(price);
  }

  private static void finalOption(Scanner scanner)
  {

    System.out.println("\n1. Main menu\n0. Exit");
    System.out.print("   Please select an option: ");
    option = scanner.nextInt();
    while (option < 0 || option > 1)
    {
      System.out.print("   Please select a valid option: ");
      option = scanner.nextInt();
    }
    if (option == 0)
      outputMessage();
    else System.out.println("\nMain menu");

  }

  private static int loopSeats(Scanner scanner, int routeOption)
  {
    int seatOption = scanner.nextInt();
    boolean loopSeat = true;
    while (loopSeat)
    {
      for (int i = 0; i < 5; i++)
        if (routes[routeOption - 1].getSeats()[i].getId() == seatOption)
          loopSeat = false;

      if (loopSeat)
      {
        System.out.print("   Please select a valid option: ");
        seatOption = scanner.nextInt();
      }
    }
    return seatOption;
  }

  private static void outputMessage()
  {

    System.out.println("\nThank you for using Avian Airlines\n");
    exit = true;

  }

}
