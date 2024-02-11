package dev.agustinventura.playlisthistory.adapter.out.rest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import dev.agustinventura.playlisthistory.PlayListHistoryItemsMother;
import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

class RestSongsRepositoryTest {

  private RestSongsAdapter restSongsAdapter;

  private SongsLocalRepository songsLocalRepository;

  private final Map<String, String> headers = Map.of(
      HttpHeaders.CONTENT_TYPE, APPLICATION_JSON.getType() + "/" + APPLICATION_JSON.getSubtype(),
      HttpHeaders.AUTHORIZATION, "Bearer test"
  );

  @BeforeEach
  void setUp() {
    songsLocalRepository = mock(SongsLocalRepository.class);
    restSongsAdapter = new RestSongsAdapter(headers, songsLocalRepository);
  }

  @Test
  void givenNoIdShouldInvokeLoadLastPlayedSongs() {
    restSongsAdapter.loadLastPlayedSongs();

    verify(songsLocalRepository, times(1)).getAll();
  }

  @Test
  void givenAnIdShouldInvokeLoadLastPlayedSong() {
    String songId = "testSongId";

    restSongsAdapter.loadPlayedSong(songId);

    verify(songsLocalRepository, times(1)).getWithId(songId);
  }

  @Test
  void givenAPlaylistHistoryItemShouldInvokeSaveWithHeaders() {
    PlayListHistoryItem playedSong = PlayListHistoryItemsMother.getRecentlyPlayed();

    restSongsAdapter.save(playedSong);

    verify(songsLocalRepository, times(1)).post(headers, playedSong);
  }
}