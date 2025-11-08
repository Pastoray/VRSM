package pkg.vehicules;

import java.lang.reflect.Field;

enum FuelType
{
    GASOLINE,
    DIESEL,
    KEROSENE
}

public abstract class Vehicule
{
    private static long g_id = 0;
    private long id;
    private String brand;
	private String model;
    private int year;
    private double price;
    private boolean available;
    private FuelType fuel_type;

    public Vehicule(String brand, String model, int year, double price, FuelType fuel_type)
    {
            this.id = genID();

            this.brand = brand;
            this.model = model;
            this.year = year;
            this.price = price;
            this.available = true;
            this.fuel_type = fuel_type;
    }

    private static long genID()
    {
        return Vehicule.g_id++;
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
        }
        catch (Exception e)
        {
            System.err.println(e.getStackTrace());
        }
    }

   	public long get_id() {
		return id;
	}

	public void set_id(long id) {
		this.id = id;
	}

	public String get_brand() {
		return brand;
	}

	public void set_brand(String brand) {
		this.brand = brand;
	}

	public String get_model() {
		return model;
	}

	public void set_model(String model) {
		this.model = model;
	}

	public int get_year() {
		return year;
	}

	public void set_year(int year) {
		this.year = year;
	}

	public double get_price() {
		return price;
	}

	public void set_price(double price) {
		this.price = price;
	}

	public boolean is_available() {
		return available;
	}

	public void set_available(boolean available) {
		this.available = available;
	}

	public FuelType get_fuel_type() {
		return fuel_type;
	}

	public void set_fuel_type(FuelType fuel_type) {
		this.fuel_type = fuel_type;
	}
}
