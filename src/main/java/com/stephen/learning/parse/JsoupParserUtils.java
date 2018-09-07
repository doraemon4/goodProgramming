package com.stephen.learning.parse;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import com.sun.org.apache.xerces.internal.dom.ElementImpl;



/**
 * @Auther: jack
 * @Date: 2018/9/6 21:42
 * @Description: Jsoup的xpath解析工具类
 *
 * 此处遇到一个问题在项目中一直报错找不到原因，本地测试没有错
 * getJsoupElements一直返回为空，但是自己测试明明有值，注意到代码161-163此处的问题
 * com.sun.org.apache.xerces.internal.dom.ElementImpl
 * 和org.apache.xerces.dom.ElementImpl转换异常
 *
 * 最后发现当项目中有xerces的jar时候会优先使用org.apache.xerces.dom.ElementImpl，
 * 所以才会出现这个问题
 * <dependency>
 *    <groupId>xerces</groupId>
 *    <artifactId>xercesImpl</artifactId>
 *    <version>2.11.0</version>
 * </dependency>
 */
@Slf4j
public class JsoupParserUtils {
    protected final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private final static XPath xPath = XPathFactory.newInstance().newXPath();

    protected static TransformerFactory tf = TransformerFactory.newInstance();

    private static final Lock LOCK = new ReentrantLock();


    /**
     * 得到该节点的子节点个数
     * 
     * @param ele
     * @param xpath
     * @return
     */
    public static int getEleChildNum(final org.jsoup.nodes.Element ele, final String xpath) {
        try {
            Object res = parse(ele, xpath, XPathConstants.NODESET);
            if (null != res && res instanceof NodeList) {
                NodeList nodeList = (NodeList) res;
                return nodeList == null ? 0 : nodeList.getLength();
            }
        } catch (Exception e) {
            log.error("根据xpath:{}，获取子节点个数出现错误,错误原因：" + e.getMessage(), xpath);
        }
        return 0;
    }


    /**
     * 判断文档中是否存在xpath节点
     * 
     * @param document
     * @param xpath
     * @return
     */
    public static boolean exists(final org.jsoup.nodes.Element ele, final String xpath) {
        try {
            Object res = parse(ele, xpath, XPathConstants.BOOLEAN);
            if (null != res && res instanceof Boolean) {
                return (boolean) res;
            }
            return false;
        } catch (Exception e) {
            log.error("检查xpath:{}，是否存在时出现错误,！" + e.getMessage(), xpath);
        }
        return false;
    }


    /**
     * 根据xpath得到w3c的Element对象
     * 
     * @param document
     * @param xpath
     * @return
     */
    public static ElementImpl getW3cElementImpl(final org.jsoup.nodes.Element ele, final String xpath) {
        try {
            Object res = parse(ele, xpath, XPathConstants.NODE);
            if (null != res && res instanceof ElementImpl) {
                return (ElementImpl) res;
            }
            return null;
        } catch (Exception e) {
            log.error("根据xpath：{}，得到w3c的Element对象出现错误，原因：" + e.getMessage(), xpath);
        }
        return null;
    }


    /**
     * 根据xpath得到jsoup的Element对象
     * 
     * @param document
     * @param xpath
     * @return
     */
    public static org.jsoup.nodes.Element getJsoupElement(final org.jsoup.nodes.Element ele, final String xpath) {
        try {
            Object res = parse(ele, xpath, XPathConstants.NODE);
            if (null != res && res instanceof ElementImpl) {
                ElementImpl elementImpl = (ElementImpl) res;
                return getJsoupEle(elementImpl);
            }
            return null;
        } catch (Exception e) {
            log.error("根据xpath：{}，得到jsoup的Element对象出现错误，原因：" + e.getMessage(), xpath);
        }
        return null;
    }


