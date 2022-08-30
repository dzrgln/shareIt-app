package ru.yandex.practicum.item;

public class ItemMapperUpdate {
    public static ItemDtoResponse ItemToResponseDto(Item item) {
        return new ItemDtoResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null
//                item.getCommentList().stream().map(CommentMapper::commentToResponseComment).collect(Collectors.toList())
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
                null,
                itemDto.getAvailable() != null ? Boolean.parseBoolean(itemDto.getAvailable()) : item.getAvailable()
                //              itemDto.getRequest() != null ? itemDto.getRequest() : item.getRequest()
        );
    }

}
