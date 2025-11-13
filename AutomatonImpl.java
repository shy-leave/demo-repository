import java.util.*;


public class AutomatonImpl implements Automaton {

    private final Map<Integer, Map<Character, Set<Integer>>> transitions;


    private final Set<Integer> startStates;
    private final Set<Integer> acceptStates;


    private Set<Integer> currentStates;

    public AutomatonImpl() {
        transitions = new HashMap<>();
        startStates = new HashSet<>();
        acceptStates = new HashSet<>();
        currentStates = new HashSet<>();
    }


    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
       
        transitions.computeIfAbsent(s, k -> new HashMap<>());
        if (is_start) startStates.add(s);
        if (is_accept) acceptStates.add(s);
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        transitions.computeIfAbsent(s_initial, k -> new HashMap<>());
        Map<Character, Set<Integer>> map = transitions.get(s_initial);
        map.computeIfAbsent(label, k -> new HashSet<>());
        map.get(label).add(s_final);

       
        transitions.computeIfAbsent(s_final, k -> new HashMap<>());
    }



    @Override
    public void reset() {

        currentStates = new HashSet<>(startStates);
    }

    @Override
    public void apply(char input) {
        Set<Integer> next = new HashSet<>();
        for (Integer s : currentStates) {
            Map<Character, Set<Integer>> map = transitions.get(s);
            if (map == null) continue;
            Set<Integer> dests = map.get(input);
            if (dests != null) {
                next.addAll(dests);
            }
        }
        currentStates = next;
    }

    @Override
    public boolean accepts() {
        for (Integer s : currentStates) {
            if (acceptStates.contains(s)) return true;
        }
        return false;
    }

    @Override
    public boolean hasTransitions(char label) {
        for (Integer s : currentStates) {
            Map<Character, Set<Integer>> map = transitions.get(s);
            if (map == null) continue;
            Set<Integer> dests = map.get(label);
            if (dests != null && !dests.isEmpty()) return true;
        }
        return false;
    }
}
