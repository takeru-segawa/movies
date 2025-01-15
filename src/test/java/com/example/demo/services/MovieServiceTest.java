package com.example.demo.services;

import com.example.demo.entities.Movie;
import com.example.demo.exceptions.DuplicateMovieException;
import com.example.demo.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        movie = new Movie();
        movie.setId("1");
        movie.setMovieId(1);
        movie.setTitle("Test Movie");
        movie.setGenres("Drama");
    }

    @Test
    public void testGetAllMovies() {
        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie));

        assertEquals(1, movieService.getAllMovies().size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testGetMovieById() {
        when(movieRepository.findById("1")).thenReturn(Optional.of(movie));

        Optional<Movie> foundMovie = movieService.getMovieById("1");
        assertTrue(foundMovie.isPresent());
        assertEquals(movie.getTitle(), foundMovie.get().getTitle());
        verify(movieRepository, times(1)).findById("1");
    }

    @Test
    public void testCreateMovie() {
        when(movieRepository.findByMovieId(1)).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie createdMovie = movieService.createMovie(movie);
        assertNotNull(createdMovie);
        assertEquals(movie.getTitle(), createdMovie.getTitle());
        verify(movieRepository, times(1)).findByMovieId(1);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testCreateMovieThrowsDuplicateException() {
        when(movieRepository.findByMovieId(1)).thenReturn(Optional.of(movie));

        DuplicateMovieException exception = assertThrows(DuplicateMovieException.class, () -> {
            movieService.createMovie(movie);
        });

        assertEquals("Movie with movieId 1 already exists.", exception.getMessage());
        verify(movieRepository, times(1)).findByMovieId(1);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    public void testUpdateMovie() {
        when(movieRepository.findById("1")).thenReturn(Optional.of(movie));
        Movie updatedMovie = new Movie();
        updatedMovie.setMovieId(1);
        updatedMovie.setTitle("Updated Movie");
        updatedMovie.setGenres("Action");

        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);

        Movie result = movieService.updateMovie("1", updatedMovie);
        assertNotNull(result);
        assertEquals("Updated Movie", result.getTitle());
        verify(movieRepository, times(1)).findById("1");
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void testUpdateMovie2() {
        when(movieRepository.findById("1")).thenReturn(Optional.of(movie));
        Movie updatedMovie = new Movie();

//        updatedMovie.setMovieId(movie.getMovieId());
//        updatedMovie.setTitle(movie.getTitle());
//        updatedMovie.setGenres(movie.getGenres());

        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie result = movieService.updateMovie("1", updatedMovie);
        assertNotNull(result);
//        assertEquals(null, result.getTitle());

        assertEquals("Test Movie", result.getTitle());
        assertEquals(1, result.getMovieId());
        assertEquals("Drama", result.getGenres());
    }

    @Test
    public void testUpdateMovieNotFound() {
        when(movieRepository.findById("2")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieService.updateMovie("2", movie);
        });

        assertEquals("Movie not found with id: 2", exception.getMessage());
        verify(movieRepository, times(1)).findById("2");
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    public void testDeleteMovie() {
        doNothing().when(movieRepository).deleteById("1");

        movieService.deleteMovie("1");
        verify(movieRepository, times(1)).deleteById("1");
    }
}
