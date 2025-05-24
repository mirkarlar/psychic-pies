package nl.hilgenbos.psyichic_pies.controller;

import nl.hilgenbos.psyichic_pies.api.PiesApi;
import nl.hilgenbos.psyichic_pies.entity.PieEntity;
import nl.hilgenbos.psyichic_pies.mapper.PieMapper;
import nl.hilgenbos.psyichic_pies.model.Pie;
import nl.hilgenbos.psyichic_pies.model.PieInput;
import nl.hilgenbos.psyichic_pies.repository.PieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PiesController implements PiesApi {

    private static final Logger logger = LoggerFactory.getLogger(PiesController.class);

    private final PieRepository pieRepository;
    private final PieMapper pieMapper;

    public PiesController(PieRepository pieRepository, PieMapper pieMapper) {
        this.pieRepository = pieRepository;
        this.pieMapper = pieMapper;
    }

    @Override
    public ResponseEntity<List<Pie>> piesGet() {
        logger.info("Retrieving all pies");
        logger.debug("Executing findAll() on pieRepository");
        List<Pie> pies = pieRepository.findAll()
                .stream()
                .map(pieMapper::toApi)
                .collect(Collectors.toList());
        logger.debug("Retrieved {} pies from repository", pies.size());
        return ResponseEntity.ok(pies);
    }

    @Override
    public ResponseEntity<Void> piesPieIdDelete(Integer pieId) {
        logger.info("Attempting to delete pie with ID: {}", pieId);
        logger.debug("Checking if pie with ID {} exists", pieId);
        if (!pieRepository.existsById(pieId)) {
            logger.info("Pie with ID {} not found", pieId);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Deleting pie with ID {}", pieId);
        pieRepository.deleteById(pieId);
        logger.info("Successfully deleted pie with ID: {}", pieId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Pie> piesPost(PieInput pieInput) {
        logger.info("Creating new pie");
        logger.debug("Converting PieInput to PieEntity: {}", pieInput);
        PieEntity entity = pieMapper.toEntity(pieInput);
        logger.debug("Saving new pie entity to repository");
        entity = pieRepository.save(entity);
        logger.info("Successfully created new pie with ID: {}", entity.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pieMapper.toApi(entity));
    }


}
