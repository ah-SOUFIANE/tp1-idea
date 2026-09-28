package com.example;

import com.example.model.Produit;
import org.h2.tools.Server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

public class App {

    public static void main(String[] args) {

        // Démarrage de la console Web H2 (http://localhost:8082)
        try {
            Server.createWebServer("-web", "-webPort", "8082").start();
            System.out.println("Console H2 disponible sur : http://localhost:8082");
        } catch (Exception e) {
            System.out.println("Erreur lors du démarrage de la console H2");
            e.printStackTrace();
        }

        // 1. Création de l'EntityManagerFactory (une seule fois pour toute l'appli)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate-demo-unit");
        EntityManager em = emf.createEntityManager();

        // 2. Insertion de quelques produits dans une transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Produit p1 = new Produit("Laptop", new BigDecimal("8500.00"));
        Produit p2 = new Produit("Smartphone", new BigDecimal("3200.00"));
        Produit p3 = new Produit("Tablette", new BigDecimal("2100.00"));

        em.persist(p1);
        em.persist(p2);
        em.persist(p3);

        tx.commit();

        // 3. Lecture de tous les produits
        TypedQuery<Produit> query = em.createQuery("SELECT p FROM Produit p", Produit.class);
        List<Produit> produits = query.getResultList();

        System.out.println("\n--- Liste des produits ---");
        for (Produit p : produits) {
            System.out.println(p);
        }

        // 4. Recherche d'un produit précis par son ID
        Produit produitTrouve = em.find(Produit.class, 2L);
        System.out.println("\n--- Produit avec ID=2 ---");
        System.out.println(produitTrouve);

        // 5. Fermeture des ressources
        em.close();
        emf.close();

        // On garde le processus actif pour pouvoir consulter la console H2
        System.out.println("\nAppuyez sur Ctrl+C pour arrêter l'application (console H2 active).");
        try {
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {
        }
    }
}
