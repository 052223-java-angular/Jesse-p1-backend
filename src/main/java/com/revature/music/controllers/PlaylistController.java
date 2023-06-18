package com.revature.music.controllers;

import com.revature.music.dtos.requests.*;
import com.revature.music.entities.Playlist;
import com.revature.music.repositories.PlaylistRepository;
import com.revature.music.services.JwtTokenService;
import com.revature.music.services.PlaylistService;
import com.revature.music.services.StringValidationService;
import com.revature.music.utils.InvalidTokenException;
import com.revature.music.utils.ResourceConflictException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/playlist")
public class PlaylistController {
  private final JwtTokenService tokenService;
  private final PlaylistService playlistService;
  private final StringValidationService stringValidationService;
  private final PlaylistRepository playlistRepository;

  /**
   * Creates a new playlist where users can add songs into
   *
   * @param req- token, title, description
   * @return - new play list for the user
   */
  @PostMapping("/create")
  public ResponseEntity<?> createPlayList(@RequestBody NewPlaylistRequest req) {
    //validation for title cant be empty
    if (stringValidationService.isBlank(req.getTitle())) {
      throw new ResourceConflictException("Title can't be blank!");
    }

    //Validation for title for certain number of characters
    if (!stringValidationService.checkLengthMin(req.getTitle(), 1)) {
      throw new ResourceConflictException("Title can't be blank!");
    }

    String userId = tokenService.extractUserId(req.getToken());
    // validate token if token is valid
    if (userId == null || userId.isEmpty()) {
      throw new InvalidTokenException("Token is not valid!");
    }

    playlistService.createPlaylist(req, userId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * User able to successfully delete a playlist
   *
   * @param req - token, playlist id
   * @return - successful deletion of a playlist
   */

  @DeleteMapping("/delete")
  public ResponseEntity<?> deletePlaylist(@RequestBody DeletePlaylistRequest req) {
    String userId = tokenService.extractUserId(req.getToken());
    // validate token if token is valid
    if (userId == null || userId.isEmpty()) {
      throw new InvalidTokenException("Token is not valid!");
    }

    //validate the playlist exists

    playlistService.deletePlaylist(req);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Gets all playlists in the database
   *
   * @return All the playlists created by a user
   */
  @GetMapping("/all")
  public ResponseEntity<List<Playlist>> getAllPlaylists(@RequestBody NewGetAllPlaylistsRequest req) {
    String userId = tokenService.extractUserId(req.getToken());

    // validate token if token is valid
    if (userId == null || userId.isEmpty()) {
      throw new InvalidTokenException("Token is not valid!");
    }

    return ResponseEntity.status(HttpStatus.OK).body(playlistService.getAllPlaylists(userId));
  }

  /**
   * Gets a playlist specified by thr user
   * @param playlistId
   * @return
   */
  @GetMapping("/get/{playlistId}")
  public ResponseEntity<Playlist> getPlaylistById(@PathVariable String playlistId) {


    return ResponseEntity.status(HttpStatus.OK).body(playlistService.findById(playlistId));
  }




 // Update an existing playlist Will override the old one to update...Not sure if needed
  @PatchMapping("/update/{playlistId}")
  public ResponseEntity<Playlist> updatePlaylist(@PathVariable String playlistId, @RequestBody NewPlaylistRequest req) {

    String userId = tokenService.extractUserId(req.getToken());
    // validate token if token is valid
    if (userId == null || userId.isEmpty()) {
      throw new InvalidTokenException("Token is not valid!");
    }
    return ResponseEntity.status(HttpStatus.OK).body(playlistService.updatePlaylist(playlistId, req, userId));
  }

  /**
   * Adds a song to a playlist
   * @param req
   * @return
   */
  @PostMapping("/add/song")
  public ResponseEntity<Playlist>addSongToPlaylist(@RequestBody AddSongToPlaylist req)
  {
    return ResponseEntity.status(HttpStatus.OK).body(playlistService.addSongToPlaylist(req));
  }

  /**
   * Deletes a song from a playlist
   * @param req - song id and playlist id
   * @return - success or an error
   */
  @DeleteMapping("/delete/song")
  public ResponseEntity<?>deleteSongFromPlaylist(@RequestBody DeleteSongFromPlaylist req)
  {
    playlistService.deleteSongFromPlaylist(req);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }


}
