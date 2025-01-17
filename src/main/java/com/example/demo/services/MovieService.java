package com.example.demo.services;

import com.example.demo.entities.Movie;
import com.example.demo.entities.User;
import com.example.demo.exceptions.DuplicateMovieException;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(String id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            String owner = movie.getOwner();

            User userLogin = userRepository.findByUsername(username).get(); // Lay thang login
            if (owner != null) {
                if (owner.equals(userLogin.getId())) {
                    return movieOptional;
                }
            }

            String role = userLogin.getRole();
            if (role.equals("ROLE_ADMIN")) return movieOptional;
        }

        return Optional.empty();
    }

    public Movie createMovie(Movie movie) {
        if (movieRepository.findByMovieId(movie.getMovieId()).isPresent()) {
            throw new DuplicateMovieException("Movie with movieId " + movie.getMovieId() + " already exists.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String userId = userRepository.findByUsername(username).get().getId();

        movie.setOwner(userId);

        return movieRepository.save(movie);
    }

    public Movie updateMovie(String id, Movie movie) {
        Optional<Movie> movieOptional = movieRepository.findById(id);

        if (movieOptional.isPresent()) {
            Movie existingMovie = movieOptional.get();

            if (movie.getMovieId() != null) {
                existingMovie.setMovieId(movie.getMovieId());
            }
            if (movie.getTitle() != null) {
                existingMovie.setTitle(movie.getTitle());
            }
            if (movie.getGenres() != null) {
                existingMovie.setGenres(movie.getGenres());
            }

            return movieRepository.save(existingMovie);
        } else {
            throw new RuntimeException("Movie not found with id: " + id);
        }
    }

    public void deleteMovie(String id) {
        movieRepository.deleteById(id);
    }
}
