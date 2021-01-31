package ch.sebastianhaeni.prophector;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class ProphectorNamingStrategy extends SpringPhysicalNamingStrategy {

    public static final String TABLE_NAME_PREFIX = "pro_";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        var newIdentifier = new Identifier(TABLE_NAME_PREFIX + name.getText(), name.isQuoted());
        return super.toPhysicalTableName(newIdentifier, context);
    }

}
