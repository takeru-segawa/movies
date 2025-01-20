package com.example.demo.controllers;

import com.example.demo.entities.Link;
import com.example.demo.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/links")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Link> findAll() {
        return linkService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") // can delete this line
    public ResponseEntity<Link> findById(@PathVariable String id) {
        try {
            Link link = linkService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(link);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping()
    public ResponseEntity<Link> create(@RequestBody Link link) {
        try {
            Link linkSaved = linkService.save(link);
            return ResponseEntity.status(HttpStatus.CREATED).body(linkSaved);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Link> update(@PathVariable String id, @RequestBody Link link) {
        try {
            Link linkUpdated = linkService.update(id, link);
            return ResponseEntity.status(HttpStatus.OK).body(linkUpdated);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        try {
            linkService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Link deleted");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
