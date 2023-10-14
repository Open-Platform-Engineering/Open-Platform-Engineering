package codes.showme.domain.platform;

import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TicketRepositoryImpl implements TicketRepository {


    @Override
    public long save(Ticket ticket) {
        Database instance = InstanceFactory.getInstance(Database.class);
        instance.save(ticket);
        return ticket.getId();
    }

}
