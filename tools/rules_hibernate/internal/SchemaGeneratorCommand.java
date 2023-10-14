package tools.rules_ebean.src.main.java.internal;

import jakarta.persistence.Entity;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public class SchemaGeneratorCommand {

    public static void main(String... args) {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        try {
            Map<String, String> params = params(args);
            String outputFile = params.get("outputFile");
            String packageStr = params.get("packages");
            boolean haltOnError = Boolean.parseBoolean(params.get("haltOnError"));
            String delimiter = params.get("delimiter");
            String dialect = params.get("dialect");
            boolean format = Boolean.parseBoolean(params.get("format"));

            Map<String, Object> settings = new HashMap<>();
            settings.put("hibernate.dialect", dialect);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();
            MetadataSources metadata = new MetadataSources(serviceRegistry);
            metadata.addAnnotatedClasses(allClassOf(packageStr));

            SchemaExport schemaExport = new SchemaExport();
            schemaExport.setOutputFile(outputFile);
            schemaExport.setDelimiter(delimiter);
            schemaExport.setHaltOnError(haltOnError);
            schemaExport.setFormat(format);
            schemaExport.execute(EnumSet.of(TargetType.SCRIPT), SchemaExport.Action.CREATE, metadata.buildMetadata());
            System.exit(0);
        } catch (Exception e) {
            throw new RuntimeException("generate ddl error", e);
        }

    }

    private static Class<?>[] allClassOf(String packageStr) {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(packageStr.split(",")));
        Set<Class<?>> annotated =
                reflections.get(SubTypes.of(TypesAnnotated.with(Entity.class)).asClass());

        if (annotated == null || annotated.size() == 0) {
            throw new RuntimeException("generate ddl error, there's no entity in the packages:" + packageStr);
        }
        return annotated.toArray(new Class[annotated.size()]);
    }


    public static Map<String, String> params(String... args) {
        int length = args.length;

        if (length % 2 != 0) {
            throw new RuntimeException("params() must be called with key/value pairs");
        }

        Map<String, String> map = new HashMap<String, String>(length / 2);

        for (int i = 0; i < length; i += 2) {
            String key = args[i];
            String value = args[i + 1];
            System.out.println("key:" + key + ",value:" + value);
            map.put(key, value);
        }
        return map;
    }
}