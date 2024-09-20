package com.Vijay.ecom_proj.controller;


import com.Vijay.ecom_proj.model.Product;
import com.Vijay.ecom_proj.service.ProductService;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController
{
    @Autowired
    ProductService service;

    @RequestMapping("/")
    public String greet()
    {
        return "Hello Worlds";
    }

//    @RequestMapping("/products")
//    public List<Product> getAllProducts()
//    {
//        return service.getAllProducts();
//    }


    //ResponseEntity used to send both the object and the status of the object

    @RequestMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @RequestMapping("/product/{prodId}")
    public ResponseEntity<Product> getOneItem(@PathVariable int prodId)
    {
        Product product = service.getOneProduct(prodId);
        if(product!=null)
            return new ResponseEntity<>(product,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile)
    {
        try {
            Product product1 = service.addProduct(product,imageFile);
            return new ResponseEntity<>(product,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProdId(@PathVariable int productId)
    {
        Product product = service.getProductById(productId);
        byte[] imageFile = product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product, @RequestPart MultipartFile imageFile)
    {
        Product product1 = null;
        try {
            product1 = service.updateProduct(id, product,imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(product1!=null)
            return new ResponseEntity<>("Updated",HttpStatus.OK);
        else
            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id)
    {
        Product product = service.getProductById(id);
        if(product!=null) {
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Product Not Found",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>>    searchProducts(@RequestParam String keywords)
    {
        System.out.println("Searching with "+keywords);
        List<Product> products = service.searchProducts(keywords);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
