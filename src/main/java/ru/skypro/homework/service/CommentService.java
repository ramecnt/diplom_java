package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment.CommentDTO;
import ru.skypro.homework.dto.Comment.CommentsDTO;
import ru.skypro.homework.dto.Comment.CreateOrUpdateCommentDTO;

public interface CommentService {
    CommentDTO addComment(CreateOrUpdateCommentDTO comment, int commentId, int adId);

    CommentsDTO getComments(int adId);

    void deleteComment(int commentId);

    boolean isCommentOwner(int commentId);
}