package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comment.RequestDtoComment;
import ru.yandex.practicum.comment.ResponseDtoComment;
import ru.yandex.practicum.item.storage.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;



    @GetMapping
    public List<ItemDtoResponse> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен GET-запрос к эндпоинту /items");
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse getUserById(@PathVariable("itemId") int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemById(itemId, userId).get();
    }

    @PostMapping
    public Item create(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Создан объект '{}'", item);
        return itemService.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse update(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable("itemId") int itemId
            , @Validated @RequestBody ItemDtoRequest itemDto) {
        log.info("Обновлен объект '{}'", itemDto);
        return itemMapper.itemToResponseItem(itemService.update(userId, itemId, itemDto));
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> foundedItems(@RequestParam String text) {
        log.info("Поиск объектов, содержащих слово '{}'", text);
        return itemService.searchItems(text.toLowerCase());
    }
    @PostMapping("/{itemId}/comment")
    public ResponseDtoComment addComment(@PathVariable("itemId") int itemId,
                                         @Validated @RequestBody RequestDtoComment requestDtoComment,
                                         @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Создан комментарий '{}'", requestDtoComment);
        return itemService.addComment(itemId, userId, requestDtoComment);
    }
}
