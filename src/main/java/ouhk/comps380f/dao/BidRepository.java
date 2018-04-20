package ouhk.comps380f.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ouhk.comps380f.model.Bid;


public interface BidRepository extends JpaRepository<Bid, Long> {
  
  public List<Bid> findByTicketId(long id);
    
}
