package br.edu.ufscar.backend.mealsfinder.models;

import br.edu.ufscar.backend.mealsfinder.models.enums.FoodTypesEnum;
// import br.edu.ufscar.backend.mealsfinder.framework.retentions.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
@DiscriminatorValue("CLIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @ElementCollection(targetClass = FoodTypesEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "client_food_likes", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "food_type")
    private Set<FoodTypesEnum> foodLikes = new HashSet<>();

    @ElementCollection(targetClass = FoodTypesEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "client_food_dislikes", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "food_type")
    private Set<FoodTypesEnum> foodDislikes = new HashSet<>();

    public Client(String email, String username, String password) {
        super(email, username, password);
    }

    @Override
    public String getUserType() {
        return "CLIENT";
    }

    public boolean addFoodLike(FoodTypesEnum foodType) {
        this.foodDislikes.remove(foodType);
        return this.foodLikes.add(foodType);
    }

    public boolean removeFoodLike(FoodTypesEnum foodType) {
        return this.foodLikes.remove(foodType);
    }

    public boolean addFoodDislike(FoodTypesEnum foodType) {
        this.foodLikes.remove(foodType);
        return this.foodDislikes.add(foodType);
    }

    public boolean removeFoodDislike(FoodTypesEnum foodType) {
        return this.foodDislikes.remove(foodType);
    }

    public boolean likesFoodType(FoodTypesEnum foodType) {
        return this.foodLikes.contains(foodType);
    }

    public boolean dislikesFoodType(FoodTypesEnum foodType) {
        return this.foodDislikes.contains(foodType);
    }
}
