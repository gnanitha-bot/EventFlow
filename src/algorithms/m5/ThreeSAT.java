package algorithms.m5;

import java.util.*;

/**
 * 3-SAT Solver using DPLL (Davis-Putnam-Logemann-Loveland) backtracking search.
 *
 * Each clause contains exactly 3 literals.
 * A literal is a Boolean variable (positive) or its negation (negative).
 * The algorithm systematically assigns TRUE/FALSE to variables and backtracks on contradiction.
 *
 * Returns:
 * - SAT with a satisfying variable assignment, or
 * - UNSAT if no satisfying assignment exists.
 */
public class ThreeSAT {

    /**
     * Represents a single literal: a variable ID and whether it is negated.
     */
    public static class Literal {
        public final int varId;    // 1-based variable index
        public final boolean negated;

        public Literal(int varId, boolean negated) {
            this.varId = varId;
            this.negated = negated;
        }

        /** Evaluates this literal under the given assignment (1=TRUE, 0=FALSE, -1=unassigned). */
        public boolean evaluate(int[] assignment) {
            int val = assignment[varId];
            if (val == -1) return false; // unassigned → not satisfied by this literal
            boolean varTrue = (val == 1);
            return negated ? !varTrue : varTrue;
        }

        @Override
        public String toString() {
            return (negated ? "¬" : "") + "x" + varId;
        }
    }

    /**
     * Represents one clause of exactly 3 literals connected by OR.
     */
    public static class Clause {
        public final Literal[] literals;

        public Clause(Literal l1, Literal l2, Literal l3) {
            this.literals = new Literal[]{l1, l2, l3};
        }

        /**
         * Returns true if at least one literal is satisfied under the assignment.
         * Returns false only if all literals evaluate to FALSE (no unassigned).
         */
        public boolean isSatisfied(int[] assignment) {
            for (Literal lit : literals) {
                if (lit.evaluate(assignment)) return true;
            }
            return false;
        }

        /**
         * Returns true if every literal in this clause is definitively FALSE
         * (all assigned and all false) — i.e., the clause is a hard conflict.
         */
        public boolean isViolated(int[] assignment) {
            for (Literal lit : literals) {
                int val = assignment[lit.varId];
                if (val == -1) return false; // unassigned: not yet violated
                boolean varTrue = (val == 1);
                boolean litTrue = lit.negated ? !varTrue : varTrue;
                if (litTrue) return false;
            }
            return true; // all 3 literals are false
        }

        @Override
        public String toString() {
            return "(" + literals[0] + " ∨ " + literals[1] + " ∨ " + literals[2] + ")";
        }
    }

    /**
     * Container for the 3-SAT result.
     */
    public static class SATResult {
        public final boolean satisfiable;
        public final int[] assignment;   // 1=TRUE, 0=FALSE, index is 1-based variable ID
        public final int numVariables;
        public final int numClauses;
        public final int statesExplored;

        public SATResult(boolean satisfiable, int[] assignment, int numVariables, int numClauses, int statesExplored) {
            this.satisfiable = satisfiable;
            this.assignment = assignment;
            this.numVariables = numVariables;
            this.numClauses = numClauses;
            this.statesExplored = statesExplored;
        }
    }

    private final int numVars;
    private final List<Clause> clauses;
    private int statesExplored = 0;

    public ThreeSAT(int numVars) {
        this.numVars = numVars;
        this.clauses = new ArrayList<>();
    }

    /** Add a clause of exactly 3 literals. */
    public void addClause(Literal l1, Literal l2, Literal l3) {
        clauses.add(new Clause(l1, l2, l3));
    }

    /** Convenience method for adding clauses by variable ID and sign (positive=true, negative=false). */
    public void addClause(int v1, boolean neg1, int v2, boolean neg2, int v3, boolean neg3) {
        addClause(new Literal(v1, neg1), new Literal(v2, neg2), new Literal(v3, neg3));
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    /** Solve the 3-SAT instance using DPLL backtracking. */
    public SATResult solve() {
        statesExplored = 0;
        int[] assignment = new int[numVars + 1];
        Arrays.fill(assignment, -1); // -1 = unassigned

        boolean sat = dpll(assignment, 1);
        return new SATResult(sat, sat ? assignment : null, numVars, clauses.size(), statesExplored);
    }

    /**
     * DPLL Recursive Backtracking.
     * Tries assigning variable `varIdx` as TRUE then FALSE, backtracking on contradiction.
     */
    private boolean dpll(int[] assignment, int varIdx) {
        statesExplored++;

        // Check for any already-violated clauses
        for (Clause c : clauses) {
            if (c.isViolated(assignment)) return false;
        }

        // Check if all clauses satisfied
        boolean allSat = true;
        for (Clause c : clauses) {
            if (!c.isSatisfied(assignment)) { allSat = false; break; }
        }
        if (allSat) return true;

        // All variables assigned but not all satisfied → contradiction
        if (varIdx > numVars) return false;

        // Try TRUE
        assignment[varIdx] = 1;
        if (dpll(assignment, varIdx + 1)) return true;

        // Try FALSE
        assignment[varIdx] = 0;
        if (dpll(assignment, varIdx + 1)) return true;

        // Backtrack
        assignment[varIdx] = -1;
        return false;
    }
}
