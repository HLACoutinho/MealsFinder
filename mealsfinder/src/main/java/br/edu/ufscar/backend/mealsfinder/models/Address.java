package br.edu.ufscar.backend.mealsfinder.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Column(name = "cep")
    private String cep;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "street")
    private String street;
    @Column(name = "number")
    private String number;
    @Column(name = "neighborhood")
    private String neighborhood;
    @Column(name = "country")
    private String country;
}
