package dev.agustinventura.playlisthistory.adapter.out.rest;

import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface SongsLocalRepository {

  @GetExchange("/items")
  List<PlayListHistoryItem> getAll();

  @GetExchange("/items/{id}")
  Optional<PlayListHistoryItem> getWithId(@PathVariable String id);

  @PostExchange("/items")
  PlayListHistoryItem post(@RequestHeader Map<String, String> headers, @RequestBody PlayListHistoryItem item);
}
