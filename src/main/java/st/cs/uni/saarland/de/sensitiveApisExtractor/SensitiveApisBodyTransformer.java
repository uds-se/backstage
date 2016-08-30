package st.cs.uni.saarland.de.sensitiveApisExtractor;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import st.cs.uni.saarland.de.helpClasses.Helper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by avdiienko on 27/01/16.
 */
public class SensitiveApisBodyTransformer extends BodyTransformer {

    private final List<String> sensitiveApis;
    private final List<String> results;

    public SensitiveApisBodyTransformer(List<String> sensitiveApis, List<String> foundApis){
        this.sensitiveApis = sensitiveApis;
        this.results = foundApis;
    }

    @Override
    protected void internalTransform(Body body, String s, Map<String, String> map) {
        if(Helper.isClassInSystemPackage(body.getMethod().getDeclaringClass().getName())){
            return;
        }
        final PatchingChain<Unit> units = body.getUnits();
        for(final Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
            final Unit u = iter.next();
            u.apply(new AbstractStmtSwitch() {


                public void caseInvokeStmt(InvokeStmt stmt) {
                    processInvokeExpr(stmt.getInvokeExpr());
                }

                public void caseAssignStmt(AssignStmt stmt){
                    if(stmt.containsInvokeExpr()){
                        processInvokeExpr(stmt.getInvokeExpr());
                    }
                }

                private void processInvokeExpr(InvokeExpr expr){
                    if(sensitiveApis.contains(expr.getMethod().getSignature())){
                        results.add(expr.getMethod().getSignature());
                    }
                }


            });
        }
    }
}
