package umpl.evaluation;

import java.util.HashMap;
import java.util.Optional;

import umpl.ast.Ast;

public class Scope {
    String name;
    HashMap<String, Ast> variables;
    Optional<Scope> parent;

    public Optional<Scope> getParent() {
        return parent;
    }

    private static int lambdaCounter = 0;

    /**
     * creates the root scope
     */
    public Scope() {
        name = "root";
        parent = Optional.empty();

        // TODO: insert primitives functions/variables
        variables = new HashMap<String, Ast>();
    }

    private Scope(String name, Scope parent) {
        this.name = name;
        this.parent = Optional.ofNullable(parent);
        variables = new HashMap<String, Ast>();
    }

    /**
     * @param name
     * @return new child scope with the given name
     */
    public Scope new_child(String name) {
        return new Scope(name, this);
    }

    /**
     * @return new child scope with unique name lambdan
     */
    public Scope new_lambda() {
        return new Scope("lambda" + lambdaCounter++, this);
    }

    /**
     * @param name
     * @param value
     * @return true if there exists a variable with the given name
     * @note even if there is a variable the value will be replaced with the new
     *       value
     */
    public boolean insert(String name, Ast value) {
        if (variables.put(name, value) != null)
            return true;
        return false;
    }

    /**
     * @param name
     * @param value
     * @return true is there a was already added variable with the given name
     */
    public boolean set(String name, Ast value) {
        if (variables.containsKey(name)) {
            variables.put(name, value);
            return true;
        } else if (parent.isPresent()) {
            return parent.get().insert(name, value);
        } else {
            return false;
        }
    }

    /**
     * @param name
     * @return Optional.of(value) if there is a variable with the given name
     *         otherwise Optional.empty()
     */
    public Optional<Ast> get(String name) {
        Ast value = variables.get(name);
        if (value != null) {
            return Optional.of(value);
        } else if (parent.isPresent()) {
            return parent.get().get(name);
        } else {
            return Optional.empty();
        }
    }

}
