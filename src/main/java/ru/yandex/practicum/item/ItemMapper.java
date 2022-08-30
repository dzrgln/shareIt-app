package ru.yandex.practicum.item;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDtoResponse itemToResponseItem(Item item);

    Item requestItemToItem(ItemDtoRequest itemDtoRequest);
}
