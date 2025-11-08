package pkg.vehicules;

class Car extends Vehicule
{
    private int number_of_doors;
    private double trunk_capacity;
    private boolean air_conditioning;
    public Car(String brand, String model, int year, double price, FuelType fuel_type, int number_of_doors, double trunk_capacity, boolean air_conditioning)
    {
        super(brand, model, year, price, fuel_type);
        this.number_of_doors = number_of_doors;
        this.trunk_capacity = trunk_capacity;
        this.air_conditioning = air_conditioning;
    }
	public int get_number_of_doors() {
		return number_of_doors;
	}
	public void set_number_of_doors(int number_of_doors) {
		this.number_of_doors = number_of_doors;
	}
	public double get_trunk_capacity() {
		return trunk_capacity;
	}
	public void set_trunk_capacity(double trunk_capacity) {
		this.trunk_capacity = trunk_capacity;
	}
	public boolean is_air_conditioning() {
		return air_conditioning;
	}
	public void set_air_conditioning(boolean air_conditioning) {
		this.air_conditioning = air_conditioning;
	}
}
