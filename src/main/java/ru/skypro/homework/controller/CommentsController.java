package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment.CommentDTO;
import ru.skypro.homework.dto.Comment.CommentsDTO;
import ru.skypro.homework.dto.Comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.service.CommentService;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("{id}/comments")
    public ResponseEntity<CommentsDTO> getComments(
            @PathVariable int id) {
        try {
            return ResponseEntity.ok(commentService.getComments(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable int id,
            @RequestBody CreateOrUpdateCommentDTO comment) {
        try {
            CommentDTO saveComment = commentService.addComment(comment, 0, id);
            return ResponseEntity.ok(saveComment);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentOwner(#commentId)")
    @DeleteMapping("{id}/comments/{commentId}")
    public void deleteComment(
            @PathVariable int id,
            @PathVariable int commentId) {
        commentService.deleteComment(commentId);
    }

    @PreAuthorize("@commentServiceImpl.isCommentOwner(#commentId)")
    @PatchMapping("{id}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable int id,
            @PathVariable int commentId,
            @RequestBody CreateOrUpdateCommentDTO comment) {
        try {
            CommentDTO saveComment = commentService.addComment(comment, commentId, id);
            return ResponseEntity.ok(saveComment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}