package ru.yandex.practicum.item;

public class ItemMapper {
    public static ItemDtoResponse toItemResponseDto(Item item) {
        return new ItemDtoResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getBookingList()
//                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static Item updateItem(ItemDtoRequest itemDto, Item item) {
        return new Item(
                item.getId(),
                itemDto.getName() != null ? itemDto.getName() : item.getName(),
                itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription(),
                null,
                null,
                itemDto.getAvailable() != null ? Boolean.parseBoolean(itemDto.getAvailable()) : item.getAvailable()
                //              itemDto.getRequest() != null ? itemDto.getRequest() : item.getRequest()
        );
    }

}
