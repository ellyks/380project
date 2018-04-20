package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import ouhk.comps380f.model.Comment;

public interface CommentService {

    public long createComment(String content, long ticketId,String buyername) throws IOException;

    public List<Comment> getComment(long id);
    
    //public void delete(long id);
}
