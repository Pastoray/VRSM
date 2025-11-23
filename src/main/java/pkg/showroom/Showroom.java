package pkg.showroom;

import jakarta.persistence.*;
import pkg.vehicules.Vehicule;
import java.util.List;
import java.util.ArrayList;

/**
 * Entity representing a physical Showroom.
 * Manages inventory through a One-to-Many relationship with Vehicule entities.
 */
@Entity
@Table(name = "SHOWROOMS")
public class Showroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LOCATION")
    private String location;

    // Maps to the 'showroom' field in the Vehicule entity.
    // CascadeType.ALL ensures vehicles are removed if the showroom is deleted.
    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicule> inventory = new ArrayList<>();

    // MANDATORY: No-argument constructor for Hibernate/JPA
    public Showroom() {
    }

    /**
     * Parameterized constructor.
     */
    public Showroom(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // --- Business Methods ---

    /**
     * Adds a vehicle to the showroom's inventory and sets the reciprocal relationship.
     */
    public void addVehicule(Vehicule v) {
        if (v != null) {
            this.inventory.add(v);
            // CRITICAL: Maintain bidirectional link
            v.setShowroom(this);
        }
    }

    /**
     * Removes a vehicle from the showroom's inventory.
     */
    public void removeVehicule(Vehicule v) {
        if (v != null) {
            this.inventory.remove(v);
            v.setShowroom(null);
        }
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<Vehicule> getInventory() {
        return inventory;
    }

    public void setInventory(List<Vehicule> inventory) {
        this.inventory = inventory;
    }
}
