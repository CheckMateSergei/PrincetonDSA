import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;

public class BaseballElimination {
    private int maxWins;
    private final int[] w, r, l, map;
    private final int[][] g;
    private final LinkedList<String> teams;
    private boolean[] trivElim;
    private LinkedList<String> cut;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In input = new In(filename);
        int n = input.readInt();
        input.readLine();
        w = new int[n];
        r = new int[n];
        l = new int[n];
        map = new int[n];
        g = new int[n][n];
        teams = new LinkedList<>();
        trivElim = new boolean[n];

        int count = 0;
        while (!input.isEmpty()) {
            String s = input.readLine();
            String[] fields = s.trim().split("\\s+");
            teams.addLast(fields[0]);
            w[count] = Integer.parseInt(fields[1]);
            l[count] = Integer.parseInt(fields[2]);
            r[count] = Integer.parseInt(fields[3]);
            
            for (int i = 0; i < n; i++) {
                g[count][i] = Integer.parseInt(fields[fields.length - n + i]);
            }
            count++;
        }
        // check for trivial elimination
        maxWins = 0;
        for (String s : teams) {
            int i = teams.indexOf(s);
            if (maxWins < w[i]) maxWins = w[i];
        }
        for (String s : teams) {
            int i = teams.indexOf(s);
            if (w[i] + r[i] < maxWins) trivElim[i] = true;
        }
    }

    private boolean buildNetwork(int x) {
        // formula for nC2 + a vertex for each team other than x and s and t
        int n = numberOfTeams() - 1;
        int numGames = n * (n - 1) / 2;
        int v = numGames + n + 2;
        FlowNetwork flowNet = new FlowNetwork(v);
        // connect team vertices to t
        int count = 0;
        for (int i = 0; i < n + 1; i++) {
            if (i == x) continue;
            double cap = w[x] + r[x] - w[i];
            if (cap < 0) cap = 0;
            flowNet.addEdge(new FlowEdge(count + 1, v - 1, cap));
            map[i] = count + 1;
            count++;
        }
        // connect s to game vertices
        count = 0;
        for (int i = 0; i < n + 1; i++) {
            for (int j = i + 1; j < n + 1; j++) {
                if (i == x || j == x) continue;
                flowNet.addEdge(new FlowEdge(0, n + count + 1, g[i][j]));
                flowNet.addEdge(new FlowEdge(n + count + 1, map[i], Double.POSITIVE_INFINITY));
                flowNet.addEdge(new FlowEdge(n + count + 1, map[j], Double.POSITIVE_INFINITY));
                count++;
            }
        }

        FordFulkerson ff = new FordFulkerson(flowNet, 0, v - 1);
        double cap = 0;
        for (FlowEdge e : flowNet.adj(0)) cap += e.capacity();
        if (cap == ff.value()) return false;
        else {
            cut = new LinkedList<>();
            for (int i = 0; i < n + 1; i++) {
                if (i == x) continue;
                if (ff.inCut(map[i])) cut.addLast(teams.get(i));
            }
            return true;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return w.length;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null) throw new IllegalArgumentException();
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException();
        return w[index];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null) throw new IllegalArgumentException();
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException();
        return l[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null) throw new IllegalArgumentException();
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException();
        return r[index];
    }
    
    // number of remaining games between team1 and team2 
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null) throw new IllegalArgumentException();
        int index1 = teams.indexOf(team1);
        int index2 = teams.indexOf(team2);
        if (index1 < 0 || index2 < 0) throw new IllegalArgumentException();
        return g[index1][index2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null) throw new IllegalArgumentException();
        // check for trivial elimination
        int x = teams.indexOf(team);
        if (x < 0) throw new IllegalArgumentException();
        if (trivElim[x]) return true;
        
        // otherwise build flow network
        return buildNetwork(x);
    }
   
    // subset R of teams that eliminates given team; null if not eliminated 
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) throw new IllegalArgumentException();
        int x = teams.indexOf(team);
        if (x < 0) throw new IllegalArgumentException();
        if (trivElim[x]) {
            cut = new LinkedList<>();
            for (int i = 0; i < w.length; i++) {
                if (w[i] == maxWins) {
                    cut.addLast(teams.get(i));
                    return cut;
                }
            }
        }
        if (buildNetwork(x)) return cut;
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
         
         
        System.out.printf("Princeton is eliminated? %b\n", division.isEliminated("Princeton"));
        for (String s : division.certificateOfElimination("Princeton")) {
            System.out.printf("%s\n", s);
        }
       
       /* 
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
       */ 
    }
}
