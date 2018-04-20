package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouhk.comps380f.dao.BidRepository;
import ouhk.comps380f.model.Bid;
import ouhk.comps380f.model.Ticket;

@Service
public class BidServiceImpl implements BidService {

    @Resource
    private BidRepository bidRepo;
    
     @Override
    @Transactional
    public List<Bid> getBids() {
        return bidRepo.findAll();
    }

    @Override
    @Transactional
    public List<Bid> getBid(long id) {
        return bidRepo.findByTicketId(id);
    }
    

    @Override
    @Transactional
  public long createBid(long price, long ticketId, String buyerName) throws IOException {
        Bid bid = new Bid();
        bid.setPrice(price);
        bid.setTicketId(ticketId);
        bid.setBuyername(buyerName);
        Bid savedBid = bidRepo.save(bid);
        return savedBid.getId();
    }

}
