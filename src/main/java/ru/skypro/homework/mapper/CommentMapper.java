package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.skypro.homework.dto.Comment.CommentDTO;
import ru.skypro.homework.dto.Comment.CommentsDTO;
import ru.skypro.homework.dto.Comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.User;

import java.time.LocalTime;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {CommentMapperUtils.class},
        imports = {LocalTime.class, Collectors.class})
public interface CommentMapper {


    @Mappings({
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "authorImage", source = "author.image"),
            @Mapping(target = "authorFirstName", source = "author.firstName")
    })
    CommentDTO toCommentDTO(Comment comment);

    @Mappings({
            @Mapping(target = "pk", source = "commentId"),
            @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())"),
            @Mapping(target = "author", source = "author"),
            @Mapping(target = "text", source = "dto.text"),
            @Mapping(target = "ad", qualifiedByName = "getAdById", source = "adId")
    })
    Comment toComment(CreateOrUpdateCommentDTO dto, int commentId, User author, int adId);

    @Mappings({
            @Mapping(target = "count", expression = "java((long) ad.getComments().size())"),
            @Mapping(target = "results", expression = "java(ad.getComments().stream().map(this::toComment).collect(Collectors.toList()))")
    })
    CommentsDTO toCommentsDTO(Ad ad);


}