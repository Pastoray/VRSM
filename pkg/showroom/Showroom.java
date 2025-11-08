package pkg.showroom;

import java.util.HashMap;
import java.util.Map;
import pkg.users.Seller;
import pkg.vehicules.Vehicule;

class Showroom
{
    private Map<Seller, Vehicule> vehicules;
    public Showroom()
    {
        this.vehicules = new HashMap<>();
    }

    public Showroom(Map<Seller, Vehicule> mp)
    {
        this.vehicules.putAll(mp);
    }

    public void add_vehicules(Seller s, Vehicule v)
    {
        this.vehicules.put(s, v);
    }

    public Map<Seller, Vehicule> get_vehicules()
    {
        return this.vehicules;
    }
}
