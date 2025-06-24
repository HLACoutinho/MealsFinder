package br.edu.ufscar.backend.mealsfinder.repositories;

import br.edu.ufscar.backend.mealsfinder.models.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, UUID> {

    Optional<Establishment> findByCnpj(String cnpj);

    List<Establishment> findByUsernameContainingIgnoreCase(String username);

    @Query("SELECT e FROM Establishment e WHERE e.address.city = :city")
    List<Establishment> findByCity(@Param("city") String city);

    List<Establishment> findByIsDeliveryTrue();

    List<Establishment> findByIsInPersonTrue();
}