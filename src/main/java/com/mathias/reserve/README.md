# Reserve

## Railway Ticketing System

#### Project Overview

Reserve is a comprehensive Railway Ticketing System designed to simplify and enhance the ticket booking experience. The system allows users to book tickets, cancel bookings, and view ticket details while providing administrators the ability to manage tickets and bookings effectively.

##### Features

###### User Features:

* Book tickets for available trains.
* View booking details and ticket information.
* Cancel tickets with ease.
* Search for tickets by ticket number.

###### Admin Features:

* Create, update, and delete tickets.
* Manage bookings for all users.
* View passengers associated with a specific ticket.
* Filter tickets and bookings by date.


###### Pain Points Addressed

1. Manual Booking Inefficiencies: Automates the booking process, reducing time and human error.
2. Cancellation Tracking: Simplifies ticket cancellations with clear record-keeping.
3. Passenger-Ticket Management: Easily associates users with their respective bookings for better organization.
4. Data Filtering: Provides daily, monthly, and yearly filtering for better management and reporting.
5. User Experience: A streamlined, user-friendly interface for passengers and administrators alike.

##### Technologies Used

* Backend: Spring Boot (Java)
* Database: MySQL (or your preferred RDBMS)
* Authentication & Security: Spring Security
* Build Tool: Maven
* Version Control: Git

#### Getting Started

##### Prerequisites

1. Java 17 or later installed.
2. MySQL Database (or compatible RDBMS).
3. Maven for dependency management.
4. IDE like IntelliJ IDEA or Eclipse.

##### Steps to Run the Project
1. Clone the repository:
`git clone https://github.com/your-username/reserve.git`
2. Navigate to the project directory:` cd reserve`
3. Configure the database in application.properties:
  ` spring.datasource.url=jdbc:mysql://localhost:3306/reserve
   spring.datasource.username=your-username
   spring.datasource.password=your-password`
4. Run the application: `mvn spring-boot:run`
5. Access the application at: 
   * User Interface: http://localhost:8080
   *  API Documentation (if enabled): 


##### Endpoints

###### Public Endpoints

* GET /api/tickets: View all tickets.
* GET /api/tickets/{ticketNo}/passengers: View passengers for a specific ticket.
* GET /api/bookings/{bookingNo}: View booking and ticket details.

###### Admin Endpoints
* POST /api/tickets: Add a new ticket.
* DELETE /api/tickets/{ticketId}: Delete a ticket.
* DELETE /api/bookings/{bookingId}: Delete a booking.

   



