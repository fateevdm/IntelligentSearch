//package intelligentsearch.indexer;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.File;
//import java.util.*;
//
///**
// * User: Dmitry Fateev
// * Date: 06.03.13
// * Time: 0:31
// */
//public class LSA {
//    static double T = 0;
//
//    public static void main(String argv[]) {
//        double in[][];
//        try {
//            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            Document doc = builder.parse(new File("src\\input.xml"));
//            NodeList keywordsNodes = doc.getElementsByTagName("keyword");
//            NodeList paragraphsNodes = doc.getElementsByTagName("paragraph");
//            String keywords[] = new String[keywordsNodes.getLength()];
//            String paragraphs[] = new String[paragraphsNodes.getLength()];
//            in = new double[keywordsNodes.getLength()][paragraphsNodes.getLength()];
//            int i = 0, j = 0;
//            for (i = 0; i < keywords.length; i++) {
//                for (j = 0; j < paragraphs.length; j++) {
//                    keywords[i] = keywordsNodes.item(i).getFirstChild().getNodeValue().trim();
//                    paragraphs[j] = paragraphsNodes.item(j).getFirstChild().getNodeValue();
//                    in[i][j] = occurIn(paragraphs[j], keywords[i]);
//                }
//            }
//            for (i = 0; i < keywords.length; i++) {
//                System.out.println("Key word " + i + ":" + keywords[i]);
//            }
//            for (j = 0; j < paragraphs.length; j++) {
//                System.out.println("Paragraph " + j + ":" + paragraphs[j]);
//            }
//            int rank = new
//                    Integer(doc.getElementsByTagName("rank").item(0).getFirstChild().getNodeValue());
//            Matrix a = new Matrix(in);
//            Matrix b = spearman(a);
//            SingularValueDecomposition svd = new SingularValueDecomposition(a);
//            Matrix mask = svd.getS();
//            for (int k = rank; k < mask.getRowDimension(); k++) {
//                mask.set(k, k, 0);
//            }
//            Matrix c = svd.getU().times(mask).times(svd.getV().transpose());
//            Matrix d = spearman(c);
//            System.out.println("INITIAL MATRIX:");
//            a.print(4, 2);
//            System.out.println("INITIAL CORRELATIONS:");
//            b.print(4, 2);
//            System.out.println("MATRIX AFTER SVD (RANK=" + rank + "):");
//            c.print(4, 2);
//            System.out.println("RESULTING CORRELATIONS:");
//            d.print(4, 2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static int occurIn(String _in, String _sample) {
//        int result = 0;
//        String in = _in.toLowerCase();
//        String sample = _sample.toLowerCase();
//        StringTokenizer tk = new StringTokenizer(in);
//        while (tk.hasMoreTokens()) {
//            String word = tk.nextToken();
//            if (word.indexOf(sample) != -1) result++;
//        }
//        return result;
//    }
//
//    private static Matrix spearman(Matrix a) {
//        double vals[][] = a.transpose().getArrayCopy();
//        double result[][] = new double[vals.length][vals.length];
//        for (int i = 0; i < vals.length; i++) {
//            for (int j = 0; j < vals.length; j++) {
//                result[i][j] = spearman(vals[i], vals[j]);
//            }
//        }
//        return new Matrix(result);
//    }
//
//    private static double spearman(double a[], double b[]) {
//        double x = 0;
//        double Ta, Tb;
//        computeRanks(a);
//        Ta = T;
//        computeRanks(b);
//        Tb = T;
//        int N = a.length;
//        for (int i = 0; i < a.length; i++) {
//            double d = a[i] - b[i];
//            x += d * d;
//        }
//        double nom = 1 - 6 * (x + Ta + Tb) / (N * (N * N - 1));
//        double denom = Math.sqrt((1 - 12 * Ta / (N * (N * N - 1))) * (1 - 12 * Tb / (N * (N * N - 1))));
//        return nom / denom;
//    }
//
//    private static double[] computeRanks(double a[]) {
//        int i = 0;
//        int N = a.length;
//        Map<Integer, Double> a_map = new LinkedHashMap<Integer, Double>();
//        for (i = 0; i < N; i++) {
//            a_map.put(i, a[i]);
//        }
//        Object entries[] = a_map.entrySet().toArray();
//        Arrays.sort(entries, new Comparator() {
//            public int compare(Object a, Object b) {
//                if ((Double) (((Map.Entry) a).getValue()) >
//                        (Double) (((Map.Entry) b).getValue())) {
//                    return 1;
//                }
//                if (((Double) (((Map.Entry) a).getValue())).doubleValue() ==
//                        ((Double) (((Map.Entry) b).getValue())).doubleValue())
//                    return 0;
//                if ((Double) (((Map.Entry) a).getValue()) <
//                        (Double) (((Map.Entry) b).getValue())) {
//                    return -1;
//                }
//                return 0;
//            }
//        });
//        Map<Object, Double> rank = new LinkedHashMap<Object, Double>();
//        for (i = 0; i < N; i++) {
//            rank.put(((Map.Entry) entries[i]).getKey(), (double) (i + 1));
//        }
//        T = 0;
//        int from_index = -1;
//        for (i = 0; i < N - 1; i++) {
//            if (from_index < 0) {
//                if (((Double) ((Map.Entry) entries[i]).getValue()).doubleValue() == ((Double) ((Map.Entry)
//                        entries[i + 1]).getValue()).doubleValue()) {
//                    from_index = i;
//                }
//            } else if ((i == N - 1) || (((Double) ((Map.Entry) entries[i]).getValue()).doubleValue() !=
//                    ((Double) ((Map.Entry) entries[i + 1]).getValue()).doubleValue())) {
//                for (int k = from_index; k < (i + 1); k++) {
//                    rank.put(((Map.Entry) entries[k]).getKey(), 0.5 * (i + from_index) + 1);
//                }
//                double t = i - from_index + 1;
//                T += (t * t * t - t);
//                from_index = -1;
//            }
//        }
//        T = T / 12;
//        for (i = 0; i < N; i++) {
//            a[i] = rank.get(i);
//        }
//        return a;
//    }
//}