package dev.agustinventura.playlisthistory.adapter.out.rest;

import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import dev.agustinventura.playlisthistory.application.port.out.LoadLastPlayedPort;
import dev.agustinventura.playlisthistory.application.port.out.SaveLastPlayedPort;
import java.util.List;
import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class SongsLocalRepository implements LoadLastPlayedPort, SaveLastPlayedPort {

  private static final String LAST_PLAYED_SONGS_COLLECTION = "/items";

  private final RestClient restClient;

  public SongsLocalRepository(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public List<PlayListHistoryItem> loadLastPlayedSongs() {
    return restClient
        .get()
        .uri(LAST_PLAYED_SONGS_COLLECTION)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(new ParameterizedTypeReference<List<PlayListHistoryItem>>() {});
  }

  @Override
  public Optional<PlayListHistoryItem> loadPlayedSong(String id) {
    try {
      PlayListHistoryItem playedSong = restClient
          .get()
          .uri(LAST_PLAYED_SONGS_COLLECTION + "/{id}", id)
          .accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
            throw new IllegalArgumentException();
          })
          .body(PlayListHistoryItem.class);
        return Optional.ofNullable(playedSong);
    } catch (IllegalArgumentException iae) {
      return Optional.empty();
    }
  }

  @Override
  public PlayListHistoryItem save(PlayListHistoryItem item) {
    if (item == null) {
      throw new IllegalArgumentException("Stored song can't be null");
    }
    return restClient
        .post()
        .uri(LAST_PLAYED_SONGS_COLLECTION)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(item)
        .retrieve()
        .body(PlayListHistoryItem.class);
  }
}
