package com.example.demo.services;

import com.example.demo.entities.Link;
import com.example.demo.repositories.LinkRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;

    public List<Link> findAll() {
        return linkRepository.findAll();
    }

    public Link findById(String id) {
        Optional<Link> link = linkRepository.findById(id);

        if (!link.isPresent()) {
            throw new RuntimeException("Link not found");
        }

        return link.get();
    }

    public Link save(Link link) {
        try {
            return linkRepository.save(link);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Data integrity violation: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new RuntimeException("Validation error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the link: " + e.getMessage());
        }
    }

    public Link update(String id, Link link) {
        Optional<Link> linkOptional = linkRepository.findById(id);

        if (!linkOptional.isPresent()) {
            throw new RuntimeException("Link not found");
        }

        Link linkToUpdate = linkOptional.get();

        if (link.getMovieId() != null) { linkToUpdate.setMovieId(link.getMovieId()); }
        if (link.getImdbId() != null) { linkToUpdate.setImdbId(link.getImdbId()); }
        if (link.getTmdbId() != null) { linkToUpdate.setTmdbId(link.getTmdbId()); }

        return linkRepository.save(linkToUpdate);
    }

    public void delete(String id) {
        Optional<Link> linkOptional = linkRepository.findById(id);

        if (!linkOptional.isPresent()) {
            throw new RuntimeException("Link not found");
        }

        linkRepository.deleteById(id);
    }
}
