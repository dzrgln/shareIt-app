package ru.yandex.practicum.item.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.item.ItemDtoRequest;
import ru.yandex.practicum.item.ItemDtoResponse;
import ru.yandex.practicum.item.ItemMapper;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private Map<Integer, Item> items = new HashMap<>();
    private final UserStorage userStorage;
    private int id = 0;

    public InMemoryItemStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<Item> getItems(int userId) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : items.values()) {
            if (isOwner(item.getId(), userId)) {
                itemList.add(item);
            }
        }
        return itemList;
    }

    @Override
    public Optional<ItemDtoResponse> getItemById(Integer itemId, Integer userId) {
        Optional<ItemDtoResponse> optionalItemDto;
        if (items.containsKey(itemId)) {
            Item item = items.get(itemId);
            ItemDtoResponse itemDto = ItemMapper.toItemResponseDto(item);
            optionalItemDto = Optional.of(itemDto);
        } else {
            throw new ObjectNotFoundException(String.format("Вещи с id \"%s\"не существует.", id));
        }
        return optionalItemDto;
    }

    @Override
    public Item create(int userid, Item item) {
//        if (userid == 0) {
//            throw new ValidationException("Необходимо ввести непустой id");
//        }
        if (!userStorage.isUser(userid)){
            throw new ObjectNotFoundException("Такаого пользователя не сущестует");
        }
            System.out.println("Inside if");
        System.out.println("Is present " + userStorage.isUser(userid));
            id++;
            item.setId(id);
            item.setOwner(userStorage.getUserById(userid).get());
            System.out.println("After getUserById");
            items.put(id, item);

        return item;
    }

    public boolean isOwner(Integer itemId, Integer userId) {
        User owner;
        User requester;
        if (userStorage.getUserById(userId).isPresent()) {
            owner = items.get(itemId).getOwner();
            requester = userStorage.getUserById(userId).get();
        } else {
            throw new ObjectNotFoundException(String.format("Пользователя с id \"%s\"не существует.", userId));
        }
        return owner.getId() == requester.getId();
    }

    @Override
    public Item update(int userId, int itemId, ItemDtoRequest itemDto) {
        Item item;
        if (items.containsKey(itemId)) {
            if (userStorage.getUserById(userId).isPresent()) {
                if (userStorage.getUserById(userId).get().getId() == items.get(itemId).getOwner().getId()) {
                    item = ItemMapper.updateItem(itemDto, items.get(itemId));
                    item.setOwner(userStorage.getUserById(userId).get());
                    item.setId(itemId);
                    items.remove(itemId);
                    items.put(itemId, item);
                } else {
                    throw new ObjectNotFoundException(String.format("Пользователь с id \"%s\"не владелец вещи.", userId));
                }
            } else {
                throw new ValidationException(String.format("Пользователь с id \"%s\"не существует.", userId));
            }
        } else {
            throw new ObjectNotFoundException(String.format("Вещи с id \"%s\"не существует.", itemId));
        }
        return item;
    }

    @Override
    public List<ItemDtoResponse> searchItems(String text) {
        List<ItemDtoResponse> listItems = new ArrayList<>();
        if (!text.equals("")) {
            for (Item item : items.values()) {
                if (item.isAvailable()) {
                    if (item.getName().toLowerCase().contains(text)
                            || item.getDescription().toLowerCase().contains(text)) {
                        listItems.add(ItemMapper.toItemResponseDto(item));
                    }
                }
            }
        }
        return listItems;
    }
}
