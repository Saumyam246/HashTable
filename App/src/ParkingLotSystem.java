import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;

    ParkingSpot(String licensePlate) {
        this.licensePlate = licensePlate;
        this.entryTime = System.currentTimeMillis();
    }
}

public class ParkingLotSystem {

    private static final int TOTAL_SPOTS = 500;

    ParkingSpot[] table = new ParkingSpot[TOTAL_SPOTS];

    int occupiedSpots = 0;
    int totalProbes = 0;
    int totalParks = 0;

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % TOTAL_SPOTS;
    }

    // Park vehicle
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index] != null) {
            index = (index + 1) % TOTAL_SPOTS;
            probes++;
        }

        table[index] = new ParkingSpot(licensePlate);

        occupiedSpots++;
        totalProbes += probes;
        totalParks++;

        System.out.println("parkVehicle(\"" + licensePlate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int start = index;

        while (table[index] != null) {

            if (table[index].licensePlate.equals(licensePlate)) {

                long duration =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = duration / (1000.0 * 60 * 60);

                double fee = hours * 5; // $5 per hour

                table[index] = null;
                occupiedSpots--;

                System.out.printf(
                        "exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        licensePlate, index, hours, fee);

                return;
            }

            index = (index + 1) % TOTAL_SPOTS;

            if (index == start)
                break;
        }

        System.out.println("Vehicle not found");
    }

    // Find nearest free spot
    public int findNearestSpot() {

        for (int i = 0; i < TOTAL_SPOTS; i++) {

            if (table[i] == null)
                return i;
        }

        return -1;
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (occupiedSpots * 100.0) / TOTAL_SPOTS;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("\nParking Statistics:");
        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Avg Probes: %.2f\n", avgProbes);
        System.out.println("Peak Hour: 2-3 PM (simulated)");
    }

    public static void main(String[] args) {

        ParkingLotSystem lot = new ParkingLotSystem();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {}

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}