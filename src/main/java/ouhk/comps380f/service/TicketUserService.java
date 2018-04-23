package ouhk.comps380f.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ouhk.comps380f.dao.TicketUserRepository;
import ouhk.comps380f.exception.TicketNotFound;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Ticket;
import ouhk.comps380f.model.TicketUser;
import ouhk.comps380f.model.UserRole;

@Service
public class TicketUserService implements UserDetailsService {

    @Resource
    TicketUserRepository ticketUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        TicketUser ticketUser = ticketUserRepo.findOne(username);
        if (ticketUser == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : ticketUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new User(ticketUser.getUsername(), ticketUser.getPassword(), authorities);
    }

    public void updateUser(String Username,String Password, List<UserRole> role) {
        TicketUser updatedbidUser = ticketUserRepo.findOne(Username);
  
        updatedbidUser.setUsername(Username);
        updatedbidUser.setPassword(Password); 
        updatedbidUser.setRoles(role);
        ticketUserRepo.save(updatedbidUser);
    }


}
