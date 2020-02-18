package utils.xpath;

import java.util.ArrayList;

public class XPathBuilder {

    private StringBuilder result;
    private ArrayList<String> xPathNodeList;

    public static final String START_TAG = "//";
    public static final String AND_TAG = "/";

    public XPathBuilder() {
        this.result = new StringBuilder();
        this.xPathNodeList = new ArrayList<>();
    }

    public XPathBuilder(XPathNodeBuilder nodeBuilder) {
        this.result = new StringBuilder();
        this.xPathNodeList = new ArrayList<>();
        addXPathNode(nodeBuilder.getXPath());
    }

    public XPathBuilder(ArrayList<XPathNodeBuilder> nodeBuilders) {
        this.result = new StringBuilder();
        this.xPathNodeList = new ArrayList<>();
        for (XPathNodeBuilder nodeBuilder : nodeBuilders) {
            addXPathNode(nodeBuilder.getXPath());
        }
    }

    public void addXPathNode(String xPathNode) {
        this.xPathNodeList.add(xPathNode);
    }

    public String getXPath() {
        int count = xPathNodeList.size();
        for (int index = 0; index < count; index++) {
            if (index == 0) {
                result.append(START_TAG);
            } else {
                result.append(AND_TAG);
            }
            String xPAthNode = xPathNodeList.get(index);
            result.append(xPAthNode);
        }
        return result.toString();
    }

}
