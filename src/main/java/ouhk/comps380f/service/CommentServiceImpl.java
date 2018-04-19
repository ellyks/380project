package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouhk.comps380f.dao.CommentRepository;
import ouhk.comps380f.model.Comment;
import ouhk.comps380f.model.Ticket;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentRepository commentRepo;

    @Override
    @Transactional
    public List<Comment> getComment() {
        return commentRepo.findAll();
    }

    @Override
    @Transactional
    public long createComment(String content, long ticketId) throws IOException {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTicketId(ticketId);
        Comment savedComment = commentRepo.save(comment);
        return savedComment.getId();
    }

}
