    package fleet;

    import vehicles.*;
    import interfaces.*;
    import exceptions.*;

    import java.io.*;
    import java.util.*;
    public class FleetManager {
        private List<Vehicle>
                    vehicles=new ArrayList<>();
        private Set<String> 
                    uniqueModels = new HashSet<>();
        private TreeSet<Vehicle> 
                    sortedByEfficiency = new TreeSet<>();
        
        public void addVehicle(Vehicle newV) 
        throws InvalidOperationException{
            for(Vehicle v:vehicles){
                if(v.getId().equals(newV.getId())){
                    throw new InvalidOperationException(
                        "Duplicate Vehicle:"+
                        newV.getId());
                }
            }
            vehicles.add(newV);
            uniqueModels.add(newV.getModel());
            sortedByEfficiency.add(newV);
            System.out.println(
                "Vehicle added successfully. Total vehicles: "
                + vehicles.size()
            );
        }

        public void removeVehicle(String id) 
            throws InvalidOperationException{
            Vehicle toRemove = null;
            for(Vehicle v : vehicles){
                if(v.getId().equals(id)){
                    toRemove = v;
                    break;
                }
            }
            
            if (toRemove == null) {
                throw new InvalidOperationException(
                    "Vehicle not found: " 
                    + id
                );
            }
            
            vehicles.remove(toRemove);
            sortedByEfficiency.remove(toRemove);
            
            rebuildUniqueModels();
            
            System.out.println(
                "Vehicle removed successfully. Total vehicles: " 
                + vehicles.size()
            );
        }

        private void rebuildUniqueModels() {
            uniqueModels.clear();
            for(Vehicle v : vehicles){
                uniqueModels.add(v.getModel());
            }
        }

        public void startAllJourneys (double distance){
            System.out.printf("-- Starting journey for${} %.2f km --%n", distance);
            double totalTime = 0;
            for(Vehicle v : vehicles){
                try {
                    v.move(distance);
                    double time = v.estimateJourneyTime(distance);
                    totalTime += time;
                    System.out.printf(
                        "Journey time %s (%s):%.2f hours%n",
                        v.getId(),
                        v.getClass().getSimpleName(),
                        time);
                }catch(Exception e){
                    System.out.printf(
                        "Failed to move %s:%s%n",
                        v.getId(),
                        e.getMessage());
                }
            }
            System.out.printf(
                "Total estimated time:%.2f hours%n",
                totalTime);
        }

        public double getTtlFuelConsumption (double distance){
            double sum = 0;
            for (Vehicle v:vehicles){
                if (v instanceof FuelConsumable){
                    try {
                        double need = distance / v.calculateFuelEfficiency();
                        sum += need;
                    }
                    catch (Exception e){}
                }
            }
            return sum;
        }

        public void maintainAll (){
            int cnt = 0;
            for (Vehicle v:vehicles){
                if (v instanceof Maintainable){
                    Maintainable m = (Maintainable) v;
                    if(m.needsMaintenance()){
                        m.performMaintenance();
                        cnt++;
                    }
                }
            }
            System.out.println(
                "Maintenance performed on " 
                + cnt 
                + " vehicle(s)."
            );
        }

        public List<Vehicle>searchByType(Class<?> type){
            List<Vehicle>result=new ArrayList<>();
            for(Vehicle v:vehicles){
                if (type.isInstance(v)) {
                    result.add(v);
                }
            }
            return result;
        }

        public void sortFleetByEfficiency (){
            Collections.sort(vehicles);
            System.out.println(
                "Fleet sorted by fuel" 
                + " efficiency (highest first)."
            );
        }

        public void sortFleetBySpeed(){
            Collections.sort(
                vehicles, 
                new Comparator<Vehicle>(){
                @Override
                public int compare(Vehicle v1,Vehicle v2){
                    return Double.compare(
                        v2.getMaxSpeed(), 
                        v1.getMaxSpeed()
                    );
                }
            });
            System.out.println(
                "Fleet sorted by maximum " 
                + "speed (highest first)."
            );
        }


        public void sortFleetByModel(){
            Collections.sort(
                vehicles, 
                new Comparator<Vehicle>(){
                @Override
                public int compare(Vehicle v1,Vehicle v2){
                    return v1.getModel().compareTo(
                        v2.getModel()
                    );
                }
            });
            System.out.println(
                "Fleet sorted by model " 
                + "name (alphabetically)."
            );
        }

        public void sortFleetById(){
            Collections.sort(
                vehicles, 
                new Comparator<Vehicle>(){
                @Override
                public int compare(
                    Vehicle v1, 
                    Vehicle v2) {
                    return v1.getId().compareTo(
                        v2.getId()
                    );
                }
            });
            System.out.println(
                "Fleet sorted by " 
                + "ID (alphabetically)."
            );
        }

        public Vehicle getFastestVehicle(){
            if (vehicles.isEmpty()){
                return null;
            }
            return Collections.max(
                vehicles, 
                new Comparator<Vehicle>(){
                @Override
                public int compare(
                    Vehicle v1, 
                    Vehicle v2){
                    return Double.compare(
                        v1.getMaxSpeed(), 
                        v2.getMaxSpeed()
                    );
                }
            });
        }

        public Vehicle getSlowestVehicle(){
            if (vehicles.isEmpty()){
                return null;
            }
            return Collections.min(
                vehicles, 
                new Comparator<Vehicle>(){
                @Override
                public int compare(
                    Vehicle v1, 
                    Vehicle v2){
                    return Double.compare(
                        v1.getMaxSpeed(), 
                        v2.getMaxSpeed()
                    );
                }
            });
        }

        public Vehicle getMostEfficientVehicle(){
            if (vehicles.isEmpty()){
                return null;
            }
            return Collections.max(
                vehicles, 
                new Comparator<Vehicle>(){
                @Override
                public int compare(
                    Vehicle v1, 
                    Vehicle v2){
                    return Double.compare(
                        v1.calculateFuelEfficiency(), 
                        v2.calculateFuelEfficiency()
                    );
                }
            });
        }

        public Vehicle getLeastEfficientVehicle(){
            if (vehicles.isEmpty()){
                return null;
            }
            return Collections.min(
                vehicles, 
                new Comparator<Vehicle>(){
                @Override
                public int compare(
                    Vehicle v1, 
                    Vehicle v2){
                    double eff1 = v1.calculateFuelEfficiency();
                    double eff2 = v2.calculateFuelEfficiency();
                    // Handle zero efficiency
                    if (eff1==0) return 1;
                    if (eff2==0) return -1;
                    return Double.compare(
                        eff1, 
                        eff2
                    );
                }
            });
        }

        public Set<String> getUniqueModels(){
            return new HashSet<>(uniqueModels);
        }

        public int getDistinctModelCount(){
            return uniqueModels.size();
        }

        public void displayUniqueModels() {
            System.out.println(
                "\n=== Unique Models" 
                + " in Fleet ==="
            );
            System.out.println(
                "Total distinct models: " 
                + uniqueModels.size()
            );
            
            // Convert to sorted list for better display
            List<String> sortedModels = new ArrayList<>(
                uniqueModels
            );
            Collections.sort(
                sortedModels
            );
            
            int count = 1;
            for(String model:sortedModels){
                System.out.println(
                    count++ 
                    + ". " 
                    + model
                );
            }
        }

        public void displaySortedByEfficiency(){
            System.out.println(
                "\n=== Vehicles Sorted " 
                + "by Efficiency (TreeSet) ==="
            );
            if(sortedByEfficiency.isEmpty()){
                System.out.println(
                    "No vehicles in fleet."
                );
                return;
            }
            
            for(Vehicle v:sortedByEfficiency){
                System.out.printf(
                    "%s (%s) - Efficiency: %.2f km/L%n",
                    v.getId(),
                    v.getModel(),
                    v.calculateFuelEfficiency()
                );
            }
        }

        public String generateReport (){
            StringBuilder str=new StringBuilder();
            str.append("=== Fleet Report ===\n");
            str.append("Total vehicles: ")
            .append(vehicles.size())
            .append("\n");
            Map<String,Integer>counts=new HashMap<>();
            double totalEf = 0.0;
            double totalMilg = 0.0;
            int effCount = 0;
            for(Vehicle v:vehicles){
                String cls = v.getClass()
                .getSimpleName();
                counts.put(
                    cls,
                    counts.getOrDefault(cls, 0) + 1
                );
                double eff = v.calculateFuelEfficiency();
                if (eff > 0) { totalEf += eff; effCount++; }
                totalMilg += v.getCurrentMileage();
            }
            for(Map.Entry<String,Integer>e:counts.entrySet()){
                str.append(e.getKey())
                .append(": ")
                .append(e.getValue())
                .append("\n");
            }
            str.append("Average efficiency: ").append(
                effCount>0 ? totalEf/effCount : 0).append("km/L\n");
            str.append("Total mileage: ").append(
                totalMilg).append("km\n");

            Vehicle fastest = getFastestVehicle();
            Vehicle slowest = getSlowestVehicle();

            if (fastest != null) {
                str.append("Fastest vehicle: ")
                .append(fastest.getId())
                .append(" (")
                .append(fastest.getModel())
                .append(") - ")
                .append(String.format("%.2f", fastest.getMaxSpeed()))
                .append(" km/h\n");
            }
            
            if (slowest != null) {
                str.append("Slowest vehicle: ")
                .append(slowest.getId())
                .append(" (")
                .append(slowest.getModel())
                .append(") - ")
                .append(String.format("%.2f", slowest.getMaxSpeed()))
                .append(" km/h\n");
            }
            
            // Most and least efficient
            Vehicle mostEff = getMostEfficientVehicle();
            Vehicle leastEff = getLeastEfficientVehicle();
            
            if (mostEff != null) {
                str.append("Most efficient: ")
                .append(mostEff.getId())
                .append(" (")
                .append(mostEff.getModel())
                .append(") - ")
                .append(String.format("%.2f", mostEff.calculateFuelEfficiency()))
                .append(" km/L\n");
            }
            
            if (leastEff != null && leastEff.calculateFuelEfficiency() > 0) {
                str.append("Least efficient: ")
                .append(leastEff.getId())
                .append(" (")
                .append(leastEff.getModel())
                .append(") - ")
                .append(String.format("%.2f", leastEff.calculateFuelEfficiency()))
                .append(" km/L\n");
            }

            return str.toString();
        }

        public List<Vehicle>getVehiclesNeedingMaintenance(){
            List<Vehicle> res = new ArrayList<>();
            for(Vehicle v:vehicles){
                if (
                    v instanceof Maintainable && 
                    ((Maintainable) v).needsMaintenance()){
                        res.add(v);
                    }
            }
            return res;
        }

        public void saveFile(String file) 
        throws IOException{
            try(
                FileWriter f=new FileWriter(file);
                PrintWriter p=new PrintWriter(f);
            ){
                for(Vehicle v:vehicles){
                    String type=v.getClass().getSimpleName();
                    StringBuilder line=new StringBuilder();
                    line.append(type).append(",")
                        .append(v.getId()).append(",")
                        .append(v.getModel()).append(",")
                        .append(v.getMaxSpeed()).append(",")
                        .append(v.getCurrentMileage());
                    if (v instanceof FuelConsumable) {
                        line.append(",FUEL,")
                            .append(((FuelConsumable) v)
                            .getFuelLevel());
                    }
                    if (v instanceof CargoCarrier) {
                        line.append(",CARGO,").append(((CargoCarrier) v)
                            .getCurrentCargo()).append(",CAP,")
                            .append(((CargoCarrier) v).getCargoCapacity());
                    }
                    if (v instanceof PassengerCarrier) {
                        line.append(",PAX,").append(
                            ((PassengerCarrier) v).getCurrentPassengers()
                            )
                            .append(",PCAP,").append(
                                ((PassengerCarrier) v).getPassengerCapacity()
                            );
                    }
                    p.println(line.toString());
                }
                System.out.println(
                    "Fleet saved successfully to " 
                    + file
                );
                System.out.println(
                    "Saved " 
                    + vehicles.size() 
                    + " vehicle(s)."
                );
            } catch (IOException e) {
                System.err.println(
                    "Error saving file: " 
                    + e.getMessage()
                );
                throw e;
            }
        }

        public void loadFile(String file)
        throws IOException {
            vehicles.clear();
            uniqueModels.clear();
            sortedByEfficiency.clear();
            try(
                FileReader f=new FileReader(file);
                BufferedReader b=new BufferedReader(f);
            ) {
                String line;
                int lineNumber = 0;
                int successCount = 0;
                int errorCount = 0;
                while((line = b.readLine())!=null){
                    lineNumber++;
                    try {
                        String[] vals = line.split(",");
                        Vehicle v = VehicleFactory.createFromCsv(vals);
                        if(v != null){
                             vehicles.add(v);
                            uniqueModels.add(v.getModel());
                            sortedByEfficiency.add(v);
                            successCount++; 
                        }
                    }catch(Exception e){
                        System.err.println(
                            "Error on line " 
                            + lineNumber + ": " 
                            + e.getMessage()
                        );
                    errorCount++;
                    }
                }
                System.out.println(
                    "Fleet loaded successfully from " 
                    + file
                );
                System.out.println(
                    "Loaded " 
                    + successCount 
                    + " vehicle(s)."
                );
                if (errorCount > 0){
                    System.out.println(
                        "Skipped " 
                        + errorCount 
                        + " invalid line(s)."
                    );
                }
            } catch (FileNotFoundException e) {
                System.err.println(
                    "File not found: " 
                    + file
                );
                throw e;
            } catch (IOException e) {
                System.err.println(
                    "Error reading file: " 
                    + e.getMessage()
                );
                throw e;
            }
        }

        public List<Vehicle>getFleets(){ 
            return new ArrayList<Vehicle>(vehicles); 
        }

        public int getVehicleCount(){
            return vehicles.size();
        }
    }
