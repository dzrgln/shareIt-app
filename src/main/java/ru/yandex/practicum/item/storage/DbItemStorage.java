package ru.yandex.practicum.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.booking.BookingRepository;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.item.*;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
//@Qualifier("DbItem")
@RequiredArgsConstructor
public class DbItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<Item> getItems(int userId) {
        return itemRepository.findByOwner_Id(userId);
    }

    @Override
    public Optional<ItemDtoResponse> getItemById(Integer itemId, Integer userId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new ObjectNotFoundException(String.format("Вещи с id \"%s\"не существует.", itemId));
        }
        Item item = itemRepository.findById(itemId).get();
        ItemDtoResponse itemDtoResponse = ItemMapper.toItemResponseDto(item);
        itemDtoResponse.setBookingList(bookingRepository.findByItem(item));
        return Optional.of(itemDtoResponse);
    }

    @Override
    public Item create(int userId, Item item) {
        Item newItem;
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Такаого пользователя не сущестует");
        } else {
            User owner = userRepository.findById(userId).get();
            item.setOwner(owner);
            newItem = itemRepository.save(item);
        }
        return newItem;
    }

    @Override
    public Item update(int userId, int itemId, ItemDtoRequest itemDto) {
        validateUpdate(userId, itemId, itemDto);
        Item newItem = null;
        if (itemRepository.findById(itemId).isPresent()) {
            newItem = ItemMapper.updateItem(itemDto, itemRepository.findById(itemId).get());
        } else {
            throw new ObjectNotFoundException("Указанной вещи не существует");
        }
        newItem.setOwner(userRepository.findById(userId).get());
        return itemRepository.save(newItem);
    }


    @Override
    public List<ItemDtoResponse> searchItems(String text) {
        List<ItemDtoResponse> listItems = new ArrayList<>();
        if (!text.equals("")) {
            for (Item item : itemRepository.findAll()) {
                if (item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text)
                                || item.getDescription().toLowerCase().contains(text))) {
                    listItems.add(ItemMapper.toItemResponseDto(item));
                }
            }
        }
        return listItems;
    }

    private void validateUpdate(int userId, int itemId, ItemDtoRequest itemDto) {
        System.out.println("into method");
        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException(String.format("Вещи с id \"%s\"не существует.", itemId));
        }
        System.out.println("after first checking");
        if (!userRepository.findById(userId).isPresent()) {
            throw new ValidationException(String.format("Пользователь с id \"%s\"не существует.", userId));
        }
        if (userId != itemRepository.findById(itemId).get().getOwner().getId()) {
            throw new ObjectNotFoundException(String.format("Пользователь с id \"%s\"не владелец вещи.", userId));
        }
    }

    public Item getItem(int itemId) {
        Item item = null;
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new ObjectNotFoundException(String.format("Вещи с id \"%s\"не существует.", itemId));
        } else {
            item = itemRepository.findById(itemId).get();
        }
        return item;
    }

}
