// DatabaseManager.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar; // Added for time calculations
import java.util.Comparator; // ADDED THIS IMPORT

class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:salon.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // !!! IMPORTANT: Drop tables to ensure fresh schema during development !!!
            // This ensures any schema changes (like adding start_time/end_time) are applied.
            stmt.execute("DROP TABLE IF EXISTS appointments;");
            stmt.execute("DROP TABLE IF EXISTS users;");
            stmt.execute("DROP TABLE IF EXISTS services;");


            // Create Users table
            String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," + // In a real app, hash and salt this!
                    "role TEXT NOT NULL" +      // 'Admin' or 'Receptionist'
                    ");";
            stmt.execute(createUsersTableSQL);

            // Create Services table
            String createServicesTableSQL = "CREATE TABLE IF NOT EXISTS services (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "duration INTEGER NOT NULL," + // in minutes
                    "priority INTEGER NOT NULL," + // lower number means higher priority
                    "price REAL NOT NULL," +
                    "department TEXT NOT NULL" +    // e.g., 'Hair', 'Makeup', 'Nails', 'Facial', 'Body'
                    ");";
            stmt.execute(createServicesTableSQL);

            // Create Appointments table
            // Added booking_timestamp column
            String createAppointmentsTableSQL = "CREATE TABLE IF NOT EXISTS appointments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customer_name TEXT NOT NULL," +
                    "service_id INTEGER NOT NULL," +
                    "start_time INTEGER NOT NULL," + // Storing start time in minutes from start of day (00:00)
                    "end_time INTEGER NOT NULL," +   // Storing end time in minutes from start of day
                    "booking_timestamp INTEGER NOT NULL," + // Storing booking time as Unix timestamp (milliseconds)
                    "is_completed BOOLEAN NOT NULL," +
                    "FOREIGN KEY (service_id) REFERENCES services(id)" +
                    ");";
            stmt.execute(createAppointmentsTableSQL);

            // Insert default users if they don't exist
            if (!userExists("admin", "Admin")) {
                insertUser("admin", "admin123", "Admin");
            }
            if (!userExists("receptionist", "rec123")) {
                insertUser("receptionist", "rec123", "Receptionist");
            }

            // Insert default services if they don't exist
            if (getAllServices().isEmpty()) {
                insertService(new Service("Haircut", 45, 2, 4000.00, "Hair Section"));
                insertService(new Service("Hair Coloring", 90, 1, 10000.00, "Hair Section"));
                insertService(new Service("Manicure", 30, 3, 5000.00, "Nails"));
                insertService(new Service("Pedicure", 45, 3, 7000.00, "Nails"));
                insertService(new Service("Bridal Makeup", 120, 1, 75000.00, "Makeup"));
                insertService(new Service("Evening Makeup", 60, 2, 20000.00, "Makeup"));
                insertService(new Service("Basic Facial", 60, 2, 3500.00, "Facial Services"));
                insertService(new Service("Anti-Aging Facial", 90, 1, 6000.00, "Facial Services"));
                insertService(new Service("Full Body Massage", 75, 2, 15000.00, "Body Services"));
                insertService(new Service("Waxing (Full Leg)", 60, 3, 4500.00, "Body Services"));
            }

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    // --- User Operations ---
    public static User authenticateUser(String username, String password, String role) { // Corrected parameter type
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return null;
    }

    public static boolean userExists(String username, String role) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND role = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    public static void insertUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            System.out.println("User '" + username + "' inserted.");
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, role FROM users";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("role")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        }
        return users;
    }

    public static void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Remember: in real app, hash
            pstmt.setString(3, user.getRole());
            pstmt.setInt(4, user.getId());
            pstmt.executeUpdate();
            System.out.println("User '" + user.getUsername() + "' updated.");
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    public static void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("User with ID " + userId + " deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    // --- Service Operations ---
    public static void insertService(Service service) {
        String sql = "INSERT INTO services(name, duration, priority, price, department) VALUES(?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, service.getName());
            pstmt.setInt(2, service.getDuration());
            pstmt.setInt(3, service.getPriority());
            pstmt.setDouble(4, service.getPrice());
            pstmt.setString(5, service.getDepartment());
            pstmt.executeUpdate();
            System.out.println("Service '" + service.getName() + "' inserted.");
        } catch (SQLException e) {
            System.err.println("Error inserting service: " + e.getMessage());
        }
    }

    public static List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                services.add(new Service(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getInt("priority"),
                        rs.getDouble("price"),
                        rs.getString("department")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all services: " + e.getMessage());
        }
        return services;
    }

    public static Service getServiceById(int serviceId) {
        String sql = "SELECT * FROM services WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Service(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getInt("priority"),
                        rs.getDouble("price"),
                        rs.getString("department")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching service by ID: " + e.getMessage());
        }
        return null;
    }

    public static void updateService(Service service) {
        String sql = "UPDATE services SET name = ?, duration = ?, priority = ?, price = ?, department = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, service.getName());
            pstmt.setInt(2, service.getDuration());
            pstmt.setInt(3, service.getPriority());
            pstmt.setDouble(4, service.getPrice());
            pstmt.setString(5, service.getDepartment());
            pstmt.setInt(6, service.getId());
            pstmt.executeUpdate();
            System.out.println("Service '" + service.getName() + "' updated.");
        } catch (SQLException e) {
            System.err.println("Error updating service: " + e.getMessage());
        }
    }

    public static void deleteService(int serviceId) {
        String sql = "DELETE FROM services WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            pstmt.executeUpdate();
            System.out.println("Service with ID " + serviceId + " deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting service: " + e.getMessage());
        }
    }

    // --- Appointment Operations ---
    public static void insertAppointment(CustomerAppointment appointment) {
        // Updated to include booking_timestamp
        String sql = "INSERT INTO appointments(customer_name, service_id, start_time, end_time, booking_timestamp, is_completed) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, appointment.getCustomerName());
            pstmt.setInt(2, appointment.getService().getId());
            pstmt.setInt(3, appointment.getStartTime());
            pstmt.setInt(4, appointment.getEndTime());
            pstmt.setLong(5, appointment.getBookingTimestamp()); // New: booking_timestamp
            pstmt.setBoolean(6, appointment.isCompleted());
            pstmt.executeUpdate();
            System.out.println("Appointment for '" + appointment.getCustomerName() + "' inserted.");
        } catch (SQLException e) {
            System.err.println("Error inserting appointment: " + e.getMessage());
        }
    }

    public static List<CustomerAppointment> getAllAppointments() {
        List<CustomerAppointment> appointments = new ArrayList<>();
        // Updated to include booking_timestamp
        String sql = "SELECT a.id, a.customer_name, a.service_id, a.start_time, a.end_time, a.booking_timestamp, a.is_completed, " +
                "s.name, s.duration, s.priority, s.price, s.department " +
                "FROM appointments a JOIN services s ON a.service_id = s.id";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Service service = new Service(
                        rs.getInt("service_id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getInt("priority"),
                        rs.getDouble("price"),
                        rs.getString("department")
                );
                appointments.add(new CustomerAppointment(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        service,
                        rs.getInt("start_time"),
                        rs.getInt("end_time"),
                        rs.getLong("booking_timestamp"), // New: booking_timestamp
                        rs.getBoolean("is_completed")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all appointments: " + e.getMessage());
        }
        return appointments;
    }

    public static void updateAppointment(CustomerAppointment appointment) {
        // Updated to include booking_timestamp in the update statement (though not typically changed by user)
        String sql = "UPDATE appointments SET customer_name = ?, service_id = ?, start_time = ?, end_time = ?, booking_timestamp = ?, is_completed = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, appointment.getCustomerName());
            pstmt.setInt(2, appointment.getService().getId());
            pstmt.setInt(3, appointment.getStartTime());
            pstmt.setInt(4, appointment.getEndTime());
            pstmt.setLong(5, appointment.getBookingTimestamp()); // New: booking_timestamp
            pstmt.setBoolean(6, appointment.isCompleted());
            pstmt.setInt(7, appointment.getId());
            pstmt.executeUpdate();
            System.out.println("Appointment for '" + appointment.getCustomerName() + "' updated.");
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
        }
    }

    public static void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            pstmt.executeUpdate();
            System.out.println("Appointment with ID " + appointmentId + " deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
        }
    }

    /**
     * Finds the earliest available time slot for a given service, considering existing appointments
     * and a chosen scheduling algorithm.
     *
     * @param service The service to book.
     * @param algorithm The scheduling algorithm ("FCFS", "SJF", "Priority Scheduling").
     * @return The start time in minutes from midnight, or -1 if no suitable slot is found.
     */
    public static int findAvailableTimeSlot(Service service, String algorithm) {
        List<CustomerAppointment> existingAppointments = getAllAppointments();
        // Filter out completed appointments for scheduling logic
        existingAppointments = existingAppointments.stream()
                .filter(appt -> !appt.isCompleted())
                .collect(java.util.stream.Collectors.toList());

        // Sort existing appointments by start time for easier conflict checking
        existingAppointments.sort(Comparator.comparingInt(CustomerAppointment::getStartTime));

        int serviceDuration = service.getDuration();
        int earliestPossibleStartTime = getMinutesFromCurrentTime(); // Start checking from current time

        // Define salon operating hours (11 AM to 11 PM)
        int openingTime = 11 * 60;  // 11:00 AM in minutes
        int closingTime = 23 * 60; // 11:00 PM in minutes

        // If current time is before opening, start checking from opening time
        if (earliestPossibleStartTime < openingTime) {
            earliestPossibleStartTime = openingTime;
        }

        // Ensure earliestPossibleStartTime is not after closingTime (e.g., if checking past 11 PM for today)
        if (earliestPossibleStartTime >= closingTime) {
            // If current time is already past closing for today, no slots available today.
            // This assumes we only book for the current day.
            return -1;
        }


        // Iterate through time slots, starting from earliestPossibleStartTime
        for (int startTime = earliestPossibleStartTime; startTime < closingTime; startTime += 5) { // Check every 5 minutes
            int endTime = startTime + serviceDuration;

            // If the service would end after closing time, no more slots today
            if (endTime > closingTime) {
                break;
            }

            boolean conflict = false;
            for (CustomerAppointment existingAppt : existingAppointments) {
                // Check if departments conflict AND if time slots overlap
                if (existingAppt.getService().getDepartment().equals(service.getDepartment())) {
                    if ( (startTime < existingAppt.getEndTime() && endTime > existingAppt.getStartTime()) ) {
                        conflict = true;
                        // If there's a conflict, jump to the end of the conflicting appointment
                        // to avoid redundant checks within the same occupied slot.
                        startTime = existingAppt.getEndTime() - 5; // -5 because loop adds 5
                        break;
                    }
                }
            }

            if (!conflict) {
                // Found a slot!
                return startTime;
            }
        }
        return -1; // No available slot found for today
    }

    /**
     * Gets the current time in minutes from midnight.
     * @return Current time in minutes from midnight.
     */
    public static int getMinutesFromCurrentTime() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
    }
}