package nl.hilgenbos.psyichic_pies.controller;

import nl.hilgenbos.psyichic_pies.api.PiesApi;
import nl.hilgenbos.psyichic_pies.entity.PieEntity;
import nl.hilgenbos.psyichic_pies.mapper.PieMapper;
import nl.hilgenbos.psyichic_pies.model.Pie;
import nl.hilgenbos.psyichic_pies.model.PieInput;
import nl.hilgenbos.psyichic_pies.repository.PieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PiesController implements PiesApi {

    private final PieRepository pieRepository;
    private final PieMapper pieMapper;

    public PiesController(PieRepository pieRepository, PieMapper pieMapper) {
        this.pieRepository = pieRepository;
        this.pieMapper = pieMapper;
    }

    @Override
    public ResponseEntity<List<Pie>> piesGet() {
        List<Pie> pies = pieRepository.findAll()
                .stream()
                .map(pieMapper::toApi)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pies);
    }

    @Override
    public ResponseEntity<Void> piesPieIdDelete(Integer pieId) {
        if (!pieRepository.existsById(pieId)) {
            return ResponseEntity.notFound().build();
        }
        pieRepository.deleteById(pieId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Pie> piesPost(PieInput pieInput) {
        PieEntity entity = pieMapper.toEntity(pieInput);
        entity = pieRepository.save(entity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pieMapper.toApi(entity));
    }


}