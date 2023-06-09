package com.revature.music.controllers;

import com.revature.music.dtos.requests.NewArtistRequest;
import com.revature.music.services.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artist")
@AllArgsConstructor
public class ArtistController {

    private ArtistService artistService;


    /**
     * Probably have to change once front end is built
     * Creates an artist
     * @param req- artist name
     * @return - 201 or error
     */
    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?>createArtist(@RequestBody NewArtistRequest req)
    {
        artistService.createSong(req.getId(),req.getName(), req.getGenreName());
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}
