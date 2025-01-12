package si.travelbuddy.ticketsearch.entity;
import sun.security.timestamp.TimestampToken;

import java.sql.Timestamp;
import javax.persistence.Converter;
import java.time.Instant;
import javax.persistence.AttributeConverter;

@Converter(autoApply = true)
public class instantConverter implements AttributeConverter<Instant, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(Instant attribute) {
        return attribute == null ? null : Timestamp.from(attribute);
    }
    @Override
    public Instant convertToEntityAttribute(Timestamp dbData) {
        return dbData == null ? null : dbData.toInstant();
    }
}
