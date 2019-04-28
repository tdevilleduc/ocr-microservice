package com.ecommerce.microcommerce.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exceptions.ProduitIntrouvableException;
import com.ecommerce.microcommerce.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @GetMapping(value = "/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) throws ProduitIntrouvableException {
        Product product = productDao.findById(id);

        if (product == null) {
            throw new ProduitIntrouvableException("Le produit avec l'id "+id+" n'existe pas");
        }

        return product;
    }

    @GetMapping(value = "/Produits")
    public List<Product> listeProduits() {
        return productDao.findAll();
    }

    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@RequestBody Product product) {

        Product productAjoute = productDao.save(product);

        if (productAjoute == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAjoute.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "test/produits/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
        return productDao.chercherUnProduitCher(prixLimit);
    }

    @DeleteMapping (value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
        Product product = productDao.findById(id);
        productDao.delete(product);
    }

    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {
        productDao.save(product);
    }

}