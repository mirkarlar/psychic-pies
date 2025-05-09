package nl.hilgenbos.psyichic_pies.mapper;

import nl.hilgenbos.psyichic_pies.entity.PieEntity;
import nl.hilgenbos.psyichic_pies.model.Pie;
import nl.hilgenbos.psyichic_pies.model.PieInput;
import org.springframework.stereotype.Component;

@Component
public class PieMapper {
    public Pie toApi(PieEntity entity) {
        Pie pie = new Pie();
        pie.setId(entity.getId());
        pie.setName(entity.getName());
        pie.setPrice(entity.getPrice());
        return pie;
    }

    public PieEntity toEntity(PieInput pieInput) {
        PieEntity entity = new PieEntity();
        entity.setName(pieInput.getName());
        entity.setPrice(pieInput.getPrice());
        return entity;
    }
}