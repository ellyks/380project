package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.TicketNotFound;
import ouhk.comps380f.model.Bid;

public interface BidService {

    public long createBid(long price, long ticketId, String buyerName)throws IOException;

    public List<Bid> getBids();
    public List<Bid> getBid(long id);

    
}
