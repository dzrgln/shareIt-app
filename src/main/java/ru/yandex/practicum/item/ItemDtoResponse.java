package ru.yandex.practicum.item;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.request.Request;

@Data
@NoArgsConstructor

public class ItemDtoResponse {
    private int id;

    private String name;

    private String description;

    private boolean available;
    private Request request;

    public ItemDtoResponse(String name, String description, boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
   //     this.request = request;
    }
    public ItemDtoResponse(int id, String name, String description, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
  //      this.request = request;
    }
}
