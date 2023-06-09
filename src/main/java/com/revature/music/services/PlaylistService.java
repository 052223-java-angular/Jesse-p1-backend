package com.revature.music.services;

import com.revature.music.dtos.requests.AddSongToPlaylist;
import com.revature.music.dtos.requests.NewPlaylistRequest;
import com.revature.music.entities.Playlist;
import com.revature.music.entities.Song;
import com.revature.music.entities.User;
import com.revature.music.repositories.PlaylistRepository;
import com.revature.music.utils.PlaylistNotFoundException;
import com.revature.music.utils.SongNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class PlaylistService {

    private PlaylistRepository playlistRepository;
    private final UserService userService;
    private final SongService songService;

    /**
     * A user can create a playlist
     * @param req title, description, token
     * @param userId - id of the user creating the playlist
     * @return - a new playlist
     */
    public Playlist createPlaylist(NewPlaylistRequest req, String userId)
    {
        User existingUser = userService.findUserById(userId);

        Playlist newPlaylist = new Playlist(req.getTitle(), req.getDescription(), existingUser);

        return playlistRepository.save(newPlaylist);
    }


    /**
     * IMPLEMENTED
     * Delete a specific play list
     * @param req - playlist id
     */
    public void deletePlaylist(String playlistId) {
        //User existingUser = userService.findUserById(userId).get();
        playlistRepository.deleteById(playlistId);
    }

    /**
     * IMPLEMENTED
     * Gets playlists associated with userid
     * @param userId - the userid
     * @return - All the playlist associated with the userid
     */
    public List<Playlist> getAllPlaylists(String userId)
    {
        //validate if user created a playlist

        return playlistRepository.findByUserId(userId);

    }


    /*
     * Finds a specific playlist by user
     */
    public Playlist findById(String playlistId)
    {
      return playlistRepository.findById(playlistId)
        .orElseThrow(() -> new PlaylistNotFoundException("PlayList Not found"));
    }

  /**
   * Updates an existing playlist
   * @param playlistId - the playlist id that needs to modified
   * @param playlist - the playlist object
   * @return - an exception if the playlist isn't found or an updated playlist
   */
    public Playlist updatePlaylist (String playlistId, NewPlaylistRequest req , String userId)
    {

      if (!playlistRepository.existsById(playlistId)) {
         throw new PlaylistNotFoundException("No playlist found");
      }

      User user = userService.findUserById(userId);

      Playlist playlist = playlistRepository.findById(playlistId)
        .orElseThrow(() -> new PlaylistNotFoundException("No playlist found")); // Fetch the existing playlist from the database

      playlist.setTitle(req.getTitle()); // Update the title
      playlist.setDescription(req.getDescription()); // Update the description
      playlist.setUser(user); // Update the user if necessary

      return playlistRepository.save(playlist);
    }

  public Playlist addSongToPlaylist(AddSongToPlaylist req)
  {
    Song song = songService.getSongById(req.getSongId());
   if (song == null)
    {
      throw new SongNotFoundException("Song does not exist in the playlist!");
    }

    Optional<Playlist> playlist = playlistRepository.findById(req.getPlaylistId());
    if (playlist.isEmpty())
    {
      throw new PlaylistNotFoundException("Playlist does not exist!");
    }

    Set<Song> songs = playlist.get().getSongs();
    songs.add(song);

    playlist.get().setSongs(songs);

      return playlistRepository.save(playlist.get());

  }
  /**
   * Deletes a song from a playlist
   * @param req - song id and playlist id
   * @return - success or an error
   */
  public void deleteSongFromPlaylist(String songId, String playlistId)
  {
    Song song = songService.getSongById(songId);
     if (song == null)
     {
        throw new SongNotFoundException("Song does not exist in the playlist!");
     }

    Optional<Playlist> playlist = playlistRepository.findById(playlistId);
     if (playlist.isEmpty())
     {
       throw new PlaylistNotFoundException("Playlist does not exist!");
     }
    Set<Song> songs = playlist.get().getSongs();

    songs.remove(song);

    playlist.get().setSongs(songs);

    playlistRepository.save(playlist.get());
  }
}
