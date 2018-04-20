package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.dao.TicketUserRepository;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.TicketNotFound;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Bid;
import ouhk.comps380f.model.Comment;
import ouhk.comps380f.model.Ticket;
import ouhk.comps380f.model.TicketUser;
import ouhk.comps380f.service.AttachmentService;
import ouhk.comps380f.service.BidService;
import ouhk.comps380f.service.CommentService;
import ouhk.comps380f.service.TicketService;
import ouhk.comps380f.view.DownloadingView;

@Controller
@RequestMapping("ticket")
public class TicketController {

  @Resource
  TicketUserRepository ticketUserRepo;

  @Autowired
  private TicketService ticketService;

  @Autowired
  private CommentService commentService;

  @Autowired
  private AttachmentService attachmentService;

  @Autowired
  private BidService bidService;

  @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
  public String list(ModelMap model) {
    model.addAttribute("ticketDatabase", ticketService.getTickets());
    return "list";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public ModelAndView create() {
    return new ModelAndView("add", "ticketForm", new Form());
  }

  public static class Form {

    private long price;
    private String subject;
    private String body;
    private List<MultipartFile> attachments;

    public long getPrice() {
      return price;
    }

    public void setPrice(long price) {
      this.price = price;
    }

    public String getSubject() {
      return subject;
    }

    public void setSubject(String subject) {
      this.subject = subject;
    }

    public String getBody() {
      return body;
    }

    public void setBody(String body) {
      this.body = body;
    }

    public List<MultipartFile> getAttachments() {
      return attachments;
    }

    public void setAttachments(List<MultipartFile> attachments) {
      this.attachments = attachments;
    }

  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(Form form, Principal principal) throws IOException {
    long ticketId = ticketService.createTicket(principal.getName(),
            form.getSubject(), form.getPrice(),
            form.getBody(), form.getAttachments());
    return "redirect:/ticket/view/" + ticketId;
  }

  @RequestMapping(value = "register", method = RequestMethod.GET)
  public ModelAndView register() {
    return new ModelAndView("register", "ticketUser", new TicketUserController.Form());
  }

  @RequestMapping(value = "register", method = RequestMethod.POST)
  public View register(TicketUserController.Form form) throws IOException {
    TicketUser user = new TicketUser(form.getUsername(),
            form.getPassword(),
            form.getRoles()
    );
    ticketUserRepo.save(user);
    return new RedirectView("/user/list", true);
  }

  @RequestMapping(value = "view/{ticketId}", method = RequestMethod.GET)
  public String view(@PathVariable("ticketId") long ticketId,
          ModelMap model) {
    Ticket ticket = ticketService.getTicket(ticketId);
    List<Comment> comment = commentService.getComment(ticketId);
    List<Bid> bid = bidService.getBid(ticketId);
    if (ticket == null) {
      return "redirect:/ticket/list";
    }
    model.addAttribute("ticket", ticket);
    model.addAttribute("comment", comment);
    model.addAttribute("bid", bid);
    return "view";
  }

  @RequestMapping(
          value = "/{ticketId}/attachment/{attachment:.+}",
          method = RequestMethod.GET
  )
  public View download(@PathVariable("ticketId") long ticketId,
          @PathVariable("attachment") String name) {

    Attachment attachment = attachmentService.getAttachment(ticketId, name);
    if (attachment != null) {
      return new DownloadingView(attachment.getName(),
              attachment.getMimeContentType(), attachment.getContents());
    }
    return new RedirectView("/ticket/list", true);
  }

  @RequestMapping(value = "delete/{ticketId}", method = RequestMethod.GET)
  public String deleteTicket(@PathVariable("ticketId") long ticketId)
          throws TicketNotFound {
    ticketService.delete(ticketId);
    return "redirect:/ticket/list";
  }

  @RequestMapping(value = "edit/{ticketId}", method = RequestMethod.GET)
  public ModelAndView showEdit(@PathVariable("ticketId") long ticketId,
          Principal principal, HttpServletRequest request) {
    Ticket ticket = ticketService.getTicket(ticketId);
    if (ticket == null
            || (!request.isUserInRole("ROLE_ADMIN")
            && !principal.getName().equals(ticket.getcustomerName()))) {
      return new ModelAndView(new RedirectView("/ticket/list", true));
    }

    ModelAndView modelAndView = new ModelAndView("edit");
    modelAndView.addObject("ticket", ticket);

    Form ticketForm = new Form();
    ticketForm.setSubject(ticket.getSubject());
    ticketForm.setBody(ticket.getBody());
    ticketForm.setPrice(ticket.getPrice());
    modelAndView.addObject("ticketForm", ticketForm);

    return modelAndView;
  }

  @RequestMapping(value = "edit/{ticketId}", method = RequestMethod.POST)
  public View edit(@PathVariable("ticketId") long ticketId, Form form,
          Principal principal, HttpServletRequest request)
          throws IOException, TicketNotFound {
    Ticket ticket = ticketService.getTicket(ticketId);
    if (ticket == null
            || (!request.isUserInRole("ROLE_ADMIN")
            && !principal.getName().equals(ticket.getcustomerName()))) {
      return new RedirectView("/ticket/list", true);
    }

    ticketService.updateTicket(ticketId, form.getSubject(), form.getPrice(),
            form.getBody(), form.getAttachments());
    return new RedirectView("/ticket/view/" + ticketId, true);
  }

  @RequestMapping(
          value = "/{ticketId}/delete/{attachment:.+}",
          method = RequestMethod.GET
  )
  public String deleteAttachment(@PathVariable("ticketId") long ticketId,
          @PathVariable("attachment") String name) throws AttachmentNotFound {
    ticketService.deleteAttachment(ticketId, name);
    return "redirect:/ticket/edit/" + ticketId;
  }

  //newwwwww
  public static class Content {

    private String content;

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

  }

  public static class Bidding {

    private Long price;

    public Long getPrice() {
      return price;
    }

    public void setPrice(Long price) {
      this.price = price;
    }
  }

  @RequestMapping(value = "addComment/{ticketId}", method = RequestMethod.GET)
  public ModelAndView addComment(@PathVariable("ticketId") long ticketId,
          Principal principal, HttpServletRequest request) {
    Ticket ticket = ticketService.getTicket(ticketId);
    if (ticket == null) {
      return new ModelAndView(new RedirectView("/ticket/list", true));
    }
    return new ModelAndView("comment", "commentForm", new Content());
  }

  @RequestMapping(value = "addComment/{ticketId}", method = RequestMethod.POST)
  public View addComment(@PathVariable("ticketId") long ticketId, Content form,
          Principal principal)
          throws IOException {
    Ticket ticket = ticketService.getTicket(ticketId);

    if (ticket == null) {
      return new RedirectView("/ticket/list", true);
    }
    commentService.createComment(form.getContent(), 1, principal.getName());//ticketId
    return new RedirectView("/ticket/view/" + ticketId, true);
  }

  @RequestMapping(value = "bid/{ticketId}", method = RequestMethod.GET)
  public ModelAndView bid(@PathVariable("ticketId") long ticketId,
          Principal principal, HttpServletRequest request) {
    Ticket ticket = ticketService.getTicket(ticketId);
    if (ticket == null) {
      return new ModelAndView(new RedirectView("/ticket/list", true));
    }
    return new ModelAndView("bidprice", "bidForm", new Bidding());
  }

  @RequestMapping(value = "bid/{ticketId}", method = RequestMethod.POST)
  public View bid(@PathVariable("ticketId") long ticketId, Bidding form,
          Principal principal)
          throws IOException {
    Ticket ticket = ticketService.getTicket(ticketId);
    if (ticket == null) {
      return new RedirectView("/ticket/list", true);
    }
    bidService.createBid(form.getPrice(), ticketId, principal.getName());
    return new RedirectView("/ticket/view/" + ticketId, true);
  }

  //Alannnnnnnnnbnbnn
  /*@RequestMapping(value = "commentdelete/{commentId}", method = RequestMethod.GET)
  public String deleteComment(@PathVariable("commentId") long commentId)
          throws TicketNotFound {
    commentService.delete(commentId);
    return "redirect:/ticket/list";
  }*/
}
