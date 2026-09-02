package androidx.work.impl.model;

public interface WorkProgressDao {
    void delete(String str);

    void deleteAll();

    void insert(WorkProgress workProgress);
}
