package com.quad.project.uber.uberApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_rating_rider", columnList = "raider_id"),
        @Index(name = "idx_rating_driver", columnList = "driver_id")
})
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ride ride;

    @ManyToOne
    private Rider rider;
    // one rider can have many rating, and many rating can belong to One rider

    @ManyToOne
    private Driver driver;

    private Integer driverRating;
    private Integer riderRating;
}
// NOTE about creating Rating
// M1: create a table to store all the ratings.
// 10 ratingCount -> 4.0
// new rating 4.6
// updated rating = (4.0 * 10) + 4.6 => 44.6/11 = 4.05

// M2: use totalRatingCount and avgRating fields for the USER.

// M3: ride object may include driver And rider rating