package org.apache.solr.flexibleqp;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.servlet.SolrDispatchFilter;
import org.junit.BeforeClass;

public class TestFlexibleQP extends SolrTestCaseJ4 {
    @BeforeClass
    public static void beforeClass() throws Exception {
        System.setProperty(SolrDispatchFilter.SOLR_INSTALL_DIR_ATTRIBUTE, TEST_HOME());
        initCore("solrconfig-minimal.xml", "schema-minimal.xml");
    }

    // todo equals hashcode
    /*
    ((spanNear([unstemmed_text:charge, unstemmed_text:account], 4, false)
spanNear([unstemmed_text:pledge, unstemmed_text:account], 4, false))
spanNear([unstemmed_text:pledge, unstemmed_text:deposit], 4, false))
spanNear([unstemmed_text:charge, unstemmed_text:deposit], 4, false)
    * */
    public void testEssential(){
        assertU(adoc("id", "3", "unstemmed_text", "account 1 2 3 4 charge"));
        assertU(commit());
        assertQ(req("q", "{!flex}unstemmed_text:fn:maxgaps(4 fn:unordered(charge account))", "indent", "true",
                "debugQuery", "true", "explainOther", "id:3"), "//*[@numFound='1']");
    }
}
