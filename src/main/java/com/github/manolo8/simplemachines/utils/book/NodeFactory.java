package com.github.manolo8.simplemachines.utils.book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NodeFactory {

    private int a;

    public List<Node> findNodes(String[] lines) {
        List<Node> nodes = new ArrayList<>();
        a = 0;
        while (a < lines.length) nodes.add(findNextNode(lines));
        return nodes;
    }

    public List<Node> findNodes(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

        List<String> lines = new ArrayList<>();

        String ln;
        while ((ln = bufferedReader.readLine()) != null) lines.add(ln);

        bufferedReader.close();

        return findNodes(lines.toArray(new String[0]));
    }

    private Node findNextNode(String[] source) {
        Node node = new Node();

        int begin = 0;
        int end = 0;
        boolean tag = true;

        StringBuilder builder = new StringBuilder();
        while (a < source.length) {
            String string = source[a];
            a++;
            if (string.length() > 0 && string.charAt(0) == '#') continue;
            if (string.equals("[end]")) {
                end = a - 1;
                break;
            }

            if (!tag) continue;

            int i = 0;
            while (string.length() > i) {
                char f = string.charAt(i);
                i++;
                if (f == '[') tag = false;
                else if (f == ']') {
                    String[] vl = builder.toString().split("-");
                    node.setKey(vl[0]);
                    node.setValue(vl[1]);
                    begin = a;
                    builder.setLength(0);
                    break;
                } else builder.append(f);
            }
        }
        String[] template = new String[end - begin];
        System.arraycopy(source, begin, template, 0, end - begin);
        node.setTemplate(template);
        return node;
    }
}
