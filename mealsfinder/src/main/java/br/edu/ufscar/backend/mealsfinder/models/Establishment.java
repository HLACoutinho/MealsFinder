package br.edu.ufscar.backend.mealsfinder.models;

import br.edu.ufscar.backend.mealsfinder.models.enums.EstablishmentTypesEnum;
import br.edu.ufscar.backend.mealsfinder.models.enums.ImageType;
import br.edu.ufscar.backend.mealsfinder.models.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "establishments")
@DiscriminatorValue("ESTABLISHMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Establishment extends User {

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstablishmentTypesEnum establishmentType;

    @Column(name = "is_delivery", nullable = false)
    private boolean isDelivery = false;

    @Column(name = "is_in_person", nullable = false)
    private boolean isInPerson = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status = StatusEnum.PENDING;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private int rejections = 0;

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, orphanRemoval = true) // <-- Updated mappedBy
    private Set<Review> receivedReviews = new HashSet<>();

    public Establishment(String email, String username, String password, String cnpj, EstablishmentTypesEnum establishmentType) {
        super(email, username, password);
        this.cnpj = cnpj;
        this.establishmentType = establishmentType;
    }

    @Override
    public String getUserType() {
        return "ESTABLISHMENT";
    }

    public Set<Image> getMenuImages() {
        return this.images.stream()
                .filter(image -> image.getType() == ImageType.MENU)
                .collect(Collectors.toSet());
    }

    public Set<Image> getEstablishmentImages() {
        return this.images.stream()
                .filter(image -> image.getType() == ImageType.ESTABLISHMENT_PICTURE)
                .collect(Collectors.toSet());
    }

    public boolean addImage(Image image) {
        image.setEstablishment(this);
        return this.images.add(image);
    }

    public boolean removeImage(Image image) {
        boolean removed = this.images.remove(image);
        if (removed) {
            image.setEstablishment(null);
        }
        return removed;
    }

    public double getAverageRating() {
        return this.receivedReviews.stream()
                .mapToDouble(Review::getOverallDetailedRating)
                .average()
                .orElse(0.0);
    }

    public int getReviewCount() {
        return this.receivedReviews.size();
    }

    public boolean isApproved() {
        return this.status == StatusEnum.APPROVED;
    }

    public boolean isPending() {
        return this.status == StatusEnum.PENDING;
    }

    public boolean isRejected() {
        return this.status == StatusEnum.REJECTED;
    }

    public void approve() {
        this.status = StatusEnum.APPROVED;
    }

    public void reject() {
        this.status = StatusEnum.REJECTED;
        this.rejections++;
    }

    public void resetToPending() {
        this.status = StatusEnum.PENDING;
    }

    public boolean supportsDelivery() {
        return this.isDelivery;
    }

    public boolean supportsInPerson() {
        return this.isInPerson;
    }
}
