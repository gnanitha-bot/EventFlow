// Standalone test and timing harness for the EventFlow algorithm modules (M1-M6).
// Run with run_tests.bat (Windows) or run_tests.sh (Linux/macOS). Runs twice; read the second (warm) timings.
import algorithms.m1.*; import algorithms.m2.*; import algorithms.m3.*; import algorithms.m4.*; import algorithms.m5.*; import algorithms.m6.*;
import java.util.*;
public class EventFlowTests {
  static int pass=0, fail=0;
  static void check(String n, boolean ok){ if(ok) pass++; else fail++; System.out.println((ok?"PASS ":"FAIL ")+n); }
  static String rnd(int n, long seed){ Random r=new Random(seed); StringBuilder sb=new StringBuilder(); for(int i=0;i<n;i++) sb.append((char)('a'+r.nextInt(4))); return sb.toString(); }
  static double ms(long t){ return t/1e6; }
  public static void main(String[] a) throws Exception { run(); System.out.println("=== SECOND (warm) PASS ==="); run(); }
  static void run() throws Exception {
    // correctness
    check("KMP finds all", KMP.search("abababa","aba").equals(Arrays.asList(0,2,4)));
    check("KMP missing", KMP.search("hackathon","xyz").isEmpty());
    check("RabinKarp == KMP (random)", RabinKarp.search(rnd(5000,1),"abca").equals(KMP.search(rnd(5000,1),"abca")));
    int[] sa=SuffixArray.buildSuffixArray("banana"); check("SuffixArray banana", Arrays.equals(sa,new int[]{5,3,1,0,4,2}));
    int[] lcp=LCP.buildLCP("banana",sa); check("LCP banana", lcp[0]==1&&lcp[1]==3&&lcp[2]==0);
    check("LCS 'xabcdy','zabcdw'", LCP.findLongestCommonSubstring("xabcdy","zabcdw").longestCommonSubstring.equals("abcd"));
    check("Levenshtein kitten/sitting=3", LevenshteinDistance.compute("kitten","sitting").distance==3);
    check("Levenshtein Fest/Fset=2", LevenshteinDistance.compute("Fest","Fset").distance==2);
    Dinic d=new Dinic(4); d.addEdge(0,1,10); d.addEdge(0,2,5); d.addEdge(1,3,7); d.addEdge(2,3,8); d.addEdge(1,2,3);
    int mf=-1; for(var m: Dinic.class.getMethods()) if(m.getName().toLowerCase().contains("flow")&&m.getParameterCount()==2){ mf=(int)m.invoke(d,0,3); }
    check("Dinic max flow = 15 (got "+mf+")", mf==15);
    BipartiteMatching bm=new BipartiteMatching(3,3); bm.addEdge(0,0); bm.addEdge(0,1); bm.addEdge(1,0); bm.addEdge(2,1); bm.addEdge(2,2);
    check("Matching = 3", bm.computeMatching().maxMatches==3);
    VertexCoverApproximation vc=new VertexCoverApproximation(5); vc.addConflictEdge(0,1,"a"); vc.addConflictEdge(1,2,"b"); vc.addConflictEdge(3,4,"c");
    var r=vc.computeVertexCover(); check("VertexCover valid & <=2*OPT(2)", r.isCoverValid()&&r.coverSize<=4);
    double[] arr=new double[2000]; Random rr=new Random(7); for(int i=0;i<arr.length;i++) arr[i]=rr.nextDouble()*1000; double[] c=arr.clone(); RandomizedQuickSort.sort(arr,0,arr.length-1); Arrays.sort(c);
    check("RandQuickSort == Arrays.sort", Arrays.equals(arr,c));
    List<String> st=new ArrayList<>(); for(int i=0;i<1000;i++) st.add("R"+i);
    var s=ReservoirSampling.sample(st,10); check("Reservoir size 10 & distinct", s.sample.size()==10 && new HashSet<>(s.sample).size()==10);
    System.out.println("TESTS "+pass+" passed, "+fail+" failed");
    // timing (median of 5 after warmup)
    System.out.println("--- timing ms");
    for(int n: new int[]{10_000,100_000,1_000_000}){ String t=rnd(n,3); String p="abcabd";
      long best1=Long.MAX_VALUE,best2=Long.MAX_VALUE; for(int k=0;k<6;k++){ long x=System.nanoTime(); KMP.search(t,p); best1=Math.min(best1,System.nanoTime()-x); x=System.nanoTime(); RabinKarp.search(t,p); best2=Math.min(best2,System.nanoTime()-x);} 
      System.out.printf("M1 n=%d KMP=%.3f RK=%.3f%n",n,ms(best1),ms(best2)); }
    for(int n: new int[]{1000,5000,20000}){ String t=rnd(n,5); long b=Long.MAX_VALUE; for(int k=0;k<4;k++){ long x=System.nanoTime(); LCP.buildLCP(t,SuffixArray.buildSuffixArray(t)); b=Math.min(b,System.nanoTime()-x);} System.out.printf("M2 SA+LCP n=%d %.3f%n",n,ms(b)); }
    for(int n: new int[]{200,1000,2000}){ String x1=rnd(n,8), x2=rnd(n,9); long b=Long.MAX_VALUE; for(int k=0;k<4;k++){ long x=System.nanoTime(); LevenshteinDistance.compute(x1,x2); b=Math.min(b,System.nanoTime()-x);} System.out.printf("M3 Lev %dx%d %.3f%n",n,n,ms(b)); }
    for(int n: new int[]{10,15,20}){ List<model.Activity> acts=new ArrayList<>(); Random q=new Random(2); for(int i=0;i<n;i++) acts.add(new model.Activity("A"+i,"EVT","act"+i,"10:00","11:00","Spk","Hall",30+q.nextInt(60),1+q.nextInt(10)));
      long b=Long.MAX_VALUE; for(int k=0;k<3;k++){ long x=System.nanoTime(); BitmaskDP.optimize(acts,240); b=Math.min(b,System.nanoTime()-x);} System.out.printf("M3 Bitmask n=%d %.3f%n",n,ms(b)); }
    for(int n: new int[]{100_000,500_000,1_000_000}){ double[] z=new double[n]; Random q=new Random(11); for(int i=0;i<n;i++) z[i]=q.nextDouble(); double[] z2=z.clone(); long x=System.nanoTime(); RandomizedQuickSort.sort(z,0,n-1); long t1=System.nanoTime()-x; x=System.nanoTime(); Arrays.sort(z2); long t2=System.nanoTime()-x; System.out.printf("M6 sort n=%d RQS=%.1f Arrays.sort=%.1f%n",n,ms(t1),ms(t2)); }
    for(int n: new int[]{100_000,1_000_000}){ List<String> z=new ArrayList<>(); for(int i=0;i<n;i++) z.add("R"+i); long x=System.nanoTime(); ReservoirSampling.sample(z,50); System.out.printf("M6 reservoir n=%d %.2f%n",n,ms(System.nanoTime()-x)); }
  }
}
