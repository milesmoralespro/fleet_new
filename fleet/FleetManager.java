    package fleet;

    import vehicles.*;
    import interfaces.*;
    import exceptions.*;

    import java.io.*;
    import java.util.*;
    public class FleetManager {
        private List<Vehicle>
                    vehicles=new ArrayList<>();
        
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
        }

        public void removeVehicle(String id) 
        throws InvalidOperationException{
            boolean removed=vehicles.removeIf(
                v -> v.getId().equals(id));
            if (!removed) {
                throw new InvalidOperationException("Not found: " + id);
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
            for (Vehicle v:vehicles){
                if (v instanceof Maintainable){
                    Maintainable m = (Maintainable) v;
                    if(m.needsMaintenance()){
                        m.performMaintenance();
                    }
                }
            }
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
            str.append("Average efficiency: ").append(effCount>0 ? totalEf/effCount : 0).append("\n");
            str.append("Total mileage: ").append(totalMilg).append("\n");
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
            FileWriter f=new FileWriter(file);
            PrintWriter p=new PrintWriter(f);
            try {
                for(Vehicle v:vehicles){
                    String type=v.getClass().getSimpleName();
                    StringBuilder line=new StringBuilder();
                    line.append(type).append(",").append(v.getId()).append(",").append(v.getModel())
                            .append(",").append(v.getMaxSpeed()).append(",").append(v.getCurrentMileage());
                    if (v instanceof FuelConsumable) {
                        line.append(",FUEL,").append(((FuelConsumable) v).getFuelLevel());
                    }
                    if (v instanceof CargoCarrier) {
                        line.append(",CARGO,").append(((CargoCarrier) v).getCurrentCargo())
                                .append(",CAP,").append(((CargoCarrier) v).getCargoCapacity());
                    }
                    if (v instanceof PassengerCarrier) {
                        line.append(",PAX,").append(((PassengerCarrier) v).getCurrentPassengers())
                                .append(",PCAP,").append(((PassengerCarrier) v).getPassengerCapacity());
                    }
                    p.println(line.toString());
                }
            } finally {
                p.close();
                f.close();
            }
        }

        public void loadFile(String file)
        throws IOException {
            vehicles.clear();
            FileReader f=new FileReader(file);
            BufferedReader b=new BufferedReader(f);
            try {
                String line;
                while((line = b.readLine())!=null){
                    String[] vals = line.split(",");
                    try {
                        Vehicle v = VehicleFactory.createFromCsv(vals);
                        if(v != null){
                            vehicles.add(v); 
                        }
                    }catch(Exception e){
                        System.out.println(
                            "Malformed line:" +line
                        );
                    }
                }
            } finally {
                b.close();
                f.close();
            }
        }

        public List<Vehicle>getFleets(){ 
            return new ArrayList<Vehicle>(vehicles); 
        }
    }
