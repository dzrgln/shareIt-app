package ru.yandex.practicum.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comment.Comment;
import ru.yandex.practicum.item.storage.ItemStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemStorage itemStorage;

    public ItemController(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @GetMapping
    public List<Item> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен GET-запрос к эндпоинту /items");
        return itemStorage.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse getUserById(@PathVariable("itemId") int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemStorage.getItemById(itemId, userId).get();
    }

    @PostMapping
    public Item create(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Создан объект '{}'", item);
        return itemStorage.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable("itemId") int itemId
            , @Validated @RequestBody ItemDtoRequest itemDto) {
        log.info("Обновлен объект '{}'", itemDto);
        return itemStorage.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> foundedItems(@RequestParam String text) {
        log.info("Поиск объектов, содержащих слово '{}'", text);
        return itemStorage.searchItems(text.toLowerCase());
    }
    @PostMapping("/{itemId}")
    public Item addComment( @PathVariable("itemId") int itemId,
                            @RequestBody Comment comment,
                            @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Создан объект '{}'", comment);
        return itemStorage.create(userId, comment);
    }
}
