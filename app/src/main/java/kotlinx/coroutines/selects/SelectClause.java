package kotlinx.coroutines.selects;

import kotlin.jvm.functions.Function3;

public interface SelectClause {
    Object getClauseObject();

    Function3 getOnCancellationConstructor();

    Function3 getProcessResFunc();

    Function3 getRegFunc();
}
