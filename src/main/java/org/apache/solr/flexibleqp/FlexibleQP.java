package org.apache.solr.flexibleqp;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.parser.QueryParser;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SolrQueryParser;
import org.apache.solr.search.SyntaxError;

public class FlexibleQP extends QParserPlugin {
    @Override
    public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
        return new QParser(qstr,localParams,params,req) {
            @Override
            public Query parse() throws SyntaxError {
                String qstr = getString();
                if (qstr == null || qstr.length()==0) return null;

                String defaultField = getParam(CommonParams.DF);
                StandardQueryParser lparser = new StandardQueryParser(req.getSchema().getQueryAnalyzer());

                final QueryParser.Operator operator = QueryParsing.parseOP(getParam(QueryParsing.OP));
                lparser.setDefaultOperator(StandardQueryConfigHandler.Operator.valueOf(operator.name()));
                try {
                    return lparser.parse(qstr, defaultField);
                } catch (QueryNodeException e) {
                    throw new SyntaxError(e);
                }
            }
        };
    }
}
