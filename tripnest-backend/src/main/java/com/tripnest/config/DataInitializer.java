package com.tripnest.config;

import com.tripnest.model.Destination;
import com.tripnest.model.User;
import com.tripnest.repository.DestinationRepository;
import com.tripnest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize Default Admin User
        User existingAdmin = userRepository.findByEmail("admin@tripnest.com");
        if (existingAdmin == null) {
            User admin = new User();
            admin.setName("Platform Administrator");
            admin.setEmail("admin@tripnest.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setAge(30);
            admin.setRole("ROLE_ADMIN");
            admin.setBio("Lead System Administrator for TripNest Travel Platform");
            admin.setPhoneNumber("+91 9876543210");
            admin.setTravelPreferences("Heritage, Hill Stations, Coastal Escapes");
            admin.setFavoriteDestinations("Goa, Manali, Ladakh");
            userRepository.save(admin);
            System.out.println("✅ Initialized Administrator account: admin@tripnest.com / Admin@123");
        } else {
            existingAdmin.setRole("ROLE_ADMIN");
            userRepository.save(existingAdmin);
        }

        // Grant Admin to sakanasri07@gmail.com if present
        User sakiUser = userRepository.findByEmail("sakanasri07@gmail.com");
        if (sakiUser != null) {
            sakiUser.setRole("ROLE_ADMIN");
            userRepository.save(sakiUser);
        }

        // Initialize Destinations
        if (destinationRepository.count() == 0) {
            Destination d1 = new Destination(
                    null,
                    "Goa",
                    "India",
                    "Famous for pristine sandy beaches, vibrant nightlife, water sports, and Portuguese colonial architecture.",
                    "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=600",
                    "November – February",
                    "₹15,000 – ₹35,000",
                    "Beach & Nightlife"
            );

            Destination d2 = new Destination(
                    null,
                    "Ooty",
                    "India",
                    "Known as the Queen of Hill Stations, featuring serene tea gardens, Nilgiri Toy Train, and lush botanical gardens.",
                    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=600",
                    "October – June",
                    "₹10,000 – ₹25,000",
                    "Hill Station & Nature"
            );

            Destination d3 = new Destination(
                    null,
                    "Manali",
                    "India",
                    "High-altitude Himalayan resort town known for snow-capped mountain vistas, paragliding, and Solang Valley adventures.",
                    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600",
                    "October – June",
                    "₹20,000 – ₹45,000",
                    "Adventure & Mountains"
            );

            Destination d4 = new Destination(
                    null,
                    "Kerala Backwaters",
                    "India",
                    "Experience tranquil houseboat cruises through palm-fringed backwaters, spice plantations, and Ayurvedic wellness.",
                    "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?w=600",
                    "September – March",
                    "₹18,000 – ₹40,000",
                    "Relaxation & Culture"
            );

            Destination d5 = new Destination(
                    null,
                    "Jaipur",
                    "India",
                    "The iconic Pink City featuring majestic palaces like Hawa Mahal, Amer Fort, vibrant bazaars, and royal heritage.",
                    "https://images.unsplash.com/photo-1477587458883-47145ed94245?w=600",
                    "October – March",
                    "₹12,000 – ₹30,000",
                    "Heritage & Culture"
            );

            Destination d6 = new Destination(
                    null,
                    "Ladakh",
                    "India",
                    "Breathtaking high-altitude landscapes, crystal-clear Pangong Lake, Buddhist monasteries, and thrilling mountain passes.",
                    "https://images.unsplash.com/photo-1581793745862-99fde7fa73d2?w=600",
                    "May – September",
                    "₹25,000 – ₹55,000",
                    "Adventure & Mountains"
            );

            destinationRepository.saveAll(Arrays.asList(d1, d2, d3, d4, d5, d6));
            System.out.println("✅ Initialized sample destinations in database.");
        }
    }
}
