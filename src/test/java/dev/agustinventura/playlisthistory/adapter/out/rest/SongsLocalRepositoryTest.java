package dev.agustinventura.playlisthistory.adapter.out.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.agustinventura.playlisthistory.PlayListHistoryItemsMother;
import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

class SongsLocalRepositoryTest {

  private RestClient restClient;
  private SongsLocalRepository songsLocalRepository;

  @BeforeEach
  void setUp() {
    restClient = mock(RestClient.class);
    songsLocalRepository = new SongsLocalRepository(restClient);
  }

  @Test
  void givenNoLastPlayedSongsShouldReturnEmptyList() {
    List<PlayListHistoryItem> playListHistoryItems = PlayListHistoryItemsMother.getEmptyRecentlyPlayed();
    setUpMockGetAllRequest(playListHistoryItems);

    List<PlayListHistoryItem> lastPlayedSongs = songsLocalRepository.loadLastPlayedSongs();

    assertThat(lastPlayedSongs).isEmpty();
  }

  @Test
  void givenMoreThanOneLastPlayedSongShouldReturnSongsInDecreasingLastPlayedAtOrder() {
    List<PlayListHistoryItem> playListHistoryItems = PlayListHistoryItemsMother.getTwoRecentlyPlayed();
    setUpMockGetAllRequest(playListHistoryItems);

      List<PlayListHistoryItem> lastPlayedSongs = songsLocalRepository.loadLastPlayedSongs();

      assertThat(lastPlayedSongs).isNotEmpty();
      assertThat(lastPlayedSongs.getFirst().playedAt()).isAfter(lastPlayedSongs.getLast().playedAt());
  }

  @Test
  void givenNonExistingPlayedSongShouldReturnEmpty() {
    String notFoundId = "foo";
    setUpMockGetNotFoundRequest(notFoundId);
    Optional<PlayListHistoryItem> playedSong = songsLocalRepository.loadPlayedSong(notFoundId);

    assertThat(playedSong).isNotPresent();
  }

  @Test
  void givenAnExistingPlayedSongShouldReturnIt() {
    PlayListHistoryItem playListHistoryItem = PlayListHistoryItemsMother.getPlayListHistoryItem();
    setUpMockGetByIdRequest(playListHistoryItem);

    Optional<PlayListHistoryItem> playedSong = songsLocalRepository.loadPlayedSong(playListHistoryItem.id());

    assertThat(playedSong).isPresent().contains(playListHistoryItem);
  }

  @Test
  void givenANullPlayedSongShouldThrowIllegalArgumentExceptionWhenStoringIt() {
    Throwable thrown = catchThrowable(() -> songsLocalRepository.save(null));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAPlayedSongShouldStoreIt() {
    PlayListHistoryItem playListHistoryItem = PlayListHistoryItemsMother.getPlayListHistoryItem();
    setUpMockPostRequest(playListHistoryItem);

    PlayListHistoryItem savedPlayListHistoryItem = songsLocalRepository.save(playListHistoryItem);

    assertThat(savedPlayListHistoryItem).isEqualTo(playListHistoryItem);
  }

  private void setUpMockGetAllRequest(List<PlayListHistoryItem> items) {
    RequestHeadersUriSpec getRequest = mock(RequestHeadersUriSpec.class);
    ResponseSpec response = mock(ResponseSpec.class);
    when(restClient.get()).thenReturn(getRequest);
    when(getRequest.uri(anyString())).thenReturn(getRequest);
    when(getRequest.accept(any())).thenReturn(getRequest);
    when(getRequest.retrieve()).thenReturn(response);
    when(response.body(any(ParameterizedTypeReference.class))).thenReturn(items);
  }

  private void setUpMockGetNotFoundRequest(String notFoundId) {
    RequestHeadersUriSpec getRequest = mock(RequestHeadersUriSpec.class);
    ResponseSpec response = mock(ResponseSpec.class);
    when(restClient.get()).thenReturn(getRequest);
    when(getRequest.uri("/items/{id}", notFoundId)).thenReturn(getRequest);
    when(getRequest.accept(any())).thenReturn(getRequest);
    when(getRequest.retrieve()).thenReturn(response);
    when(response.onStatus(any(), any())).thenThrow(IllegalArgumentException.class);
  }

  private void setUpMockGetByIdRequest(PlayListHistoryItem item) {
    RequestHeadersUriSpec getRequest = mock(RequestHeadersUriSpec.class);
    ResponseSpec response = mock(ResponseSpec.class);
    when(restClient.get()).thenReturn(getRequest);
    when(getRequest.uri("/items/{id}", item.id())).thenReturn(getRequest);
    when(getRequest.accept(any())).thenReturn(getRequest);
    when(getRequest.retrieve()).thenReturn(response);
    when(response.onStatus(any(), any())).thenReturn(response);
    when(response.body(PlayListHistoryItem.class)).thenReturn(item);
  }

  private void setUpMockPostRequest(PlayListHistoryItem item) {
    RequestBodyUriSpec postRequest = mock(RequestBodyUriSpec.class);
    ResponseSpec response = mock(ResponseSpec.class);
    when(restClient.post()).thenReturn(postRequest);
    when(postRequest.uri(anyString())).thenReturn(postRequest);
    when(postRequest.contentType(any())).thenReturn(postRequest);
    when(postRequest.body(any(PlayListHistoryItem.class))).thenReturn(postRequest);
    when(postRequest.accept(any())).thenReturn(postRequest);
    when(postRequest.retrieve()).thenReturn(response);
    when(response.body(PlayListHistoryItem.class)).thenReturn(item);
  }
}