package core.models.storage;

import core.interfaces.repository.Repository;
import core.models.airport.Passenger;
import java.util.*;
import java.util.function.Consumer;

public class PassengerRepository implements Repository<Passenger, Long> {
    private final List<Passenger> passengers = new ArrayList<>();
    private Runnable updateHandler;
    
    public void setUpdateHandler(Runnable handler) {
        this.updateHandler = handler;
    }
    
    private void notifyUpdate() {
        if (updateHandler != null) {
            updateHandler.run();
        }
    }
    
    public void updateAll(Collection<Passenger> passengersToUpdate) {
        synchronized (this) {
            passengersToUpdate.forEach(this::update);
        }
    }
    
    public void update(Passenger updatedPassenger) {
        Objects.requireNonNull(updatedPassenger, "Pasajero no puede ser nulo");
        Long passengerId = updatedPassenger.getId();
    
        synchronized (this) {
            // Buscar el índice del pasajero existente
            int index = -1;
            for (int i = 0; i < passengers.size(); i++) {
                if (passengerId.equals(passengers.get(i).getId())) {
                    index = i;
                    break;
                }
            }
        
            if (index == -1) {
                throw new IllegalArgumentException("Pasajero con ID " + passengerId + " no existe");
            }
        
            // Reemplazar el pasajero existente
            passengers.set(index, updatedPassenger);
            notifyUpdate();
        }
    }
    
    @Override
    public boolean add(Passenger passenger) {
        if (passenger == null) return false;
        if (passengers.stream().anyMatch(p -> p.getId() == passenger.getId())) return false;
        
        boolean result = passengers.add(passenger);
        if (result) notifyUpdate();
        return result;
    }
    
    @Override
    public boolean remove(Long id) {
        if (id == null) return false;
        
        boolean result = passengers.removeIf(p -> p.getId() == id);
        if (result) notifyUpdate();
        return result;
    }
    
    @Override
    public Passenger get(Long id) {
        return passengers.stream()
                       .filter(p -> p.getId() == id)
                       .findFirst()
                       .orElse(null);
    }
    
    @Override
    public List<Passenger> getAll() {
        return new ArrayList<>(passengers);
    }
    
    @Override
    public List<Passenger> getAllSorted(Comparator<Passenger> comparator) {
        List<Passenger> sorted = new ArrayList<>(passengers);
        sorted.sort(comparator);
        return sorted;
    }
    
    // Método específico para PassengerRepository
    public boolean updatePassenger(Passenger passenger) {
        Passenger existing = get(passenger.getId());
        if (existing == null) return false;
        
        existing.setFirstName(passenger.getFirstName());
        existing.setLastName(passenger.getLastName());
        existing.setCountryCode(passenger.getCountryCode());
        existing.setPhone(passenger.getPhone());
        existing.setCountry(passenger.getCountry());
        
        notifyUpdate();
        return true;
    }
}