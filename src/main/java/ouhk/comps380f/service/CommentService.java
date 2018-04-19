package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import ouhk.comps380f.model.Comment;

public interface CommentService {

    public long createComment(String content, long ticketId) throws IOException;

    public List<Comment> getComment();
}
