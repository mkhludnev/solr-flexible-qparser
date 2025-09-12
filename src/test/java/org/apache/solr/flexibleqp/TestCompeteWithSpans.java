package org.apache.solr.flexibleqp;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.servlet.SolrDispatchFilter;
import org.junit.BeforeClass;

public class TestCompeteWithSpans  extends SolrTestCaseJ4 {
    @BeforeClass
    public static void beforeClass() throws Exception {
        System.setProperty(SolrDispatchFilter.SOLR_INSTALL_DIR_ATTRIBUTE, TEST_HOME());
        initCore("solrconfig-minimal.xml", "schema-minimal.xml");
        assertU(adoc("id", "1", "unstemmed_text", "lorem the domain is ipsum"));
        assertU(adoc("id", "2", "unstemmed_text", "lorem the domain name system is ipsum"));
        assertU(adoc("id", "3", "unstemmed_text", "lorem the domain blame system is ipsum"));
        assertU(commit());
    }


    public void testFlex() {
        assertQ(req("q", "{!flex}unstemmed_text:fn:phrase(the fn:or(domain fn:phrase(domain name system)) is)", "indent", "true",
                "debugQuery", "true", "explainOther", "id:3"), "//*[@numFound='2']");
    }

    public void testSpanNear() {
        assertQ(req("q", "{!xmlparser df=unstemmed_text}"
                                + "  <SpanNear slop=\"0\" inOrder=\"true\">\n"
                                + "    <SpanTerm fieldName=\"unstemmed_text\">domain</SpanTerm>\n"
                                + "    <SpanTerm fieldName=\"unstemmed_text\">name</SpanTerm>\n"
                                + "    <SpanTerm fieldName=\"unstemmed_text\">system</SpanTerm>\n"
                                + "  </SpanNear>\n"
                        , "debugQuery", "true"),
                "//*[@numFound='1']");
    }

    public void testSpanNearSpanOr() {
        assertQ(req("q", "{!xmlparser df=unstemmed_text}"
                        + "<SpanNear slop=\"0\" inOrder=\"true\" fieldName=\"unstemmed_text\"> "
                        + "<SpanTerm fieldName=\"unstemmed_text\">the</SpanTerm>"
                        + "<SpanTerm fieldName=\"unstemmed_text\">domain</SpanTerm>\n"
                        + "<SpanOr>\n"
                        + "  <SpanTerm fieldName=\"unstemmed_text\">name</SpanTerm>\n"
                        + "  <SpanTerm fieldName=\"unstemmed_text\">is</SpanTerm>"
                        + "</SpanOr>"
                        + "</SpanNear>", "debugQuery", "true"),
                "//*[@numFound='2']");
    }

    public void testSpanNearSpanOrSpanNear() {
        assertQ("LUCENE-7398", req("q", "{!xmlparser df=unstemmed_text}"
                        + "<SpanNear slop=\"0\" inOrder=\"true\" fieldName=\"unstemmed_text\"> "
                        + "<SpanTerm fieldName=\"unstemmed_text\">the</SpanTerm>"
                        + "<SpanOr>\n"
                        + "  <SpanTerm fieldName=\"unstemmed_text\">domain</SpanTerm>\n"
                        + "  <SpanNear slop=\"0\" inOrder=\"true\">\n"
                        + "    <SpanTerm fieldName=\"unstemmed_text\">domain</SpanTerm>\n"
                        + "    <SpanTerm fieldName=\"unstemmed_text\">name</SpanTerm>\n"
                        + "    <SpanTerm fieldName=\"unstemmed_text\">system</SpanTerm>\n"
                        + "  </SpanNear>\n"
                        + "</SpanOr>"
                        + "<SpanTerm fieldName=\"unstemmed_text\">is</SpanTerm>"
                        + "</SpanNear>",  "debugQuery", "true"),
                "//*[@numFound='2']");
    }
}
