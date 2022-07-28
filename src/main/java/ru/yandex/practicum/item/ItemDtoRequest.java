package ru.yandex.practicum.item;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.request.ItemRequest;

@Data
@NoArgsConstructor
@Builder
public class ItemDtoRequest {
    private int id;

    private String name;

    private String description;

    private String available;
    private ItemRequest request;

    public ItemDtoRequest(String name, String description, String available, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
    public ItemDtoRequest(int id, String name, String description, String available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}
