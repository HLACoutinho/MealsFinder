package br.edu.ufscar.backend.mealsfinder;

import br.edu.ufscar.backend.mealsfinder.framework.PersistenceFramework;
import br.edu.ufscar.backend.mealsfinder.models.Client;
import br.edu.ufscar.backend.mealsfinder.models.Establishment;
import br.edu.ufscar.backend.mealsfinder.models.Address;
import br.edu.ufscar.backend.mealsfinder.models.Post;
import br.edu.ufscar.backend.mealsfinder.models.enums.EstablishmentTypesEnum;
import br.edu.ufscar.backend.mealsfinder.models.enums.FoodTypesEnum;
import br.edu.ufscar.backend.mealsfinder.models.enums.StatusEnum;
import br.edu.ufscar.backend.mealsfinder.repositories.ClientRepository;
import br.edu.ufscar.backend.mealsfinder.repositories.EstablishmentRepository;
import br.edu.ufscar.backend.mealsfinder.repositories.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.*;

@SpringBootApplication
public class MealsFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealsFinderApplication.class, args);
	}

	@Bean
	@Transactional
	public CommandLineRunner databaseTest(
			EstablishmentRepository establishmentRepo,
			ClientRepository clientRepo,
			PostRepository postRepo) {
		return args -> {
			System.out.println("=== Testing Database Setup ===");

			// Test 1: Create and save a Client
			Client client = new Client("john.doe@example.com", "johndoe", "password123");
			client.setBio("Food lover and reviewer");
			client.addFoodLike(FoodTypesEnum.Pizza);
			client.addFoodLike(FoodTypesEnum.Brasileira);
			client.addFoodDislike(FoodTypesEnum.Salada);

			client = clientRepo.save(client);
			System.out.println("✅ Client created: " + client);

			// Test 2: Create and save an Establishment
			Establishment establishment = new Establishment(
					"manapoke@gmail.com",
					"manapoke",
					"password123",
					"79.536.761/0001-12",
					EstablishmentTypesEnum.Restaurante
			);
			establishment.setBio("Best poke in town!");
			establishment.setDelivery(true);
			establishment.setInPerson(true);
			establishment.setStatus(StatusEnum.APPROVED);

			// Set address
			Address address = new Address(
					"13560-000",
					"São Carlos",
					"SP",
					"Rua das Flores",
					"123",
					"Centro",
					"Brazil"
			);
			establishment.setAddress(address);

			establishment = establishmentRepo.save(establishment);
			System.out.println("✅ Establishment created: " + establishment);

			// Test 3: Create a Post
			Post post = new Post(establishment, "Check out our new poke bowl!",
					"Fresh salmon poke with avocado and rice. Delicious!");
			post.getFoodTags().add(FoodTypesEnum.Brasileira);
			post.getFoodTags().add(FoodTypesEnum.Lanche);

			post = postRepo.save(post);
			System.out.println("✅ Post created: " + post);

			// Test 4: Test relationships
			client.followUser(establishment);
			client.saveContent(post);
			client.saveContent(post);

			clientRepo.save(client);
			System.out.println("✅ Client now follows establishment and liked/saved the post");

			// Test 5: Query data
			System.out.println("\n=== Database Queries ===");

			System.out.println("All Clients:");
			clientRepo.findAll().forEach(c ->
					System.out.println("  - " + c.getUsername() + " (" + c.getEmail() + ")"));

			System.out.println("All Establishments:");
			establishmentRepo.findAll().forEach(e ->
					System.out.println("  - " + e.getUsername() + " (" + e.getCnpj() + ")"));

			System.out.println("All Posts:");
			postRepo.findAll().forEach(p ->
					System.out.println("  - " + p.getText() + " by " + p.getCreator().getUsername()));

			System.out.println("\n=== Database Test Completed Successfully! ===");
		};
	}
}
