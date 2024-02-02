package dev.agustinventura.playlisthistory.adapter.out.rest;

import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import dev.agustinventura.playlisthistory.application.port.out.LoadLastPlayedPort;
import dev.agustinventura.playlisthistory.application.port.out.SaveLastPlayedPort;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface SongsLocalRepository extends LoadLastPlayedPort, SaveLastPlayedPort {

  @Override
  @GetExchange("/items")
  List<PlayListHistoryItem> loadLastPlayedSongs();

  @Override
  @GetExchange("/items/{id}")
  Optional<PlayListHistoryItem> loadPlayedSong(@PathVariable String id);

  @Override
  @PostExchange("/items")
  PlayListHistoryItem save(@RequestBody PlayListHistoryItem item);
}
