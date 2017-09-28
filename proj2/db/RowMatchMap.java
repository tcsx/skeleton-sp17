// package db;

// import java.util.HashMap;
// import java.util.LinkedList;
// import java.util.List;

// public class RowMatchMap extends HashMap<Integer, List<Integer>> {

//     public RowMatchMap(Table t1, Table t2, List<String> common) {
        
//         ColumnMatchMap cmm = new ColumnMatchMap(t1, t2);
//         if (cmm.size() == 0) {
//             for (int i = 0; i < t1.rowNum(); i++) {
//                 List<Integer> value = new LinkedList<Integer>();
//                 for (int j = 0; i < t2.rowNum(); i++) {
//                     value.add(j);
//                 }
//                 this.put(i, value);
//             }
//         } else {
//             for (int i = 0; i < t1.rowNum(); i++) {
//                 List<Integer> value = null;
//                 for (ColumnMatchMap.Entry<Integer, Integer> entry : cmm.entrySet()) {
//                     Table.Column col1 = t1.getColumn(entry.getKey());
//                     Table.Column col2 = t2.getColumn(entry.getValue());
//                     if (value == null) {
//                         value = col2.indexesOf(col1.get(i));
//                     } else {
//                         value = intersects(value, col2.indexesOf(col1.get(i)));
//                     }
//                     if (value.size() == 0) {
//                         break;
//                     }
//                 }
//                 if (value.size() != 0) {
//                     this.put(i, value);
//                 }
//             }
//         }
//     }

//     public static List<Integer> intersects(List<Integer> a1, List<Integer> a2) {
//         LinkedList<Integer> result = new LinkedList<Integer>();
//         for (Integer i : a1) {
//             if (a2.contains(i)) {
//                 result.add(i);
//             }
//         }
//         return result;
//     }
    
// }