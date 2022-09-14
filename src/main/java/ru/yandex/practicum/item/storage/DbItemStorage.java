package ru.yandex.practicum.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.BookingRepository;
import ru.yandex.practicum.booking.BookingStatus;
import ru.yandex.practicum.booking.BookingsMapper;
import ru.yandex.practicum.booking.DTO.ResponseBookingForItem;
import ru.yandex.practicum.comment.*;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.item.*;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
//@Qualifier("DbItem")
@RequiredArgsConstructor
public class DbItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;
    private final BookingsMapper bookingsMapper;

    @Override
    public List<ItemDtoResponse> getItems(int userId) {
        List<ItemDtoResponse> items = itemRepository.findByOwner_Id(userId).stream()
                .map(itemMapper::itemToResponseItem)
                .collect(Collectors.toList());
        User user = null;
        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
        }
        for (ItemDtoResponse itemDtoResponse : items) {
            if (findBookingLast(itemMapper.responseItemToItem(itemDtoResponse), user).isPresent()) {
                itemDtoResponse.setLastBooking(findBookingLast(itemMapper.responseItemToItem(itemDtoResponse), user).get());
            }
            if (findBookingNext(itemMapper.responseItemToItem(itemDtoResponse), user).isPresent()) {
                itemDtoResponse.setNextBooking(findBookingNext(itemMapper.responseItemToItem(itemDtoResponse), user).get());
            }
        }
        return items;
    }

    @Override
    public Optional<ItemDtoResponse> getItemById(Integer itemId, Integer userId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new ObjectNotFoundException(String.format("Вещи с id \"%s\"не существует.", itemId));
        }
        Item item = itemRepository.findById(itemId).get();
        ItemDtoResponse itemDtoResponse = itemMapper.itemToResponseItem(item);
        User requester = null;
        if (userRepository.findById(userId).isPresent()) {
            requester = userRepository.findById(userId).get();
        }
        if (findBookingLast(item, requester).isPresent()) {
            itemDtoResponse.setLastBooking(findBookingLast(item, requester).get());
        }
        if (findBookingNext(item, requester).isPresent()) {
            itemDtoResponse.setNextBooking(findBookingNext(item, requester).get());
        }
        return Optional.of(itemDtoResponse);
    }

    public Optional<ResponseBookingForItem> findBookingLast(Item item, User booker) {
        List<Booking> bookingList = bookingRepository.getLastBooking(booker, item,
                LocalDateTime.now());
        Optional<ResponseBookingForItem> responseBookingForItem = Optional.empty();
        if (!bookingList.isEmpty()) {
            responseBookingForItem = Optional.of(bookingsMapper.bookingToResponseBookingForItem(bookingList.get(0)));
        }
        return responseBookingForItem;
    }

    public Optional<ResponseBookingForItem> findBookingNext(Item item, User booker) {
        List<Booking> bookingList = bookingRepository.getNextBooking(booker, item,
                LocalDateTime.now());
        Optional<ResponseBookingForItem> responseBookingForItem = Optional.empty();
        if (!bookingList.isEmpty()) {
            responseBookingForItem = Optional.of(bookingsMapper.bookingToResponseBookingForItem(bookingList.get(0)));
        }
        return responseBookingForItem;
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
            newItem = ItemMapperUpdate.updateItem(itemDto, itemRepository.findById(itemId).get());
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
                    listItems.add(itemMapper.itemToResponseItem(item));
                }
            }
        }
        return listItems;
    }

    private void validateUpdate(int userId, int itemId, ItemDtoRequest itemDto) {
        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException(String.format("Вещи с id \"%s\"не существует.", itemId));
        }
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

    @Override
    public ResponseDtoComment addComment(int itemId, int userId, RequestDtoComment requestDtoComment) {
        Comment comment = commentMapper.requestCommentToComment(requestDtoComment);
        if (userRepository.findById(userId).isPresent()) {
            comment.setAuthor(userRepository.findById(userId).get());
        }
        comment.setItem(getItem(itemId));
        validateAddComment(comment);
        commentRepository.save(comment);
        ResponseDtoComment responseDtoComment = commentMapper.commentToResponseComment(comment);
        Item item = comment.getItem();
        item.getCommentsList().add(comment);
        itemRepository.save(item);
        return responseDtoComment;
    }

    private void validateAddComment(Comment comment) {
        List<Booking> bookingList = bookingRepository.findByItemAndBookerAndStatusAndStartIsBefore(
                comment.getItem(),
                comment.getAuthor(),
                BookingStatus.APPROVED,
                LocalDateTime.now());
        if (bookingList.isEmpty()) {
            throw new BookingException("Пользователь не пользовался указанной вещью");
        }
    }
}
