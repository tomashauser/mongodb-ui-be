package cz.cvut.fel.hauseto2.ds2_mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/mongo-api")
public class QueryController {
    @Autowired
    private MongoService mongoService;

    @RequestMapping("/")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping(value = "find/{collection}/{query}")
    public ResponseEntity<List<String>> find(@PathVariable String collection,
                                             @PathVariable String query) {
        return mongoService.find(collection, query);
    }

    @GetMapping(value = "empty/{collection}")
    public ResponseEntity<String> emptyCollection(@PathVariable String collection) {
        return mongoService.emptyCollection(collection);
    }

    @GetMapping(value = "drop/{collection}")
    public ResponseEntity<String> dropCollection(@PathVariable String collection) {
        return mongoService.dropCollection(collection);
    }

    @GetMapping(value = "delete/{collection}/{query}")
    public ResponseEntity<String> delete(@PathVariable String collection, @PathVariable String query) {
        return mongoService.delete(collection, query);
    }

    @GetMapping(value = "update/{collection}/{query}/{newData}")
    public ResponseEntity<String> updateCollection(@PathVariable String collection, @PathVariable String query, @PathVariable String newData) {
        return mongoService.update(collection, query, newData);
    }

    @GetMapping(value = "insert/{collection}/{object}")
    public ResponseEntity<String> insert(@PathVariable String collection,
                                         @PathVariable String object) {
        return mongoService.insert(collection, object);
    }
}
