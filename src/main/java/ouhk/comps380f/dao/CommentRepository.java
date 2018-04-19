package ouhk.comps380f.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ouhk.comps380f.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
