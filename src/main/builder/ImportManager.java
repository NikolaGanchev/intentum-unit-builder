package main.builder;

import java.util.ArrayList;

public class ImportManager {
    private StringBuilder imports;
    private ArrayList<String> importedComponents;

    public ImportManager() {
        this.imports = new StringBuilder();
        this.importedComponents = new ArrayList<>();
    }

    public void addStateImport() {
        if (!this.importedComponents.contains(ImportStrings.STATE_IDENTIFIER)) {
            this.imports.append(ImportStrings.USE_STATE_IMPORT);
            this.imports.append("\n");
            this.importedComponents.add(ImportStrings.STATE_IDENTIFIER);
        }
    }

    public void addTranslationImport() {
        if (!this.importedComponents.contains(ImportStrings.TRANSLATION_IDENTIFIER)) {
            this.imports.append(ImportStrings.USE_TRANSLATION_IMPORT);
            this.imports.append("\n");
            this.importedComponents.add(ImportStrings.TRANSLATION_IDENTIFIER);
        }
    }

    public void addEndButtonImport() {
        addComponentImport(ComponentStrings.END_BUTTON);
    }

    public void addExpectedImports() {
        addTranslationImport();
        addEndButtonImport();
    }

    public void addComponentImport(String component) {
        if (!importedComponents.contains(component)) {
            this.imports.append(getComponentImport(component));
            this.imports.append("\n");
            this.importedComponents.add(component);
        }
    }

    public String getImportState() {
        return imports.toString();
    }

    private String getComponentImport(String component) {
        return ImportStrings.COMPONENT_IMPORT.formatted(component, component);
    }
}
