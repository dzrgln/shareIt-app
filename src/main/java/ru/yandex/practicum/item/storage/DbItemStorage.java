package ru.yandex.practicum.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.item.*;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
//@Qualifier("DbItem")
@RequiredArgsConstructor
public class DbItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<Item> getItems(int userId) {
        return itemRepository.findAll();
    }

    @Override
    public Optional<ItemDtoResponse> getItemById(Integer itemId, Integer userId) {

        return Optional.of(ItemMapper.toItemResponseDto(itemRepository.findById(itemId).get()));
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
//        validateUpdate(userId, itemId, itemDto);

       Item newItem = ItemMapper.updateItem(itemDto, getItemFromRep(itemId));
//        item.setName(newItem.getName());
//        item.setDescription(newItem.getDescription());
        newItem.setOwner(userRepository.findById(userId).get());
//        item.setAvailable(newItem.isAvailable());

        return itemRepository.save(ItemMapper.updateItem(itemDto, newItem));
    }


    @Override
    public List<ItemDtoResponse> searchItems(String text) {
        List<ItemDtoResponse> listItems = new ArrayList<>();
        if (!text.equals("")) {
            for (Item item : itemRepository.findAll()) {
                if (item.isAvailable() &&
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
        if (userRepository.findById(userId).isEmpty()) {
            throw new ValidationException(String.format("Пользователь с id \"%s\"не существует.", userId));
        }
        if (userId != itemRepository.findById(itemId).get().getOwner().getId()) {
            throw new ObjectNotFoundException(String.format("Пользователь с id \"%s\"не владелец вещи.", userId));
        }
    }

    private Item getItemFromRep(Integer itemId){
                return itemRepository.findById(itemId).get();
    }

}
