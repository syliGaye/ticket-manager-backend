package org.jpa.ticketmanagerbackend.application.restcontroller;

import com.google.common.base.Preconditions;
import org.jpa.ticketmanagerbackend.application.configuration.securities.CustomSecurityContextRepository;
import org.jpa.ticketmanagerbackend.application.facade.AuthenticationFacade;
import org.jpa.ticketmanagerbackend.application.restcontroller.exception.ErrorResponse;
import org.jpa.ticketmanagerbackend.dao.domain.UserAccountDao;
import org.jpa.ticketmanagerbackend.model.entities.UserAccount;
import org.jpa.ticketmanagerbackend.utilities.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/user")
public class UserAccountRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserAccountRestController.class);
    private final UserAccountDao userAccountDao;
    private final AuthenticationFacade authenticationFacade;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final CustomSecurityContextRepository securityContextRepository;

    @Autowired
    public UserAccountRestController(
            UserAccountDao userAccountDao, SecurityContextHolderStrategy securityContextHolderStrategy,
            CustomSecurityContextRepository securityContextRepository,
            AuthenticationFacade authenticationFacade, AuthenticationManager authenticationManager
    ) {
        this.userAccountDao = userAccountDao;
        this.authenticationFacade = authenticationFacade;
        this.authenticationManager = authenticationManager;
        this.securityContextHolderStrategy = securityContextHolderStrategy;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<UserAccount>> list(
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ) {
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        return new ResponseEntity<Collection<UserAccount>>(
                this.userAccountDao.findAll(Utility.toInt(min), Utility.toInt(max)),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get")
    public ResponseEntity<UserAccount> get(
            @RequestParam(value = "id", defaultValue = "-1") String id
    ){
        Preconditions.checkNotNull(id, "Identifiant est requis !");
        AtomicReference<UserAccount> userAccount = new AtomicReference<>(new UserAccount());
        this.userAccountDao.findById(Utility.toLong(id)).ifPresent(userAccount::set);
        return new ResponseEntity<UserAccount>(
                userAccount.get(),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/account/login")
    public ResponseEntity<Object> login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try {
            Preconditions.checkNotNull(username, "Nom d'utilisateur est requis!");
            Preconditions.checkNotNull(password, "Mot de passe est requis!");
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            final SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            if (authentication == null){
                final ErrorResponse errorResponse = new ErrorResponse("Echec de l'authentification de l'utilisateur!", HttpStatus.CONFLICT);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            return new ResponseEntity<Object>(context, HttpStatus.ACCEPTED);
        }
        catch (Exception ex){
            ex.printStackTrace();
            final ErrorResponse errorResponse = new ErrorResponse("Echec de l'authentification de l'utilisateur!", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/account/logout")
    public ResponseEntity<Object> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        authenticationFacade.getAuthenticatedUser().ifPresent(u ->{
            securityContextRepository.removeContext(request, response);
            SecurityContextHolder.clearContext();
        });
        return new ResponseEntity<Object>(true, HttpStatus.GONE);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody UserAccount user){
        Preconditions.checkNotNull(user, "Utilisateur est requis !");
        AtomicReference<String> message = new AtomicReference<>("");
        AtomicBoolean error = new AtomicBoolean(false);
        this.userAccountDao.findByUsername(user.getUsername()).ifPresent(u -> {
            if (user.getId() <= 0L){
                message.set("Ce nom d'utilisateur existe déjà !");
                error.set(true);
            }
        });

        this.userAccountDao.findByEmail(user.getEmail()).ifPresent(u -> {
            if (user.getId() <= 0L){
                message.set("Cette adresse mail existe déjà !");
                error.set(true);
            }
        });

        if (error.get()){
            final ErrorResponse errorResponse = new ErrorResponse(message.get(), HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        final UserAccount userSaved = this.userAccountDao.save(user);
        return new ResponseEntity<Object>(
                userSaved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "id", defaultValue = "-1") String id){
        this.userAccountDao.findById(Utility.toLong(id)).ifPresent(this.userAccountDao::delete);
        return new ResponseEntity<Void>(HttpStatus.GONE);
    }
}
