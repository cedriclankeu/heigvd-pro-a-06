package heigvd.ch.segfaultapi.controller;

import heigvd.ch.segfaultapi.model.Discussion;
import heigvd.ch.segfaultapi.model.Message;
import heigvd.ch.segfaultapi.model.Tag;
import heigvd.ch.segfaultapi.projection.DiscussionCreate;
import heigvd.ch.segfaultapi.repositories.DiscussionRepository;
import heigvd.ch.segfaultapi.repositories.MessageRepository;
import heigvd.ch.segfaultapi.repositories.TagRepository;
import heigvd.ch.segfaultapi.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Discussion controller.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("discussions")
public class DiscussionController {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TagRepository tagRepository;

/*
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        List<Discussion> discussions = discussionRepository.findAll();
        if (discussions.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Discussion>>(discussions, HttpStatus.OK);

    }
*/
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody DiscussionCreate payload) {

        if (!discussionRepository.findAllBySujet(payload.getSujet()).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        // On commence par créer le message

        Message message = new Message();

        message.setDate(LocalDateTime.now());

        message.setAuthor(utilisateurRepository.findById(payload.getUtilisateurID()).get());

        message.setText(payload.getContenu());

        messageRepository.save(message);

        // Puis on l'ajoute à la discussion

        Discussion discussion = new Discussion();

        discussion.setSujet(payload.getSujet());

        discussion.setMsgracine(message);

        for (Integer tagid :payload.getTags()) {
            Tag tag = tagRepository.findById(tagid).get();

            System.out.println(tag.getNom());

            discussion.getTagList().add(tag);
        }

        discussionRepository.save(discussion);

        return new ResponseEntity<>(discussion.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<Discussion> getAll (@RequestParam("page") int page) {

        Pageable tstPage = PageRequest.of(page, 7, Sort.by("msgracine.date"));
        return discussionRepository.findAll(tstPage);
    }
}
