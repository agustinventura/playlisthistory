package dev.agustinventura.playlisthistory.adapter.out.rest;

import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import dev.agustinventura.playlisthistory.application.port.out.LoadLastPlayedPort;
import dev.agustinventura.playlisthistory.application.port.out.SaveLastPlayedPort;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestSongsAdapter implements LoadLastPlayedPort, SaveLastPlayedPort {

  private final Map<String, String> headers;

  private final SongsLocalRepository songsLocalRepository;

  public RestSongsAdapter(Map<String, String> headers, SongsLocalRepository songsLocalRepository) {
    this.headers = headers;
    this.songsLocalRepository = songsLocalRepository;
  }

  @Override
  public List<PlayListHistoryItem> loadLastPlayedSongs() {
    return songsLocalRepository.getAll();
  }

  @Override
  public Optional<PlayListHistoryItem> loadPlayedSong(String id) {
    return songsLocalRepository.getWithId(id);
  }

  @Override
  public PlayListHistoryItem save(PlayListHistoryItem item) {
    return songsLocalRepository.post(headers, item);
  }
}
