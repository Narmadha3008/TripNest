package com.tripnest.service.ai;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KnowledgeBaseService {

    public static class DestinationInfo {
        private String name;
        private String country;
        private String climate;
        private String category;
        private List<String> interests;
        private double latitude;
        private double longitude;
        private String bestTimeToVisit;
        private String estimatedBudget;
        private String imageUrl;
        private String description;
        private List<AttractionInfo> attractions = new ArrayList<>();

        public DestinationInfo(String name, String country, String climate, String category,
                               List<String> interests, double latitude, double longitude,
                               String bestTimeToVisit, String estimatedBudget, String imageUrl, String description) {
            this.name = name;
            this.country = country;
            this.climate = climate;
            this.category = category;
            this.interests = interests;
            this.latitude = latitude;
            this.longitude = longitude;
            this.bestTimeToVisit = bestTimeToVisit;
            this.estimatedBudget = estimatedBudget;
            this.imageUrl = imageUrl;
            this.description = description;
        }

        public String getName() { return name; }
        public String getCountry() { return country; }
        public String getClimate() { return climate; }
        public String getCategory() { return category; }
        public List<String> getInterests() { return interests; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public String getBestTimeToVisit() { return bestTimeToVisit; }
        public String getEstimatedBudget() { return estimatedBudget; }
        public String getImageUrl() { return imageUrl; }
        public String getDescription() { return description; }
        public List<AttractionInfo> getAttractions() { return attractions; }
        public void addAttraction(AttractionInfo a) { this.attractions.add(a); }
    }

    public static class AttractionInfo {
        private String name;
        private String type; // Sightseeing, Adventure, Food, Relaxation, Shopping, Heritage
        private String timeSlot;
        private int durationMinutes;
        private double estimatedCostInr;
        private String notes;
        private double latitude;
        private double longitude;

        public AttractionInfo(String name, String type, String timeSlot, int durationMinutes,
                              double estimatedCostInr, String notes, double latitude, double longitude) {
            this.name = name;
            this.type = type;
            this.timeSlot = timeSlot;
            this.durationMinutes = durationMinutes;
            this.estimatedCostInr = estimatedCostInr;
            this.notes = notes;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public String getTimeSlot() { return timeSlot; }
        public int getDurationMinutes() { return durationMinutes; }
        public double getEstimatedCostInr() { return estimatedCostInr; }
        public String getNotes() { return notes; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }

    private final Map<String, DestinationInfo> destinations = new LinkedHashMap<>();
    private final Map<String, double[]> cityCoordinates = new HashMap<>();

    public KnowledgeBaseService() {
        initDestinations();
        initCoordinates();
    }

    private void initCoordinates() {
        cityCoordinates.put("goa", new double[]{15.2993, 74.1240});
        cityCoordinates.put("ooty", new double[]{11.4102, 76.6950});
        cityCoordinates.put("manali", new double[]{32.2432, 77.1892});
        cityCoordinates.put("kerala", new double[]{9.4981, 76.3388});
        cityCoordinates.put("kerala backwaters", new double[]{9.4981, 76.3388});
        cityCoordinates.put("jaipur", new double[]{26.9124, 75.7873});
        cityCoordinates.put("delhi", new double[]{28.6139, 77.2090});
        cityCoordinates.put("mumbai", new double[]{19.0760, 72.8777});
        cityCoordinates.put("bangalore", new double[]{12.9716, 77.5946});
        cityCoordinates.put("bengaluru", new double[]{12.9716, 77.5946});
        cityCoordinates.put("chennai", new double[]{13.0827, 80.2707});
        cityCoordinates.put("kolkata", new double[]{22.5726, 88.3639});
        cityCoordinates.put("hyderabad", new double[]{17.3850, 78.4867});
        cityCoordinates.put("pune", new double[]{18.5204, 73.8567});
        cityCoordinates.put("shimla", new double[]{31.1048, 77.1734});
        cityCoordinates.put("agra", new double[]{27.1767, 78.0081});
        cityCoordinates.put("varanasi", new double[]{25.3176, 82.9739});
        cityCoordinates.put("ladakh", new double[]{34.1526, 77.5771});
        cityCoordinates.put("leh", new double[]{34.1526, 77.5771});
        cityCoordinates.put("rishikesh", new double[]{30.0869, 78.2676});
        cityCoordinates.put("udaipur", new double[]{24.5854, 73.7125});
        cityCoordinates.put("mysore", new double[]{12.2958, 76.6394});
        cityCoordinates.put("mysuru", new double[]{12.2958, 76.6394});
        cityCoordinates.put("amritsar", new double[]{31.6340, 74.8723});
        cityCoordinates.put("darjeeling", new double[]{27.0410, 88.2663});
        cityCoordinates.put("munnar", new double[]{10.0889, 77.0595});
        cityCoordinates.put("pondicherry", new double[]{11.9416, 79.8083});
        cityCoordinates.put("puducherry", new double[]{11.9416, 79.8083});
        cityCoordinates.put("andaman", new double[]{11.6234, 92.7265});
        cityCoordinates.put("hampi", new double[]{15.3350, 76.4600});
        cityCoordinates.put("coorg", new double[]{12.4244, 75.7382});
        cityCoordinates.put("madurai", new double[]{9.9252, 78.1198});
        cityCoordinates.put("kochi", new double[]{9.9312, 76.2673});
    }

    private void initDestinations() {
        // Goa
        DestinationInfo goa = new DestinationInfo(
                "Goa", "India", "Coastal / Beach", "Beach & Nightlife",
                Arrays.asList("Beaches", "Nightlife", "Water Sports", "Relaxation", "Food"),
                15.2993, 74.1240, "November - February", "Rs. 15,000 - Rs. 35,000",
                "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=600",
                "Famous for pristine sandy beaches, vibrant nightlife, water sports, and Portuguese colonial architecture."
        );
        goa.addAttraction(new AttractionInfo("Calangute & Baga Beach", "Beach & Water Sports", "Morning", 180, 500, "Parasailing, jet ski, sunbathing, beach shacks", 15.5439, 73.7553));
        goa.addAttraction(new AttractionInfo("Aguada Fort & Lighthouse", "Historical Sightseeing", "Afternoon", 120, 100, "17th-century Portuguese fortress with sweeping ocean views", 15.4925, 73.7736));
        goa.addAttraction(new AttractionInfo("Basilica of Bom Jesus", "Heritage & Culture", "Morning", 90, 50, "UNESCO World Heritage site holding mortal remains of St. Francis Xavier", 15.5009, 73.9116));
        goa.addAttraction(new AttractionInfo("Anjuna Flea Market", "Shopping & Vibes", "Evening", 120, 200, "Bohemian beachside flea market with handmade crafts, spices, and music", 15.5804, 73.7428));
        goa.addAttraction(new AttractionInfo("Dudhsagar Waterfalls", "Adventure & Nature", "Morning", 240, 1200, "Majestic four-tiered waterfall on the Mandovi River, accessible via jeep safari", 15.3144, 74.3143));
        goa.addAttraction(new AttractionInfo("Panaji Latin Quarter (Fontainhas)", "Culture & Food", "Evening", 120, 300, "Colorful heritage homes, art galleries, and authentic Goan Portuguese bakeries", 15.4989, 73.8322));
        destinations.put("goa", goa);

        // Ooty
        DestinationInfo ooty = new DestinationInfo(
                "Ooty", "India", "Cool / Mountain", "Hill Station & Nature",
                Arrays.asList("Nature", "Relaxation", "Sightseeing", "Food"),
                11.4102, 76.6950, "October - June", "Rs. 10,000 - Rs. 25,000",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=600",
                "Known as the Queen of Hill Stations, featuring serene tea gardens, Nilgiri Toy Train, and lush botanical gardens."
        );
        ooty.addAttraction(new AttractionInfo("Government Botanical Garden", "Nature & Botanical", "Morning", 120, 80, "Lush 55-acre landscaped terraced gardens founded in 1848", 11.4172, 76.7112));
        ooty.addAttraction(new AttractionInfo("Ooty Lake & Boating", "Relaxation & Family", "Afternoon", 90, 250, "Scenic artificial lake with paddle boating and eucalyptus-lined paths", 11.4064, 76.6896));
        ooty.addAttraction(new AttractionInfo("Doddabetta Peak", "Mountain Viewpoint", "Morning", 120, 60, "Highest mountain peak in Nilgiri Hills at 2,637m with panoramic telescope view", 11.4011, 76.7360));
        ooty.addAttraction(new AttractionInfo("Nilgiri Mountain Railway (Toy Train)", "Heritage Ride", "Afternoon", 180, 200, "UNESCO World Heritage meter-gauge steam train traversing tunnels and bridges", 11.4070, 76.7020));
        ooty.addAttraction(new AttractionInfo("Pykara Waterfalls & Lake", "Nature & Scenic", "Morning", 150, 150, "Cascading falls and peaceful boathouse surrounded by shola forests", 11.5218, 76.5986));
        destinations.put("ooty", ooty);

        // Manali
        DestinationInfo manali = new DestinationInfo(
                "Manali", "India", "Cool / Mountain", "Adventure & Mountains",
                Arrays.asList("Adventure", "Nature", "Mountains", "Temples"),
                32.2432, 77.1892, "October - June", "Rs. 20,000 - Rs. 45,000",
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600",
                "High-altitude Himalayan resort town known for snow-capped mountain vistas, paragliding, and Solang Valley adventures."
        );
        manali.addAttraction(new AttractionInfo("Solang Valley", "Adventure Sports", "Morning", 240, 1500, "Hub for paragliding, zorbing, ATV quad biking, and skiing in winter", 32.3160, 77.1575));
        manali.addAttraction(new AttractionInfo("Hadimba Devi Temple", "Heritage & Temples", "Afternoon", 90, 50, "Pagoda-style wooden temple built in 1553 set amid towering deodar forests", 32.2483, 77.1802));
        manali.addAttraction(new AttractionInfo("Atal Tunnel & Sissu Valley", "Scenic Highway", "Morning", 240, 800, "World-record high-altitude tunnel leading to dramatic Lahaul landscapes and waterfall", 32.4172, 77.1643));
        manali.addAttraction(new AttractionInfo("Old Manali Cafes & Mall Road", "Culture & Shopping", "Evening", 120, 400, "Charming cobblestone alleyways, live acoustic cafes, and Himachali shawls", 32.2530, 77.1850));
        manali.addAttraction(new AttractionInfo("Jogini Waterfall Trek", "Nature & Trekking", "Morning", 180, 200, "Short scenic forest trail ending at picturesque multi-tiered cascades", 32.2680, 77.1950));
        destinations.put("manali", manali);

        // Kerala Backwaters
        DestinationInfo kerala = new DestinationInfo(
                "Kerala Backwaters", "India", "Tropical", "Relaxation & Culture",
                Arrays.asList("Nature", "Relaxation", "Food", "Culture"),
                9.4981, 76.3388, "September - March", "Rs. 18,000 - Rs. 40,000",
                "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?w=600",
                "Tranquil houseboat cruises through palm-fringed backwaters, spice plantations, and Ayurvedic wellness."
        );
        kerala.addAttraction(new AttractionInfo("Alleppey Houseboat Cruise", "Relaxation & Scenic", "Morning", 300, 3500, "Day cruise through palm-lined canals with traditional Keralite sadhya lunch", 9.4981, 76.3388));
        kerala.addAttraction(new AttractionInfo("Marari Beach", "Coastal & Serene", "Evening", 120, 100, "Quiet coconut-fringed white sand beach ideal for relaxation and sunset walks", 9.6015, 76.2995));
        kerala.addAttraction(new AttractionInfo("Kumarakom Bird Sanctuary", "Nature & Wildlife", "Morning", 150, 100, "Lush 14-acre sanctuary on Lake Vembanad sheltering migratory birds", 9.6200, 76.4300));
        destinations.put("kerala backwaters", kerala);
        destinations.put("kerala", kerala);

        // Jaipur
        DestinationInfo jaipur = new DestinationInfo(
                "Jaipur", "India", "Moderate", "Heritage & Culture",
                Arrays.asList("Heritage", "Culture", "Shopping", "Food", "Sightseeing"),
                26.9124, 75.7873, "October - March", "Rs. 12,000 - Rs. 30,000",
                "https://images.unsplash.com/photo-1477587458883-47145ed94245?w=600",
                "The iconic Pink City featuring majestic palaces like Hawa Mahal, Amer Fort, vibrant bazaars, and royal heritage."
        );
        jaipur.addAttraction(new AttractionInfo("Amer Fort & Palace", "Heritage & Architecture", "Morning", 210, 500, "Stunning hillside Rajput fortress with Sheesh Mahal (Mirror Palace) and courtyards", 26.9855, 75.8513));
        jaipur.addAttraction(new AttractionInfo("Hawa Mahal (Palace of Winds)", "Sightseeing & Photo", "Morning", 60, 200, "Iconic pink sandstone facade featuring 953 ornate jharokhas", 26.9239, 75.8267));
        jaipur.addAttraction(new AttractionInfo("City Palace & Jantar Mantar", "Royal Heritage & Science", "Afternoon", 150, 400, "Living royal residence alongside 18th-century UNESCO astronomical observatory", 26.9258, 75.8236));
        jaipur.addAttraction(new AttractionInfo("Johari & Bapu Bazaar", "Shopping & Handicrafts", "Evening", 150, 300, "Historic bazaars famous for precious gemstones, block prints, and juttis", 26.9200, 75.8220));
        jaipur.addAttraction(new AttractionInfo("Nahargarh Fort Sunset Viewpoint", "Scenic & Evening", "Evening", 120, 150, "Historic fort perched on Aravalli ridge overlooking illuminated Jaipur city", 26.9372, 75.8156));
        destinations.put("jaipur", jaipur);
    }

    public DestinationInfo getDestination(String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        for (Map.Entry<String, DestinationInfo> entry : destinations.entrySet()) {
            if (clean.contains(entry.getKey()) || entry.getKey().contains(clean)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public double[] getCoordinates(String placeName, double[] fallback) {
        if (placeName == null || placeName.trim().isEmpty()) return fallback;
        String clean = placeName.trim().toLowerCase();
        for (Map.Entry<String, double[]> entry : cityCoordinates.entrySet()) {
            if (clean.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return fallback;
    }

    public Map<String, DestinationInfo> getAllDestinations() {
        return destinations;
    }
}