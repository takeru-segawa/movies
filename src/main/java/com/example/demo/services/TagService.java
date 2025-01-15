package com.example.demo.services;

import com.example.demo.entities.Tag;
import com.example.demo.repositories.TagRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findById(String id) {
        Optional<Tag> tag = tagRepository.findById(id);

        if (!tag.isPresent()) {
            throw new RuntimeException("Tag not found");
        }

        return tag.get();
    }

    public Tag save(Tag tag) {
        try {
            return tagRepository.save(tag);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Data integrity violation: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new RuntimeException("Validation error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the rating: " + e.getMessage());
        }
    }

    public Tag update(String id, Tag tag) {
        Optional<Tag> existingTag = tagRepository.findById(id);
        if (!existingTag.isPresent()) {
            throw new RuntimeException("Tag not found");
        }

        Tag oldTag = existingTag.get();
        if (tag.getUserId() != null) {
            oldTag.setUserId(tag.getUserId());
        }
        if (tag.getMovieId() != null) {
            oldTag.setMovieId(tag.getMovieId());
        }
        if (tag.getTag() != null) {
            oldTag.setTag(tag.getTag());
        }
        if (tag.getTimestamp() != null) {
            oldTag.setTimestamp(tag.getTimestamp());
        }

        return tagRepository.save(oldTag);
    }

    public void delete(String id) {
        Optional<Tag> existingTag = tagRepository.findById(id);
        if (!existingTag.isPresent()) {
            throw new RuntimeException("Tag not found");
        }

        tagRepository.deleteById(id);
    }
}
