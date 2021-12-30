package software.plusminus.generator;

import org.springframework.stereotype.Component;
import software.plusminus.business.model.BusinessEntity;
import software.plusminus.business.model.BusinessObject;
import software.plusminus.business.model.BusinessType;
import software.plusminus.generator.typescript.TypescriptGenerator;
import software.plusminus.generator.typescript.model.TypescriptClass;

import java.util.ArrayList;

@Component
public class BusinessTypescriptGenerator extends TypescriptGenerator {

    @Override
    public TypescriptClass generateModel(Class<?> sourceClass) {
        TypescriptClass model = super.generateModel(sourceClass);
        if (sourceClass == BusinessObject.class) {
            model.title(
                    model.title().replace(" implements BusinessType, Classable, Jsog", "")
            );
            model.imports().remove("import { Classable } from './classable';");
            model.imports().remove("import { Jsog } from './jsog';");
            model.imports().remove("import { BusinessType } from './business-type';");
            model.fields().add("class: string;");
            model.constructor("  this.class = this.constructor.name;");
        }
        if (sourceClass == BusinessEntity.class) {
            if (model.imports() == null) {
                model.imports(new ArrayList<>());
            }
            model.imports().add("import { UUID } from 'angular2-uuid';");
            model.constructor("  super();\n"
                    + "    this.uuid = UUID.UUID();\n"
                    + "    this.modifiedTime = new Date();");
        }
        if (sourceClass.isEnum()) {
            model.imports().remove("import { BusinessType } from '../business-type';");
            model.title(
                    model.title().replace(" implements BusinessType", "")
            );
        }
        return model;
    }

    @Override
    public boolean supports(Class<?> sourceClass) {
        return sourceClass != BusinessType.class;
    }
}
