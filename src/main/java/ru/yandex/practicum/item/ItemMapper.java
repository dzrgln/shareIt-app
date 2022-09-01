package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.booking.BookingRepository;
import ru.yandex.practicum.comment.CommentMapper;

@Mapper(componentModel = "spring",
        uses = {BookingRepository.class, CommentMapper.class})

public interface ItemMapper {

    @Mapping(target = "comments", source = "commentsList")
    ItemDtoResponse itemToResponseItem(Item item);

}
