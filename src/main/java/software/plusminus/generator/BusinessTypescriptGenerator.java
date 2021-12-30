package software.plusminus.generator;

import org.springframework.stereotype.Component;
import software.plusminus.business.model.BusinessEntity;
import software.plusminus.generator.typescript.TypescriptGenerator;
import software.plusminus.generator.typescript.model.TypescriptClass;

import java.util.ArrayList;

@Component
public class BusinessTypescriptGenerator extends TypescriptGenerator {

    @Override
    public TypescriptClass generateModel(Class<?> sourceClass) {
        TypescriptClass model = super.generateModel(sourceClass);
        if (sourceClass == BusinessEntity.class) {
            if (model.imports() == null) {
                model.imports(new ArrayList<>());
            }
            model.imports().remove("import { Classable } from './classable';");
            model.imports().remove("import { Jsog } from './jsog';");
            model.imports().add("import { UUID } from 'angular2-uuid';");

            model.title(
                    model.title().replace(" implements Classable, Jsog", "")
            );

            model.fields().add(0, "class: string;");
            
            model.constructor("  this.class = this.constructor.name;\n"
                    + "    this.uuid = UUID.UUID();\n"
                    + "    this.modifiedTime = new Date();"
            );
        }
        return model;
    }
}
