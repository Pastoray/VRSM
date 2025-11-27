package pkg.vehicules;

import jakarta.persistence.*;

@Entity
@Table(name = "MOTORCYCLES")
public class Motorcycle extends Vehicule
{
    @Column(name = "ENGINE_CAPACITY_CC")
    private int engine_capacity;

    @Column(name = "MOTORCYCLE_TYPE")
    @Enumerated(EnumType.STRING)
    private MotorcycleType type;

    public Motorcycle()
    {
        super();
    }

	public Motorcycle(String brand, String model, int year, double price, FuelType fuel_type, int engine_capacity, MotorcycleType type)
    {
        super(brand, model, year, price, fuel_type);
        this.engine_capacity = engine_capacity;
        this.type = type;
    }

    public int get_engine_capacity() {
		return engine_capacity;
	}
	public void set_engine_capacity(int engine_capacity) {
		this.engine_capacity = engine_capacity;
	}
	public MotorcycleType get_type() {
		return type;
	}
	public void set_type(MotorcycleType type) {
		this.type = type;
	}
}
