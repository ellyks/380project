package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Ticket;
import ouhk.comps380f.view.DownloadingView;

@Controller
@RequestMapping("ticket")
public class TicketController {

  private volatile long TICKET_ID_SEQUENCE = 1;
  private Map<Long, Ticket> ticketDatabase = new Hashtable<>();

  @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
  public String list(ModelMap model) {
    model.addAttribute("ticketDatabase", ticketDatabase);
    return "list";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public ModelAndView create() {
    return new ModelAndView("add", "ticketForm", new Form());
  }

  @RequestMapping(value = "comment", method = RequestMethod.GET)
  public ModelAndView comment() {
    return new ModelAndView("comment", "ticketForm", new Form());//change later
  }

  @RequestMapping(value = "bid", method = RequestMethod.GET)
  public ModelAndView bidprice() {
    return new ModelAndView("bidprice", "ticketForm", new Form());//change later
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
  public View create(Form form, Principal principal) throws IOException {
    Ticket ticket = new Ticket();
    ticket.setId(this.getNextTicketId());
    ticket.setCustomerName(principal.getName());
    ticket.setSubject(form.getSubject());
    ticket.setBody(form.getBody());

    ticket.setPrice(form.getPrice());

    for (MultipartFile filePart : form.getAttachments()) {
      Attachment attachment = new Attachment();
      attachment.setName(filePart.getOriginalFilename());
      attachment.setMimeContentType(filePart.getContentType());
      attachment.setContents(filePart.getBytes());
      if (attachment.getName() != null && attachment.getName().length() > 0
              && attachment.getContents() != null && attachment.getContents().length > 0) {
        ticket.addAttachment(attachment);
      }
    }
    this.ticketDatabase.put(ticket.getId(), ticket);
    return new RedirectView("/ticket/view/" + ticket.getId(), true);
  }

  private synchronized long getNextTicketId() {
    return this.TICKET_ID_SEQUENCE++;
  }

  @RequestMapping(value = "view/{ticketId}", method = RequestMethod.GET)
  public String view(@PathVariable("ticketId") long ticketId,
          ModelMap model) {
    Ticket ticket = this.ticketDatabase.get(ticketId);
    if (ticket == null) {
      return "redirect:/ticket/list";
    }
    model.addAttribute("ticketId", Long.toString(ticketId));
    model.addAttribute("ticket", ticket);
    return "view";
  }

  @RequestMapping(
          value = "/{ticketId}/attachment/{attachment:.+}",
          method = RequestMethod.GET
  )
  public View download(@PathVariable("ticketId") long ticketId,
          @PathVariable("attachment") String name) {
    Ticket ticket = this.ticketDatabase.get(ticketId);
    if (ticket != null) {
      Attachment attachment = ticket.getAttachment(name);
      if (attachment != null) {
        return new DownloadingView(attachment.getName(),
                attachment.getMimeContentType(), attachment.getContents());
      }
    }
    return new RedirectView("/ticket/list", true);
  }

  @RequestMapping(
          value = "/{ticketId}/delete/{attachment:.+}",
          method = RequestMethod.GET
  )
  public String deleteAttachment(@PathVariable("ticketId") long ticketId,
          @PathVariable("attachment") String name) {
    Ticket ticket = this.ticketDatabase.get(ticketId);
    if (ticket != null) {
      if (ticket.hasAttachment(name)) {
        ticket.deleteAttachment(name);
      }
    }
    return "redirect:/ticket/edit/" + ticketId;
  }

  @RequestMapping(value = "edit/{ticketId}", method = RequestMethod.GET)
  public ModelAndView showEdit(@PathVariable("ticketId") long ticketId,
          Principal principal, HttpServletRequest request) {
    Ticket ticket = this.ticketDatabase.get(ticketId);
    if (ticket == null
            || (!request.isUserInRole("ROLE_ADMIN")
            && !principal.getName().equals(ticket.getCustomerName()))) {
      return new ModelAndView(new RedirectView("/ticket/list", true));
    }
    ModelAndView modelAndView = new ModelAndView("edit");
    modelAndView.addObject("ticketId", Long.toString(ticketId));
    modelAndView.addObject("ticket", ticket);

    Form ticketForm = new Form();
    ticketForm.setSubject(ticket.getSubject());
    ticketForm.setBody(ticket.getBody());
    modelAndView.addObject("ticketForm", ticketForm);

    return modelAndView;
  }

  @RequestMapping(value = "edit/{ticketId}", method = RequestMethod.POST)
  public View edit(@PathVariable("ticketId") long ticketId, Form form,
          Principal principal, HttpServletRequest request)
          throws IOException {
    Ticket ticket = this.ticketDatabase.get(ticketId);
    if (ticket == null
            || (!request.isUserInRole("ROLE_ADMIN")
            && !principal.getName().equals(ticket.getCustomerName()))) {
      return new RedirectView("/ticket/list", true);
    }

    ticket.setSubject(form.getSubject());
    ticket.setBody(form.getBody());

    for (MultipartFile filePart : form.getAttachments()) {
      Attachment attachment = new Attachment();
      attachment.setName(filePart.getOriginalFilename());
      attachment.setMimeContentType(filePart.getContentType());
      attachment.setContents(filePart.getBytes());
      if (attachment.getName() != null && attachment.getName().length() > 0
              && attachment.getContents() != null && attachment.getContents().length > 0) {
        ticket.addAttachment(attachment);
      }
    }
    this.ticketDatabase.put(ticket.getId(), ticket);
    return new RedirectView("/ticket/view/" + ticket.getId(), true);
  }

  @RequestMapping(value = "delete/{ticketId}", method = RequestMethod.GET)
  public String deleteTicket(@PathVariable("ticketId") long ticketId) {
    if (this.ticketDatabase.containsKey(ticketId)) {
      this.ticketDatabase.remove(ticketId);
    }
    return "redirect:/ticket/list";
  }

}
