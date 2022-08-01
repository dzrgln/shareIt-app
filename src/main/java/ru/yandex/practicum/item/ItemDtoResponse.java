package ru.yandex.practicum.item;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.request.ItemRequest;

@Data
@NoArgsConstructor
@Builder
public class ItemDtoResponse {
    private int id;

    private String name;

    private String description;

    private boolean available;
    private ItemRequest request;

    public ItemDtoResponse(String name, String description, boolean available, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
    public ItemDtoResponse(int id, String name, String description, boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}
