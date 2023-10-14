package codes.showme.domain.observability;



import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.ioc.InstanceProvider;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class AbstractIntegrationTest {
    public static final InstanceProvider instanceProvider = Mockito.mock(InstanceProvider.class);
    public static InstanceProvider setupInstanceProvider(){

        InstanceFactory.setInstanceProvider(instanceProvider);
        return instanceProvider;
    }

}
