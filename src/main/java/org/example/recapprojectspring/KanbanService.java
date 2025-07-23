package org.example.recapprojectspring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Stack;

@Service
@RequestMapping("/api")
public class KanbanService {

    Stack<Action> undoStack = new Stack<>();
    Stack<Action> redoStack = new Stack<>();

    final EntryRepository entryRepository;
    final IdService idService;

    public KanbanService(EntryRepository entryRepository,  IdService idService) {
        this.entryRepository = entryRepository;
        this.idService = idService;
        //entryRepository.save(new Entry(idService.generateId(), "Test"));
    }

    public List<Entry> findAll(){
        return entryRepository.findAll();
    }

    public Entry findById(String id){
        return entryRepository.findById(id).orElse(null);
    }

    public Entry create(TodoDTO dto){
        redoStack.clear();
        Entry e = entryRepository.save(new Entry(idService.generateId(), dto));
        undoStack.push(new Action(e, null));
        return entryRepository.save(e);
    }

    public String deleteById(String id){
        redoStack.clear();
        Entry e = entryRepository.findById(id).orElse(null);
        if(e == null){
            return id;
        }
        undoStack.push(new Action(null, e));
        entryRepository.deleteById(id);
        return id;
    }

    public Entry update(String id, TodoDTO dto){
        redoStack.clear();
        Entry e = entryRepository.findById(id).orElse(null);
        if(e == null){
            throw new IllegalArgumentException("Entry not found");
        }
        undoStack.push(new Action(new Entry(id, dto), new Entry(id, e.description(), e.status())));
        e = e.withDescription(dto.description());
        e = e.withStatus(EntryStatus.valueOf(dto.status().toUpperCase()));
        return entryRepository.save(e);
    }

    public ResponseEntity<String> undo(){
        if(undoStack.isEmpty()){
            return new ResponseEntity<>("UNDO not possible, Stack empty", HttpStatus.NOT_MODIFIED);
        }
        Action action = undoStack.pop();
        HttpStatus status = reverseAction(action);
        // A redo is a reverse undo; Swapping old and new value therefore makes a redo
        redoStack.push(new Action(action.oldValue, action.newValue));
        return new ResponseEntity<>("UNDO - Old Value: " + action.getOldValue() + " New Value: " + action.getNewValue(), status);
    }
    public ResponseEntity<String> redo(){
        if(redoStack.isEmpty()){
            return new ResponseEntity<>("REDO not possible, Stack empty", HttpStatus.NOT_MODIFIED);
        }
        Action action = redoStack.pop();
        HttpStatus status = reverseAction(action);
        // A redo is a reverse undo; Swapping old and new value therefore makes a redo
        undoStack.push(new Action(action.oldValue, action.newValue));
        return new ResponseEntity<>("REDO - Old Value: " + action.getOldValue() + " New Value: " + action.getNewValue(), status);
    }

    private HttpStatus reverseAction(Action action){
        // If old value is null, then the last action was an action to create
        // So new value needs  to be deleted
        if(action.oldValue == null){
            entryRepository.delete(action.getNewValue());
            return HttpStatus.NO_CONTENT;
        }
        // Otherwise it was either an update or an action to delete
        // In both cases the new value needs to be reverted to what the old Value was
        else{
            entryRepository.save(action.getOldValue());
            if(action.newValue == null){
                return HttpStatus.CREATED;
            }
            return HttpStatus.OK;
        }
    }
}
