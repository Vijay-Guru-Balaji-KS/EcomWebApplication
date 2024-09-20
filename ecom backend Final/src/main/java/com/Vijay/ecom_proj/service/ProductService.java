package com.Vijay.ecom_proj.service;

import com.Vijay.ecom_proj.model.Product;
import com.Vijay.ecom_proj.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService
{
    @Autowired
    ProductRepo repo;

    public List<Product> getAllProducts()
    {
        return repo.findAll();
    }

    public Product getOneProduct(int prodId) {
        return repo.findById(prodId).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }

    public ResponseEntity<byte[]> getImageByProdId(int productId)
    {
        Product product = repo.findById(productId).get();
        return new ResponseEntity<>(product.getImageData(), HttpStatus.FOUND);
    }

    public Product getProductById(int productId)
    {
        return repo.findById(productId).get();
    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        return repo.save(product);
    }

    public void deleteProduct(int id)
    {
        repo.deleteById(id);
    }

    public List<Product> searchProducts(String keywords)
    {
        return repo.searchProducts(keywords);
    }
}
