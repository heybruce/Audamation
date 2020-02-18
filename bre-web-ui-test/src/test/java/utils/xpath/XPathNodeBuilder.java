package utils.xpath;

import java.util.ArrayList;

public class XPathNodeBuilder {
    public static final String ATTRIBUTE_INDEX = "index";
    public static final String ATTRIBUTE_TEXT = "text";
    public static final String ATTRIBUTE_RESOURCE_ID = "resource-id";
    public static final String ATTRIBUTE_PACKAGE = "package";
    public static final String ATTRIBUTE_CONTENT_DESC = "content-desc";
    public static final String ATTRIBUTE_BOUNDS = "bounds";
    public static final String ATTRIBUTE_CLASS = "class";

    public static final String CLASS_ALL = "*";
    public static final String CLASS_LINEAR_LAYOUT = "android.widget.LinearLayout";
    public static final String CLASS_RELATIVE_LAYOUT = "android.widget.RelativeLayout";
    public static final String CLASS_FRAME_LAYOUT = "android.widget.FrameLayout";
    public static final String CLASS_LIST_VIEW = "android.widget.ListView";
    public static final String CLASS_IMAGE_VIEW = "android.widget.ImageView";
    public static final String CLASS_TEXT_VIEW = "android.widget.TextView";
    public static final String CLASS_IMAGE_BUTTON = "android.widget.ImageButton";

    public static final String PAIR_PREFIX = "@";
    public static final String EQUAL_TAG = "=";
    public static final String NOT_EQUAL_TAG = "!=";
    public static final String CONTAIN_TAG = "contains";
    public static final String NOT_CONTAIN_TAG = "not";
    public static final String VALUE_QUOTE = "'";
    public static final String LEFT_PARENTHESIS = "(";
    public static final String RIGHT_PARENTHESIS = ")";
    public static final String COMMA_TAG = ",";
    public static final String LEFT_SQUARE_BRAKET = "[";
    public static final String RIGHT_SQUARE_BRAKET = "]";
    public static final String AND = " and ";

    // find front node of filtered node - same layout level
    public static final String PRE_FILTER_PRECEDING_SIBLING = "preceding-sibling::";
    // find back node of filtered node - same layout level
    public static final String PRE_FILTER_FOLLOWING_SIBLING = "following-sibling::";
    // find parent node of filtered node - outer layout level
    public static final String PRE_FILTER_PARENT = "parent::";

    public static final int NO_INDEX = 99999;


    private StringBuilder result;
    private String prefixFilter;
    private ArrayList<String> filterPairList;

    public XPathNodeBuilder() {
        this.result = new StringBuilder();
        this.filterPairList = new ArrayList<>();
        this.result.append(addStartingView(CLASS_ALL));
    }

    public XPathNodeBuilder(String startingView) {
        this.result = new StringBuilder();
        this.filterPairList = new ArrayList<>();
        this.result.append(addStartingView(startingView));
    }

    public XPathNodeBuilder(String startingView, String prefixFilter, int index, String key, String value) {
        this.result = new StringBuilder();
        this.filterPairList = new ArrayList<>();
        this.result.append(addStartingView(startingView));
        addPrefixFilter(prefixFilter);
        addIndex(index);
        addEqualPair(true, key, value);
    }

    public XPathNodeBuilder(String startingView, String prefixFilter, String key, String value) {
        this.result = new StringBuilder();
        this.filterPairList = new ArrayList<>();
        this.result.append(addStartingView(startingView));
        addPrefixFilter(prefixFilter);
        addEqualPair(true, key, value);
    }

    public XPathNodeBuilder(String startingView, String key, String value) {
        this.result = new StringBuilder();
        this.filterPairList = new ArrayList<>();
        this.result.append(addStartingView(startingView));
        addEqualPair(true, key, value);
    }


    private String addStartingView(String startingView) {
        StringBuilder sb = new StringBuilder();
        sb.append(startingView);
        return sb.toString();
    }

    public void addPrefixFilter(String preFilterKey) {
        this.prefixFilter = preFilterKey;
    }

    public void addIndex(int index) {
        addEqualPair(true, ATTRIBUTE_INDEX, Integer.toString(index));
    }

    public void addEqualPair(boolean equal, String attrKey, String attrValue) {
        /*
         * equal        : true
         * attrKey      : resource-id
         * attrValue    : com.xueqiu.android:id/action_search
         * keyValuePair : @resource-id='com.xueqiu.android:id/action_search'
         */
        StringBuilder sb = new StringBuilder();
        String equalTag = equal ? EQUAL_TAG : NOT_EQUAL_TAG;
        String keyValuePair = sb.append(attrKey).append(equalTag).append(VALUE_QUOTE).append(attrValue).append(VALUE_QUOTE).toString();
        keyValuePair = PAIR_PREFIX + keyValuePair;
        System.out.print("addEqualPair:" + keyValuePair);
        filterPairList.add(keyValuePair);
    }

    public void addContainsPair(boolean contains, String attrKey, String attrValue) {
        /*
         * attrKey      : resource-id
         * attrValue    : com.xueqiu.android:id/action_search
         * keyValuePair : contains(@resource-id,'com.xueqiu.android:id/action_search')
         */
        StringBuilder sb = new StringBuilder();
        String keyValuePair = sb.append(PAIR_PREFIX).append(attrKey).append(COMMA_TAG).append(attrValue).toString();
        // contains(@...,...)
        keyValuePair = CONTAIN_TAG + LEFT_PARENTHESIS + keyValuePair + RIGHT_PARENTHESIS;
        if (!contains) {
            // not(contains(@..., ...))
            keyValuePair = NOT_CONTAIN_TAG + LEFT_PARENTHESIS + keyValuePair + RIGHT_PARENTHESIS;
        }
        filterPairList.add(keyValuePair);
    }

    public String getXPath() {
        int count = this.filterPairList.size();
        // add key value pair filter
        for (int index = 0; index < count; index++) {
            if (index == 0) {
                this.result.append(LEFT_SQUARE_BRAKET);
            } else {
                this.result.append(AND);
            }

            String keyValuePair = this.filterPairList.get(index);
            this.result.append(keyValuePair);

            if (index == count - 1) {
                this.result.append(RIGHT_SQUARE_BRAKET);
            }
        }

        // add pre filter
        if (this.prefixFilter != null) {
            return this.prefixFilter + this.result.toString();
        }
        return this.result.toString();
    }

}
