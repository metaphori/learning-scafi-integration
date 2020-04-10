package learning.scafi.integration;

import acprograms.ScafiContext;
import acprograms.ScafiDSL;
import acprograms.ScafiExport;

import java.util.HashMap;

public class ScafiOnJava {

    public static void main(String[] args){
        GradientProgram g = new GradientProgram();

        ScafiContext c11 = new ScafiContext(1,
                new HashMap<>(),
                new HashMap<>(){{ put("source",true); }},
                new HashMap<>(){{ put("NBR_RANGE", new HashMap<>(){{ put(1, 0.0); put(2, 0.77); }}); }});
        ScafiExport e11 = g.executionRound(c11);
        System.out.println("Export 1-1: " + e11.root());

        ScafiContext c21 = new ScafiContext(2,
                new HashMap<>(){{ put(1, e11); }},
                new HashMap<>(){{ put("source",false); }},
                new HashMap<>(){{ put("NBR_RANGE", new HashMap<>(){{ put(1, 0.77); put(2, 0.0); }}); }});
        ScafiExport e21 = g.executionRound(c21);
        System.out.println("Export 2-1: " + e21.root());

        ScafiContext c12 = new ScafiContext(1,
                new HashMap<>(){{ put(1, e11); put(2, e21); }},
                new HashMap<>(){{ put("source",true); }},
                new HashMap<>(){{ put("NBR_RANGE", new HashMap<>(){{ put(1, 0.0); put(2, 0.77); }}); }});
        ScafiExport e12 = g.executionRound(c12);
        System.out.println("Export 1-2: " + e12.root());

        ScafiContext c22 = new ScafiContext(2,
                new HashMap<>(){{ put(1, e12); put(2,e21); }},
                new HashMap<>(){{ put("source",false); }},
                new HashMap<>(){{ put("NBR_RANGE", new HashMap<>(){{ put(1, 0.77); put(2, 0.0); }}); }});
        ScafiExport e22 = g.executionRound(c22);
        System.out.println("Export 2-2: " + e22.root() + " \n\n---\n" + c22 + "\n" + e22);
    }

    public static class GradientProgram extends ScafiDSL<Double> {
        @Override
        public Double main() {
            return REP(Double.POSITIVE_INFINITY, (v) ->
                MUX(sense("source"), 0.0,
                   FOLDHOODplus(Double.POSITIVE_INFINITY, (x, y) -> Double.min(x,y),
                           () -> this.<Double>NBR(() -> v) + this.<Double>NBRVAR("NBR_RANGE") )
                )
            );
        }
    }

    public static class CounterProgram extends ScafiDSL<Double> {
        @Override
        public Double main() {
            return REP(0.0, (v) -> v+1);
        }
    }

    public static class BasicProgram extends ScafiDSL<String> {
        @Override
        public String main() {
            return "hello scafi";
        }
    }
}