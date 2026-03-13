import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;

    public ParkingSpot(String licensePlate) {
        this.licensePlate = licensePlate;
        this.entryTime = System.currentTimeMillis();
    }
}

class ParkingLot {

    private ParkingSpot[] table;
    private int capacity;
    private int occupied = 0;
    private int totalProbes = 0;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index] != null) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index] = new ParkingSpot(licensePlate);
        occupied++;
        totalProbes += probes;

        System.out.println("parkVehicle(\"" + licensePlate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index] != null) {

            if (table[index].licensePlate.equals(licensePlate)) {

                long durationMs = System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMs / (1000.0 * 60 * 60);

                double fee = hours * 5; // $5 per hour

                table[index] = null;
                occupied--;

                System.out.println("exitVehicle(\"" + licensePlate + "\") → Spot #" +
                        index + " freed, Duration: " +
                        String.format("%.2f", hours) +
                        "h, Fee: $" + String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found");
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (occupied * 100.0) / capacity;
        double avgProbes = occupied == 0 ? 0 : (double) totalProbes / occupied;

        System.out.println("\nParking Statistics:");
        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
    }
}

public class problem8 {

    public static void main(String[] args) throws Exception {

        ParkingLot lot = new ParkingLot(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000);

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}