    /**
     * 根据xpath得到jsoup的Elements对象
     * 
     * @param document
     * @param xpath
     * @return
     */
    public static Elements getJsoupElements(final org.jsoup.nodes.Element ele, final String xpath) {
        try {
            NodeList nodeList = getNodeList(ele, xpath);
            if (null != nodeList && nodeList.getLength() > 0) {
                int len = nodeList.getLength();
                Elements elements = new Elements();
                for (int i = 0; i < len; i++) {
                    Node node = nodeList.item(i);
                    if (null != node && node instanceof ElementImpl) {
                        org.jsoup.nodes.Element element = getJsoupEle(((ElementImpl) node));
                        elements.add(element);
                    }
                }
                return elements;
            }
        } catch (Exception e) {
            log.error("根据xpath：{}，得到jsoup的Element对象出现错误，原因：" + e.getMessage(), xpath);
        }
        return null;
    }


    /**
     * 从Jsoup的Element中解析出W3C的NodeList
     * 
     * @param ele
     * @param xpath
     * @return
     */
    public static NodeList getNodeList(final org.jsoup.nodes.Element ele, final String xpath) {
        try {
            Object res = parse(ele, xpath, XPathConstants.NODESET);
            if (null != res && res instanceof NodeList) {
                return (NodeList) res;
            }
        } catch (Exception e) {
        }


        return null;
    }


    /**
     * 得到节点的某一个属性值
     * 
     * @param document
     * @param xpath
     * @return
     */
    public static String getXpathString(final org.jsoup.nodes.Element ele, final String xpath) {
        try {
            int textNum = getEleChildNum(ele, xpath);
            if (1 == textNum) {
                Object res = parse(ele, xpath, XPathConstants.STRING);
                if (null != res) {
                    return res.toString();
                }
            } else {
                List<String> res = getXpathListString(ele, xpath);
                if (res != null && res.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Iterator<String> iterator = res.iterator(); iterator.hasNext();) {
                        String text = iterator.next();
                        if (null != text) {
                            stringBuilder.append(text.replace("\r\n", "."));
                        }
                    }
                    return stringBuilder.toString();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据xpath:{}查询字符串时出现错误:" + e.getMessage(), xpath);
        }
        return null;
    }


    /**
     * 查询字符串列表
     * 
     * @param document
     * @param xpath
     * @return
     */
    public static List<String> getXpathListString(final org.jsoup.nodes.Element ele, final String xpath) {
        try {
            Object res = parse(ele, xpath, XPathConstants.NODESET);
            if (null != res && res instanceof NodeList) {
                NodeList nodeList = (NodeList) res;
                int length = nodeList.getLength();
                if (length <= 0) {
                    return null;
                }
                List<String> list = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    Node node = nodeList.item(i);
                    list.add(null == node ? null : node.getNodeValue());
                }
                return list;
            }
            return null;
        } catch (Exception e) {
            log.error("根据xpath:{}查询字符串列表时出现错误:" + e.getMessage(), xpath);
        }
        return null;
    }


    /**
     * 获取xpath解析结果
     * 
     * @param doc
     * @param xPathStr
     * @param qName
     * @return
     */
    public static Object parse(final org.jsoup.nodes.Element doc, final String xPathStr, final QName qName) {
        Node node = fromJsoup(doc);
        return parse(node, xPathStr, qName);
    }


    /**
     * 
     * @param doc
     * @param xPathStr
     * @param qName
     * @return
     */
    public static Object parse(final Node doc, final String xPathStr, final QName qName) {
        try {
            if (doc == null) {
                log.warn("解析文档为null！");
                return null;
            }
            if (StringUtils.isBlank(xPathStr)) {
                log.warn("解析的Xpath路径为空！");
                return null;
            }
            if (null == qName) {
                log.warn("解析类型为null！");
                return null;
            }
            try {
                LOCK.lock();
                Object res = xPath.evaluate(xPathStr, doc, qName);
                return res;
            } finally {
                LOCK.unlock();
            }
        } catch (Exception e) {
            log.warn("解析Xpath：{}，出现错误,解析类型：{}，错误原因：{}！", xPathStr, qName, e.getMessage());
        }
        return null;
    }


    /**
     * 根据ElementImpl得到Jsoup的Element
     * 
     * @param elementImpl
     * @return
     */
    public static org.jsoup.nodes.Element getJsoupEle(final ElementImpl elementImpl) {
        try {
            String value = getW3cDocString(elementImpl);
            org.jsoup.nodes.Document document = Jsoup.parse(value);
            return document.body().child(0);
        } catch (Exception e) {
            log.error("根据ElementImpl得到Jsoup的Element出现错误，错误原因：" + e.getMessage());
            return null;
        }


    }


