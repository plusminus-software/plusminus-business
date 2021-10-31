package software.plusminus.generator;

import org.springframework.stereotype.Component;
import software.plusminus.generator.typescript.TypescriptGenerator;
import software.plusminus.generator.typescript.model.TypescriptClass;

import java.util.ArrayList;

@Component
public class BusinessTypescriptGenerator extends TypescriptGenerator {

    @Override
    public TypescriptClass generateModel(Class<?> sourceClass) {
        TypescriptClass model = super.generateModel(sourceClass);
        if (model.imports() == null) {
            model.imports(new ArrayList<>());
        }
        model.imports().add("import { UUID } from 'angular2-uuid';");
        
        model.constructor("  super();\n"
                + "  this.modifiedTime = new Date();\n"
                + "  this.uuid = UUID.UUID();\n");
        return model;
    }
}