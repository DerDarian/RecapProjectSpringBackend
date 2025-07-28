package org.example.recapprojectspring.ToDo;

import org.example.recapprojectspring.InvalidDTOException;
import org.springframework.http.HttpStatus;
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
        validateTodoTDO(dto);
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
        validateTodoTDO(dto);
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

    private void validateTodoTDO(TodoDTO dto){

        if(dto == null){
            throw new InvalidDTOException("TodoDTO dto is null");
        }
        String message = "";
        if(dto.description() == null || dto.description().trim().isEmpty())
            message = "TodoDTO description is empty\n";
        if(dto.status() == null){
            message += "No status is provided\n";
        }else{
            try{
                EntryStatus status = EntryStatus.valueOf(dto.status().toUpperCase());
            }catch(IllegalArgumentException e){
                message += "Invalid entry status provided\n";
                message += "Valid entry status values are: OPEN, IN_PROGRESS, DONE";
            }
        }
        if(!message.isEmpty()){
            throw new InvalidDTOException(message);
        }
    }
}
