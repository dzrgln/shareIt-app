package ru.yandex.practicum.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.user.User;

@Mapper(componentModel = "spring",
        uses = User.class,
        imports = User.class)
public interface CommentMapper {

    Comment requestCommentToComment(RequestDtoComment requestDtoComment);

    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    ResponseDtoComment commentToResponseComment(Comment comment);
}
