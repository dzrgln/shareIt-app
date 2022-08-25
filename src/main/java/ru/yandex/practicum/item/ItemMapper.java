package ru.yandex.practicum.item;

public class ItemMapper {
    public static ItemDtoResponse toItemResponseDto(Item item) {
        return new ItemDtoResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable()
//                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static Item updateItem(ItemDtoRequest itemDto, Item item) {
        return new Item(
                item.getId(),
                itemDto.getName() != null ? itemDto.getName() : item.getName(),
                itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription(),
                null,
                itemDto.getAvailable() != null ? Boolean.parseBoolean(itemDto.getAvailable()) : item.isAvailable()
  //              itemDto.getRequest() != null ? itemDto.getRequest() : item.getRequest()
        );
    }

    public static Item toItem(ItemDtoRequest itemDto) {
        return new Item(
                0,
                itemDto.getName(),
                itemDto.getDescription(),
                null,
                Boolean.parseBoolean(itemDto.getAvailable())
           //     itemDto.getRequest()
        );
    }
}
