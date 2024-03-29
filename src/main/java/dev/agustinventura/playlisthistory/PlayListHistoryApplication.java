package dev.agustinventura.playlisthistory;

import dev.agustinventura.playlisthistory.adapter.out.rest.RestSongsAdapter;
import dev.agustinventura.playlisthistory.adapter.out.rest.SongsLocalRepository;
import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem.PlayListHistoryItemBuilder;
import dev.agustinventura.playlisthistory.application.port.out.LoadLastPlayedPort;
import dev.agustinventura.playlisthistory.application.port.out.SaveLastPlayedPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class PlayListHistoryApplication {

  private static final Logger logger = LoggerFactory.getLogger(PlayListHistoryApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(PlayListHistoryApplication.class, args);
  }

  private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON.getType() + "/" + MediaType.APPLICATION_JSON.getSubtype();

  @Bean
  public RestClient restClient(@Value("${song-server.base.url}") String baseUrl) {
    return RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.ACCEPT, APPLICATION_JSON)
        .defaultStatusHandler(
            HttpStatusCode::isError,
            (request, response) ->
                logger.error("Response error [code={}, body={}]", response.getStatusCode(), new String(response.getBody().readAllBytes()))
        )
        .build();
  }

  @Bean
  public Map<String, String> headers(@Value("${song-server.api-key}") String apiKey) {
    return Map.of(
        HttpHeaders.CONTENT_TYPE, APPLICATION_JSON,
        HttpHeaders.AUTHORIZATION, "Bearer " + apiKey
    );
  }

  @Bean
  public SongsLocalRepository songsLocalRepository(RestClient restClient) {
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(SongsLocalRepository.class);
  }

  @Bean
  public LoadLastPlayedPort loadLastPlayedPort(Map<String, String> headers, SongsLocalRepository songsLocalRepository) {
    return new RestSongsAdapter(headers, songsLocalRepository);
  }

  @Bean
  public SaveLastPlayedPort saveLastPlayedPort(Map<String, String> headers, SongsLocalRepository songsLocalRepository) {
    return new RestSongsAdapter(headers, songsLocalRepository);
  }

  @Bean
  ApplicationRunner appStarted(LoadLastPlayedPort loadLastPlayedPort, SaveLastPlayedPort saveLastPlayedPort) {
    return args -> {
      List<PlayListHistoryItem> lastPlayedSongs = loadLastPlayedPort.loadLastPlayedSongs();
      Optional<PlayListHistoryItem> notValidSong = loadLastPlayedPort.loadPlayedSong("foo");
      Optional<PlayListHistoryItem> playedSong = loadLastPlayedPort.loadPlayedSong(lastPlayedSongs.getFirst().id());
      PlayListHistoryItem newPlayedSong = PlayListHistoryItemBuilder.aPlayListHistoryItem().withId("SANsVA4ihQplEQIf0GZea3").withPlayedAt(
          LocalDateTime.now()).withTrack(playedSong.get().track()).build();
      saveLastPlayedPort.save(newPlayedSong);
    };
  }
}
