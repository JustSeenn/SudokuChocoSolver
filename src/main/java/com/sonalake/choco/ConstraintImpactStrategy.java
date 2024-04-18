package com.sonalake.choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperatorFactory;
import org.chocosolver.solver.search.strategy.decision.Decision;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.variables.IntVar;


import org.chocosolver.solver.constraints.Constraint;

import org.chocosolver.solver.variables.Variable;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;

public class ConstraintImpactStrategy extends AbstractStrategy<IntVar> {
    private Model model;

    public ConstraintImpactStrategy(Model model, IntVar[] vars) {
        super(vars);
        this.model = model;
    }

    @Override
    public boolean init() {
        // Initialize the strategy
        return true;
    }

    @Override
    public Decision<IntVar> getDecision() {
        // Find the variable with the highest impact
        IntVar varWithHighestImpact = null;
        double highestImpact = Double.NEGATIVE_INFINITY;

        for (IntVar var : vars) {
            if (!var.isInstantiated()) {
                double impact = calculateImpact(var);
                if (impact > highestImpact) {
                    highestImpact = impact;
                    varWithHighestImpact = var;
                }
            }
        }

        // Make a decision on the variable with the highest impact
        if (varWithHighestImpact != null) {
            int value = varWithHighestImpact.getLB();
            return model.getSolver().getDecisionPath().makeIntDecision(varWithHighestImpact, DecisionOperatorFactory.makeIntEq(), value);
        } else {
            return null; // No variable to make decision on
        }
    }

    private double calculateImpact(IntVar var) {
        // Estimate the impact of assigning a value to the variable
        double impact = 0.0;
        for (Constraint constraint : model.getCstrs()) {
            for (int i = 0; i < constraint.getPropagators().length; i++) {
                if (constraint.getPropagator(i).isEntailed() != ESat.TRUE) {
                    for (int j = 0; j < constraint.getPropagator(i).getNbVars(); j++) {
                        Variable[] relatedVars = constraint.getPropagator(i).getVars();
                        if (!containsVariable(relatedVars, var)) {
                            int propagationConditions = constraint.getPropagator(i).getPropagationConditions(j);
                            if ((propagationConditions & IntEventType.INSTANTIATE.getMask()) != 0 ||
                                (propagationConditions & IntEventType.INCLOW.getMask()) != 0 ||
                                (propagationConditions & IntEventType.DECUPP.getMask()) != 0) {
                                // We consider the propagator for INSTANTIATE, INCLOW, or DECUPP events
                                // Add its impact to the total impact
                                impact += constraint.getPropagator(i).getPropagationConditions(j);
                            }
                        }
                    }
                }
            }
        }
        return impact;
    }
    
    
    private boolean containsVariable(Variable[] variables, Variable var) {
        for (Variable v : variables) {
            if (v == var) {
                return true;
            }
        }
        return false;
    }
}