package org.jpa.ticketmanagerbackend.application.restcontroller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);
	
	@GetMapping(value = "/")
	public ResponseEntity<String> pong() 
	{
		LOGGER.info("Démarrage des services OK .....");
	    return new ResponseEntity<String>("Réponse du serveur: "+HttpStatus.OK.name(), HttpStatus.OK);
	}
	
	@GetMapping("/api")
	public	@ResponseBody String ping() {
		LOGGER.info("Réponse du serveur sur /api : "+ HttpStatus.OK);
    	return "{\"status\":\"OK\",\"timestamp\":\"" + System.currentTimeMillis() + "\"}";
	}

}