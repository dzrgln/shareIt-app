package ru.yandex.practicum.item.storage;

import ru.yandex.practicum.comment.RequestDtoComment;
import ru.yandex.practicum.comment.ResponseDtoComment;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.item.ItemDtoRequest;
import ru.yandex.practicum.item.ItemDtoResponse;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> getItems(int userId);

    Optional<ItemDtoResponse> getItemById(Integer itemId, Integer userId);

    Item create(int userId, Item item);

    Item update (int userId, int itemId, ItemDtoRequest itemDto);

    List<ItemDtoResponse> searchItems(String text);

    ResponseDtoComment addComment(int itemId, int userId, RequestDtoComment requestDtoComment);
}
