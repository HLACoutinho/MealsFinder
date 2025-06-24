package br.edu.ufscar.backend.mealsfinder.models;

import br.edu.ufscar.backend.mealsfinder.models.enums.EstablishmentTagsEnum;
import br.edu.ufscar.backend.mealsfinder.models.enums.FoodTypesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "reviews")
@DiscriminatorValue("REVIEW")
@Getter
@Setter
@NoArgsConstructor
public class Review extends Post {

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private boolean isDelivery;

    @Column
    private Double establishmentRating;

    @Column
    private Double serviceRating;

    @Column
    private Double deliveryRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_post_id")
    private Post reviewedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id", nullable = false)
    private Establishment establishment;

    public Review(User creator, String text, String description, Double price, Double rating, boolean isDelivery) {
        super(creator, text, description);
        this.price = price;
        this.rating = rating;
        this.isDelivery = isDelivery;
        this.establishment = establishment;
    }

    @Override
    public String getContentType() {
        return "REVIEW";
    }

    public boolean isValidRating() {
        return rating != null && rating >= 0.0 && rating <= 5.0;
    }

    public boolean hasDetailedRatings() {
        return establishmentRating != null || serviceRating != null || deliveryRating != null;
    }

    public double getOverallDetailedRating() {
        double sum = 0.0;
        int count = 0;

        if (establishmentRating != null) {
            sum += establishmentRating;
            count++;
        }
        if (serviceRating != null) {
            sum += serviceRating;
            count++;
        }
        if (deliveryRating != null && isDelivery) {
            sum += deliveryRating;
            count++;
        }

        return count > 0 ? sum / count : 0.0;
    }
}
