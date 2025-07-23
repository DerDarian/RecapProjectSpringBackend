package org.example.recapprojectspring;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api")
public class KanbanController {

    final  KanbanService kanbanService;

    public  KanbanController(KanbanService kanbanService){
        this.kanbanService = kanbanService;
    }

    @GetMapping("/todo")
    public List<TodoDTO> findAll(){
        return convert(kanbanService.findAll());
    }

    @PostMapping("/todo")
    @ResponseBody
    public ResponseEntity<String> create(@RequestBody TodoDTO dto){
        return new ResponseEntity<>( kanbanService.create(dto).id(), HttpStatus.CREATED);
    }

    @GetMapping("/todo/{id}")
    public TodoDTO findById(@PathVariable String id){
        return new TodoDTO(kanbanService.findById(id));
    }

    @GetMapping("/undo")
    @ResponseBody
    public ResponseEntity<String> undo(){
        return kanbanService.undo();
    }

    @GetMapping("/redo")
    @ResponseBody
    public ResponseEntity<String> redo(){
        return kanbanService.redo();
    }

    @PutMapping("/todo/{id}")
    public void update(@PathVariable String id, @RequestBody TodoDTO dto){
        System.out.println(id);
        kanbanService.update(id, dto);
    }

    @DeleteMapping("/todo/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable String id){
        return new ResponseEntity<>(kanbanService.deleteById(id), HttpStatus.NO_CONTENT);
    }

    private List<TodoDTO> convert(List<Entry> entries){
        return entries.stream().map(TodoDTO::new).collect(Collectors.toList());
    }
}
