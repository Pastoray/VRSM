package pkg.vehicules;

import jakarta.persistence.*;

@Entity
@Table(name = "TRUCKS")
class Truck extends Vehicule
{
    @Column(name = "PAYLOAD_CAPACITY")
    private double payload_capacity;

    @Column(name = "NUMBER_OF_AXLES")
    private int number_of_axles;

    public Truck()
    {
        super();
    }
	public Truck(String brand, String model, int year, double price, FuelType fuel_type, double payload_capacity, int number_of_axles)
    {
        super(brand, model, year, price, fuel_type);
        this.payload_capacity = payload_capacity;
        this.number_of_axles = number_of_axles;
    }

    public void set_payload_capacity(double payload_capacity) {
		this.payload_capacity = payload_capacity;
	}
	public void set_number_of_axles(int number_of_axles) {
		this.number_of_axles = number_of_axles;
	}
	public double get_payload_capacity() {
		return payload_capacity;
	}
	public int get_number_of_axles() {
		return number_of_axles;
	}
}
