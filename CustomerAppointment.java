// CustomerAppointment.java
import java.util.Objects; // ADDED THIS IMPORT

class CustomerAppointment {
    private int id; // Database ID
    private String customerName;
    private Service service;
    private int startTime; // Start time in minutes from 00:00 (e.g., 9:00 AM is 540 minutes)
    private int endTime;   // End time in minutes from 00:00
    private long bookingTimestamp; // New: Timestamp when the appointment was booked (milliseconds since epoch)
    private boolean isCompleted;

    // Constructor for existing appointments fetched from DB
    public CustomerAppointment(int id, String customerName, Service service, int startTime, int endTime, long bookingTimestamp, boolean isCompleted) {
        this.id = id;
        this.customerName = customerName;
        this.service = service;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingTimestamp = bookingTimestamp;
        this.isCompleted = isCompleted;
    }

    // Constructor for new appointments (before saving to DB, ID is auto-generated)
    public CustomerAppointment(String customerName, Service service, int startTime, int endTime, long bookingTimestamp) {
        this(0, customerName, service, startTime, endTime, bookingTimestamp, false); // Default to not completed
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public Service getService() { return service; }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }
    public long getBookingTimestamp() { return bookingTimestamp; } // Getter for new field
    public boolean isCompleted() { return isCompleted; }

    public void setId(int id) { this.id = id; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setService(Service service) { this.service = service; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public void setEndTime(int endTime) { this.endTime = endTime; }
    public void setBookingTimestamp(long bookingTimestamp) { this.bookingTimestamp = bookingTimestamp; } // Setter for new field
    public void setCompleted(boolean completed) { this.isCompleted = completed; }

    @Override
    public String toString() {
        return customerName + " - " + service.getName() + " (Priority: " + service.getPriority() + ")";
    }

    // Needed for list models and object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAppointment that = (CustomerAppointment) o;
        return id == that.id; // Equality based on DB ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}