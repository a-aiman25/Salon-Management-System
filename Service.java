import java.util.Objects; // ADDED THIS IMPORT

class Service {
    private int id;
    private String name;
    private int duration; // in minutes
    private int priority; // Lower number means higher priority (e.g., 1 is highest)
    private double price;
    private String department; // e.g., "Hair Section", "Makeup", "Nails", "Facial Services", "Body Services"

    // Constructor for existing services fetched from DB
    public Service(int id, String name, int duration, int priority, double price, String department) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.priority = priority;
        this.price = price;
        this.department = department;
    }

    // Constructor for new services (before saving to DB, ID is auto-generated)
    public Service(String name, int duration, int priority, double price, String department) {
        this(0, name, duration, priority, price, department); // id will be set by DB
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getDuration() { return duration; }
    public int getPriority() { return priority; }
    public double getPrice() { return price; }
    public String getDepartment() { return department; }

    // Setters (for editing services)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setPrice(double price) { this.price = price; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() {
        return name + " (" + duration + "min, Priority:" + priority + ", Dept: " + department + ")";
    }

    // Needed for JComboBox and List selection to correctly identify Service objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return id == service.id &&
               duration == service.duration &&
               priority == service.priority &&
               Double.compare(service.price, price) == 0 &&
               Objects.equals(name, service.name) &&
               Objects.equals(department, service.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, duration, priority, price, department);
    }
}