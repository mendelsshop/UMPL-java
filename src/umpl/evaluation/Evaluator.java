package umpl.evaluation;

import java.util.Optional;

import umpl.ast.Ast;

public class Evaluator {
    Scope currrentScope;

    public Evaluator() {
        // initialize the root scope
        currrentScope = new Scope();
    }

    /**
     * @param name
     * @param value
     * @return true if there exists a variable with the given name
     * @note even if there is a variable the value will be replaced with the new
     *       value
     */
    public boolean insert(String name, Ast value) {
        return currrentScope.insert(name, value);
    }

    /**
     * @param name
     * @param value
     * @return true is there a was already added variable with the given name
     */
    public boolean set(String name, Ast value) {
        return currrentScope.set(name, value);
    }

    /**
     * @param name
     * @return Optional.of(value) if there is a variable with the given name
     *         otherwise Optional.empty()
     */
    public Optional<Ast> get(String name) {
        return currrentScope.get(name);
    }

    /**
     * @param name
     * @return new child scope with the given name
     */
    public void new_scope(String name) {
        currrentScope = currrentScope.new_child(name);
    }

    /**
     * @return new child scope with unique name lambdan
     */
    public void new_lambda() {
        currrentScope = currrentScope.new_lambda();
    }

    /**
     * @return true if the current scope has a parent
     */
    public boolean pop_scope() {
        Optional<Scope> parentScope = currrentScope.getParent();
        if (parentScope.isPresent()) {
            currrentScope = parentScope.get();
            return true;
        }
        return false;
    }
}
