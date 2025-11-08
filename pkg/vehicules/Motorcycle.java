package pkg.vehicules;

enum MotorcycleType {
    SPORT_BIKE,
    CRUISER,
    TOURING,
    STANDARD,
    DIRT_BIKE
}

class Motorcycle extends Vehicule
{
    private int engine_capacity;
    private MotorcycleType type;
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
