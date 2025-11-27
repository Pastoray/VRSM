package pkg.showroom;

import jakarta.persistence.*;
import pkg.vehicules.Vehicule;
import java.util.List;
import java.util.ArrayList;

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



    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicule> inventory = new ArrayList<>();


    public Showroom() {
    }

    public Showroom(String name, String location) {
        this.name = name;
        this.location = location;
    }


    public void addVehicule(Vehicule v) {
        if (v != null) {
            this.inventory.add(v);

            v.setShowroom(this);
        }
    }

    public void removeVehicule(Vehicule v) {
        if (v != null) {
            this.inventory.remove(v);
            v.setShowroom(null);
        }
    }



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