    /**
     * 将w3c的Document转为jsoup的Document
     * 
     * @param in
     * @return
     */
    public static org.jsoup.nodes.Document fromW3C(final Document doc) throws Exception {
        String string = getW3cDocString(doc);
        org.jsoup.nodes.Document res = Jsoup.parse(string);
        return res;


    }


    /**
     * 将jsoup的Document转为w3c的Document
     * 
     * @param in
     * @return
     */
    public static Node fromJsoup(final org.jsoup.nodes.Element in) {
        DocumentBuilder builder;
        try {
            if (null == in) {
                return null;
            }
            builder = factory.newDocumentBuilder();
            Document out = builder.newDocument();
            if (in instanceof org.jsoup.nodes.Document) {
                List<org.jsoup.nodes.Node> childs = in.childNodes();
                if (childs != null && childs.size() > 0) {
                    org.jsoup.nodes.Element rootEl = in.child(0);
                    NodeTraversor traversor = new NodeTraversor(new W3CBuilder(out));
                    traversor.traverse(rootEl);
                    return out;
                } else {
                    return out;
                }
            }else if (in instanceof org.jsoup.nodes.Element) {
                NodeTraversor traversor = new NodeTraversor(new W3CBuilder(out));
                traversor.traverse(in);
                return out;
            }


        } catch (ParserConfigurationException e) {
            return null;
        }
        return null;
    }


    /**
     * 将W3c的doc转为字符串
     * 
     * @param doc
     * @return
     * @throws Exception
     */
    public static String getW3cDocString(final Node doc) throws Exception {
        try (StringWriter writer = new StringWriter()) {
            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(writer);
            LOCK.lock();
            try {
                Transformer transformer = tf.newTransformer();
                transformer.transform(domSource, result);
                return writer.toString();
            } finally {
                LOCK.unlock();
            }
        } catch (TransformerException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * 将Jsoup的node属性拷贝到w3c的Element中
     * 
     * @param source
     * @param el
     */
    public static void copyAttributes(final org.jsoup.nodes.Node source, final Element el) {
        for (Attribute attribute : source.attributes()) {
            el.setAttribute(attribute.getKey(), attribute.getValue());
        }
    }


}


class W3CBuilder implements NodeVisitor {
    private final Document doc;
    private Element dest;


    public W3CBuilder(Document doc) {
        this.doc = doc;
    }


    public void head(final org.jsoup.nodes.Node source, int depth) {
        if (source instanceof org.jsoup.nodes.Element) {
            org.jsoup.nodes.Element sourceEl = (org.jsoup.nodes.Element) source;
            Element el = doc.createElement(sourceEl.tagName());
            JsoupParserUtils.copyAttributes(sourceEl, el);
            if (dest == null) {
                doc.appendChild(el);
            } else {
                dest.appendChild(el);
            }
            dest = el;
        } else if (source instanceof org.jsoup.nodes.TextNode) {
            org.jsoup.nodes.TextNode sourceText = (org.jsoup.nodes.TextNode) source;
            Text text = doc.createTextNode(sourceText.getWholeText());
            dest.appendChild(text);
        } else if (source instanceof org.jsoup.nodes.Comment) {
            org.jsoup.nodes.Comment sourceComment = (org.jsoup.nodes.Comment) source;
            Comment comment = doc.createComment(sourceComment.getData());
            dest.appendChild(comment);
        } else if (source instanceof org.jsoup.nodes.DataNode) {
            org.jsoup.nodes.DataNode sourceData = (org.jsoup.nodes.DataNode) source;
            Text node = doc.createTextNode(sourceData.getWholeData());
            dest.appendChild(node);
        } else {


        }
    }

    @Override
    public void tail(final org.jsoup.nodes.Node source, int depth) {
        if (source instanceof org.jsoup.nodes.Element && dest.getParentNode() instanceof Element) {
            dest = (Element) dest.getParentNode();
        }
    }
}

