package dev.agustinventura.playlisthistory;

import dev.agustinventura.playlisthistory.adapter.out.rest.SongsLocalRepository;
import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem.PlayListHistoryItemBuilder;
import dev.agustinventura.playlisthistory.application.port.out.LoadLastPlayedPort;
import dev.agustinventura.playlisthistory.application.port.out.SaveLastPlayedPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class PlayListHistoryApplication {

  public static void main(String[] args) {
    SpringApplication.run(PlayListHistoryApplication.class, args);
  }

  @Bean
  public RestClient restClient(@Value("${song-server.base.url}") String baseUrl) {
    return RestClient.builder()
        .baseUrl(baseUrl)
        .build();
  }

  @Bean
  public LoadLastPlayedPort loadLastPlayedPort(RestClient restClient) {
    return new SongsLocalRepository(restClient);
  }

  @Bean
  public SaveLastPlayedPort saveLastPlayedPort(RestClient restClient) {
    return new SongsLocalRepository(restClient);
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
