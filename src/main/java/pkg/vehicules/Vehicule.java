package pkg.vehicules;

import jakarta.persistence.*;
import pkg.users.Seller;
import pkg.showroom.Showroom;
import java.lang.reflect.Field;

@Entity
@Table(name = "VEHICULES")
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


    @Column(name = "FUEL_TYPE")
    @Enumerated(EnumType.STRING)
    private FuelType fuel_type;


    @ManyToOne
    @JoinColumn(name = "ASSIGNED_SELLER_ID")
    private Seller assignedSeller;


    @ManyToOne
    @JoinColumn(name = "SHOWROOM_ID")
    private Showroom showroom;


    public Vehicule()
    {

    }


    public Vehicule(String brand, String model, int year, double price, FuelType fuel_type)
    {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.available = true;
        this.fuel_type = fuel_type;
        this.assignedSeller = null;
        this.showroom = null;
    }

    public void display_info()
    {

        Class<?> cls = this.getClass();

        System.out.println("Vehicule Info: ");
        System.out.println("ID: " + this.id);

        try
        {

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
