package com.fbs.app.controller;

import com.fbs.app.dto.EventRequestDto;
import com.fbs.app.dto.TransactionRequestDto;
import com.fbs.app.model.EventModel;
import com.fbs.app.model.TransactionModel;
import com.fbs.app.service.EventService;
import com.fbs.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fbs/events")

public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/add")
    public ResponseEntity<String> Event(@RequestBody EventRequestDto event ){
        eventService.Event(event);
        return ResponseEntity.ok("Event Added");
    }

    @GetMapping("/")
    public List<EventModel> getAllEvent(){
        return eventService.readEvent();
    }

    @DeleteMapping("/{id}")
    public String delEvent(@PathVariable Long id){
        eventService.delEvent(id);
        return "Event Deleted";
    }
}
