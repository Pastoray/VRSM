package pkg.vehicules;

import jakarta.persistence.*;
import pkg.users.Seller; // Import Seller for Many-to-One relationship
import pkg.showroom.Showroom; // Import Showroom for Many-to-One relationship
import java.lang.reflect.Field; // Import for the display_info method

enum FuelType
{
    GASOLINE,
    DIESEL,
    KEROSENE
}

@Entity
@Table(name = "VEHICULES") // Keeping the original table name for simplicity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehicule
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "PROD_YEAR")
    private int year;

    @Column(name = "PRICE")
    private double price;

    @Column(name = "AVAILABLE")
    private boolean available;

    // Use EnumType.STRING to save the enum name ("GASOLINE", etc.)
    @Column(name = "FUEL_TYPE")
    @Enumerated(EnumType.STRING)
    private FuelType fuel_type;

    // Relationship 1: Back to the Seller who is assigned this vehicle
    @ManyToOne
    @JoinColumn(name = "ASSIGNED_SELLER_ID")
    private Seller assignedSeller;

    // Relationship 2: Back to the Showroom where the vehicle is stored
    @ManyToOne
    @JoinColumn(name = "SHOWROOM_ID")
    private Showroom showroom;

    // -------------------------------------------------------------
    // MANDATORY FIX: THE JPA NO-ARGUMENT CONSTRUCTOR
    // -------------------------------------------------------------
    /**
     * Required by Hibernate/JPA for instantiation when loading data from the DB.
     */
    public Vehicule()
    {
        // Keep this constructor empty.
    }

    // Parameterized constructor (used when creating a new object in Java)
    public Vehicule(String brand, String model, int year, double price, FuelType fuel_type)
    {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.available = true; // Default value set ONLY on new Java object creation
        this.fuel_type = fuel_type;
        this.assignedSeller = null; // Default to unassigned
        this.showroom = null; // Default to unassigned
    }

    /**
     * Prints detailed information about the vehicle and its subclass fields
     * using Java Reflection.
     */
    public void display_info()
    {
        // Start with base class fields
        Class<?> cls = this.getClass();

        System.out.println("Vehicule Info: ");
        System.out.println("ID: " + this.id);

        try
        {
            // Iterate over fields of the concrete class (Car, Truck, etc.)
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields)
            {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                    continue;

                field.setAccessible(true);

                String field_name = field.getName();
                Object field_value = field.get(this);
                System.out.println(field_name + ": " + field_value);

                field.setAccessible(false);
            }

            // Explicitly show base fields and relationships (safer approach for display)
            System.out.println("brand: " + this.brand);
            System.out.println("model: " + this.model);
            System.out.println("year: " + this.year);
            System.out.println("price: " + this.price);
            System.out.println("available: " + this.available);
            System.out.println("fuel_type: " + this.fuel_type);
            System.out.println("assignedSeller: " + (this.assignedSeller != null ? this.assignedSeller.getLogin() : "None"));
            System.out.println("showroom: " + (this.showroom != null ? this.showroom.getName() : "None"));
        }
        catch (Exception e)
        {
            System.err.println("Error displaying vehicle info using reflection.");
            e.printStackTrace();
        }
    }


    // --- Getters and Setters (Using standard Java/JPA camelCase) ---

    public Seller getAssignedSeller() { return assignedSeller; }
    public void setAssignedSeller(Seller assignedSeller) { this.assignedSeller = assignedSeller; }

    public Showroom getShowroom() { return showroom; }
    public void setShowroom(Showroom showroom) { this.showroom = showroom; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public FuelType getFuelType() { return fuel_type; }
    public void setFuelType(FuelType fuel_type) { this.fuel_type = fuel_type; }
}
