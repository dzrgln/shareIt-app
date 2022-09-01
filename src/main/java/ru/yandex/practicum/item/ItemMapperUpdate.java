package ru.yandex.practicum.item;

public class ItemMapperUpdate {

    public static Item updateItem(ItemDtoRequest itemDto, Item item) {
        return new Item(
                item.getId(),
                itemDto.getName() != null ? itemDto.getName() : item.getName(),
                itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription(),
                null,
                !item.getBookingList().isEmpty() ? item.getBookingList() : null,
                !item.getCommentsList().isEmpty() ? item.getCommentsList() : null,
                itemDto.getAvailable() != null ? Boolean.parseBoolean(itemDto.getAvailable()) : item.getAvailable()
                //              itemDto.getRequest() != null ? itemDto.getRequest() : item.getRequest()
        );
    }

}
