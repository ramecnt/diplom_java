package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment.CommentDTO;
import ru.skypro.homework.dto.Comment.CommentsDTO;
import ru.skypro.homework.dto.Comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    @Override
    public CommentDTO addComment(CreateOrUpdateCommentDTO commentDto, int commentId, int adId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);

        Comment comment = commentMapper.toComment(commentDto, commentId, user, adId);

        return commentMapper.toCommentDTO(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentsDTO getComments(int adId) {

        Ad ad = adRepository.findById(adId).orElse(null);
        if (ad == null) {
            return null;
        }

        return commentMapper.toCommentsDTO(ad);
    }

    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public boolean isCommentOwner(int commentId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);
        return commentRepository.findById(commentId)
                .map(comment -> comment.getAuthor().getId() == user.getId())
                .orElse(false);
    }
}