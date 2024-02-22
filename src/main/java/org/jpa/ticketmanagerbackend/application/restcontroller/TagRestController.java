package org.jpa.ticketmanagerbackend.application.restcontroller;

import com.google.common.base.Preconditions;
import org.jpa.ticketmanagerbackend.dao.domain.TagDao;
import org.jpa.ticketmanagerbackend.model.entities.Tag;
import org.jpa.ticketmanagerbackend.utilities.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("tag")
public class TagRestController extends BaseRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TagRestController.class);
    private final TagDao tagDao;

    @Autowired
    public TagRestController(TagDao tagDao) {
        super();
        this.tagDao = tagDao;
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<Tag>> list(
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ) {
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        return new ResponseEntity<Collection<Tag>>(
                this.tagDao.findAll(Utility.toInt(min), Utility.toInt(max)),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get")
    public ResponseEntity<Tag> get(
            @RequestParam(value = "id", defaultValue = "-1") String id
    ){
        Preconditions.checkNotNull(id, "Identifiant est requis !");
        AtomicReference<Tag> tag = new AtomicReference<>(new Tag());
        this.tagDao.findById(Utility.toLong(id)).ifPresent(tag::set);
        return new ResponseEntity<Tag>(
                tag.get(),
                HttpStatus.FOUND
        );
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<Tag> add(@RequestBody Tag tag){
        Preconditions.checkNotNull(tag, "Tag est requis !");
        final Tag tagSaved = this.tagDao.save(tag);
        return new ResponseEntity<Tag>(
                tagSaved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "id", defaultValue = "-1") String id){
        this.tagDao.findById(Utility.toLong(id)).ifPresent(this.tagDao::delete);
        return new ResponseEntity<Void>(HttpStatus.GONE);
    }
}
